/* ----------------------------------------------------------------------------
 * Copyright (C) 2015      European Space Agency
 *                         European Space Operations Centre
 *                         Darmstadt
 *                         Germany
 * ----------------------------------------------------------------------------
 * System                : ESA NanoSat MO Framework
 * ----------------------------------------------------------------------------
 * Licensed under the European Space Agency Public License, Version 2.0
 * You may not use this file except in compliance with the License.
 *
 * Except as expressly set forth in this License, the Software is provided to
 * You on an "as is" basis and without warranties of any kind, including without
 * limitation merchantability, fitness for a particular purpose, absence of
 * defects or errors, accuracy or non-infringement of intellectual property rights.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * ----------------------------------------------------------------------------
 */
package esa.mo.nmf.apps;

//import at.tugraz.ihf.opssat.iadcs.*;
//import at.tugraz.ihf.opssat.iadcs.SEPP_IADCS_API;
import esa.mo.nmf.NMFException;
import esa.mo.nmf.nanosatmoconnector.NanoSatMOConnectorImpl;
//import java.util.Timer;
//import java.util.TimerTask;
import esa.mo.helpertools.misc.TaskScheduler;
import static esa.mo.nmf.apps.FuzzyControlAdapter.mtqDipoleMoment;
import static esa.mo.nmf.apps.Util.calculate_md5;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static java.lang.Math.sqrt;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
//import org.ccsds.moims.mo.com.structures.ObjectId;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.MALInteractionException;
import org.ccsds.moims.mo.mal.structures.FloatList;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.ActuatorsTelemetry;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.AttitudeTelemetry;
//import net.sourceforge.jFuzzyLogic.plot.JDialogFis;
//import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.Quaternion;
//import org.ccsds.moims.mo.platform.autonomousadcs.structures.ReactionWheelIdentifier;
import org.ccsds.moims.mo.platform.structures.VectorF3D;
import org.hipparchus.linear.Array2DRowRealMatrix;
import org.orekit.propagation.analytical.tle.TLE;



/**
 * The demo app class for FuzzyControl
 */
public class FuzzyControl {
    static final NanoSatMOConnectorImpl connector = new NanoSatMOConnectorImpl();
    static final TaskScheduler TIMER = new TaskScheduler(1);
    protected static final int REFRESH_RATE =200;
    static Quaternion currentAttitude = new Quaternion();
    private static Quaternion targetAttitude = new Quaternion(1.0f, 0.0f, 0.0f, 0.0f);
    static Quaternion error = new Quaternion(1.0f, 0.0f, 0.0f, 0.0f);
    static Quaternion previousError = new Quaternion(0f, 0f, 0f, 0f);
    static Quaternion errorDerivative = new Quaternion();
    static VectorF3D angularVelocity = new VectorF3D(0.0f, 0.0f, 0.0f);
    static VectorF3D magneticField = new VectorF3D();
    static VectorF3D actuation = new VectorF3D(0.0f, 0.0f, 0.0f);
    static VectorF3D torque = new VectorF3D(0.0f, 0.0f, 0.0f);
    static FloatList wheelTargetVelocities = new FloatList(3);
    static FunctionBlock fb_x;
    static FunctionBlock fb_y;
    static FunctionBlock fb_z;
    private static String controllerType = "Fuzzy";
    static ScriptExecutor TIMELINE_EXECUTOR = new ScriptExecutor("timeline.js");
    public static boolean desaturating = false;
    static final Float WHEEL_MAX_SPEED = (float)(0.8*10000.0*PI/30.0) ;
    static final Float WHEEL_MAX_TORQUE = (float)(0.8e-4) ;
    static final Float WHEEL_INERTIA = (float)(1.5465e-6) ;
    static final File TELEMETRY_FILE = new File (System.getProperty("user.dir")+"/toGroundLP/TM_"+String.valueOf(System.currentTimeMillis())+".dat");
    static final File LOG_FILE = new File (System.getProperty("user.dir")+"/toGround/log_"+String.valueOf(System.currentTimeMillis())+".txt");
//    static final File TELEMETRY_FILE = new File (System.getProperty("user.dir")+"/toGroundLP/TM_"+String.valueOf(System.currentTimeMillis())+".dat");
//    static final File LOG_FILE = new File (System.getProperty("user.dir")+"/toGround/log_"+String.valueOf(System.currentTimeMillis())+".txt");
    static FuzzyControlAdapter adapter;
    static AttitudeTelemetry attitudeTm;
    static ActuatorsTelemetry actuatorsTm;
    static boolean iADCSinitialConectionError=true;
    static int reconnectionTry = 0;
    static boolean controlFlag = true;
    static HinftyController Hx = new HinftyController("defaultX");
    static HinftyController Hy = new HinftyController("defaultY");
    static HinftyController Hz = new HinftyController("defaultZ");

 //   static SEPP_IADCS_API adcsApi= new SEPP_IADCS_API();

    /**
     * Main command line entry point.
     *
     * @param args the command line arguments
     * @throws java.lang.Exception If there is an error
     */
    public static void main(final String args[]) {
        if(args.length!=0){
            Comms.setPortNumber(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
            PrintWriter headerWriter = null;
            try {
                headerWriter = new PrintWriter(new FileWriter(FuzzyControl.TELEMETRY_FILE, true));
                for(String s:args){
                    headerWriter.print(s+" ");
                }
                headerWriter.println();
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                headerWriter.close();
            }

        }
        Comms.initComms();
        
        

        try {
            Files.deleteIfExists(new File("comArchive.db").toPath());
        } catch (IOException ex) {
            Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        checkFilesUpdates();
        Util.initOrekitFiles();
        
        // adapter = new FuzzyControlAdapter(connector);
        // connector.init(adapter);
//        adapter.startAdcsAttitudeMonitoring();
        
        initiateFIS("Fuzzy_CP.fcl");
        
        // CP PID gains:
        PIDComparator.setPIDGains(2.15e+1f, 7.07e-1f, 5.15e+2f, 2.05e+1f, 8.57e-1f, 5.22e+2f, 1.46e+1f, 9.95e-1f, 1.74e+2f);
        
        //LC PID gains:
        // PIDComparator.setPIDGains(5.98e+0f, 5.21e-2f, 3.43e+2f, 1.19e+1f, 1.84e-1f, 5.87e+2f, 3.49e+0f, 1.82e-1f, 9.45e+1f);
       
        //LE PID gains:
        // PIDComparator.setPIDGains(4.54e+1f, 9.43e-1f, 3.55e+2f, 1.34e+2f, 7.77e-2f, 5.94e+2f, 9.55e+1f, 2.61e-1f, 2.47e+2f);

        

        
        // while (iADCSinitialConectionError){
        //     iADCSinitialConectionError = setIADCSIdleMode();
        //     try {
        //         Thread.sleep(5000);
        //         reconnectionTry++;
        //         if (reconnectionTry > 10) {
        //                 Thread.sleep(300000);
        //                 reconnectionTry = 0;
        //             }
        //     } catch (InterruptedException ex1) {
        //         Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex1);
        //     }
        // }
        
        for (int i=0;i<3;i++){
            wheelTargetVelocities.add(i,(float)0.0);
        }
            
        // updateTLE();
        
        // TIMELINE_EXECUTOR.start();
        
//        executeMain(REFRESH_RATE, 5000);                                                                         
        executeMain(200, 0); 
    
//        TIMER.scheduleTask(new Thread() {
//            @Override
//            public void run() {
//        Comms.read42();
//        Comms.write42(torque);
//    }
//    }, 0, 200, TimeUnit.MILLISECONDS, true); 
    System.exit(0);
    }
    
    static void executeMain(int refreshRate, int initialDelay){
        int i=0;
        while (i < 600*1000/200) {
            i=i+1;
        // TIMER.scheduleTask(new Thread() {
        //     @Override 
        //     public void run() {
//                try {
//                    if (reconnectionTry > 10) {
//                        Thread.sleep(300000);
//                        reconnectionTry = 0;
//                    }
                    Comms.read42();
//                    attitudeTm = connector.getPlatformServices().getAutonomousADCSService().getStatus().getBodyElement0();
//                    OrbitalFrame.nmfSunVector = attitudeTm.getSunVector();
//                    magneticField = attitudeTm.getMagneticField();
//                    angularVelocity = attitudeTm.getAngularVelocity();
//                    FuzzyControlAdapter.setInertialAttitude(attitudeTm.getAttitude());
//                    actuatorsTm = connector.getPlatformServices().getAutonomousADCSService().getStatus().getBodyElement1();
//                    mtqDipoleMoment = actuatorsTm.getMtqDipoleMoment();
//                    FuzzyControlAdapter.wheelsSpeed = actuatorsTm.getCurrentWheelSpeed().getRotationalSpeed();
                                        
                    OrbitalFrame.getOrbitalFrame();
                    currentAttitude = OrbitalFrame.getOrbitalAttitude();
//                    System.out.println("atitude = "+currentAttitude.toString());
                    // if (controlFlag){
                    if ((i % 1)==0){
                        executeControlLoop();
                    }
                    Comms.write42(torque);
                    reconnectionTry = 0;
                    Util.writeTelemtry();
//                } catch (NMFException | IOException | MALInteractionException | MALException ex) {
//                    reconnectionTry++;
////                    Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex);
//                    log("iADCS not responding");
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        // }, initialDelay, refreshRate, TimeUnit.MILLISECONDS, true);        
    }

    
    static void executeControlLoop() {
//        System.out.println("init control loop");
        updateError();
//        System.out.println("error = "+error.toString());
        updateErrorDerivative();
//        System.out.println("error derivative = "+errorDerivative.toString());
        switch (controllerType) {
            case "Fuzzy":
                fisEvaluate();
                break;

            case "PID":
                PIDComparator.updateErrorIntegral();
                PIDComparator.computeActuation();
                break;

            case "Hinfty":
                double angles[];
                angles = OrbitalFrame.getNavigationAngles();
                // System.out.println("angles = "+angles[0]+" "+angles[1]+" "+angles[2]);
                // actuation.setX(0.0f);
                // actuation.setY(0.0f);
                // actuation.setZ(0.0f);
                double[][] ux = {{angles[2]}};
                actuation.setX((float)Hx.integrateOneStep(new Array2DRowRealMatrix(ux), REFRESH_RATE/1000.).getEntry(0, 0)*WHEEL_INERTIA/WHEEL_MAX_TORQUE);               
                double[][] uy = {{angles[1]}};
                actuation.setY((float)Hy.integrateOneStep(new Array2DRowRealMatrix(uy), REFRESH_RATE/1000.).getEntry(0, 0)*WHEEL_INERTIA/WHEEL_MAX_TORQUE);
                double[][] uz = {{angles[0]}};
                actuation.setZ((float)Hz.integrateOneStep(new Array2DRowRealMatrix(uz), REFRESH_RATE/1000.).getEntry(0, 0)*WHEEL_INERTIA/WHEEL_MAX_TORQUE);
                break; 
        
            default:
                System.out.println("Unrecognize controller type. Using Fuzzy");
                fisEvaluate();
                break;
        }

//        System.out.println("init control actuation");
        applyActuation();
//        System.out.println("check saturation");
        //check wheels speed and desaturation flag and desaturate if needed (any w>0.9wmax and desaturating =flase)
        if ((desaturating == false) && desaturationNeeded() ){
            desaturateWheels();
        }
//        System.out.println("Ending control loop");
    }

    static void updateError(){
        error.setB(-targetAttitude.getA()*currentAttitude.getB()+targetAttitude.getB()*currentAttitude.getA()+targetAttitude.getC()*currentAttitude.getD()-targetAttitude.getD()*currentAttitude.getC());
        error.setC(-targetAttitude.getA()*currentAttitude.getC()+targetAttitude.getC()*currentAttitude.getA()-targetAttitude.getB()*currentAttitude.getD()+targetAttitude.getD()*currentAttitude.getB());
        error.setD(-targetAttitude.getA()*currentAttitude.getD()+targetAttitude.getD()*currentAttitude.getA()+targetAttitude.getB()*currentAttitude.getC()-targetAttitude.getC()*currentAttitude.getB());
        error.setA( targetAttitude.getA()*currentAttitude.getA()+targetAttitude.getB()*currentAttitude.getB()+targetAttitude.getC()*currentAttitude.getC()+targetAttitude.getD()*currentAttitude.getD());
    
    if (error.getA() <0){
	error.setB(-error.getB());
	error.setC(-error.getC());
        error.setD(-error.getD());
        error.setA(-error.getA());
    }
     float suma =  error.getA()*error.getA() + error.getB()*error.getB() + error.getC()*error.getC() + error.getD()*error.getD();
     suma = (float) sqrt(suma);
     
     error.setB(error.getB()/suma);
     error.setC(error.getC()/suma);
     error.setD(error.getD()/suma);
     error.setA(error.getA()/suma);             
    }
    
    static void updateErrorDerivative(){
        errorDerivative.setA((error.getA()-previousError.getA())/(REFRESH_RATE/1000f));
        errorDerivative.setB((error.getB()-previousError.getB())/(REFRESH_RATE/1000f));
        errorDerivative.setC((error.getC()-previousError.getC())/(REFRESH_RATE/1000f));
        errorDerivative.setD((error.getD()-previousError.getD())/(REFRESH_RATE/1000f));
        
        previousError.setA(error.getA());
        previousError.setB(error.getB());
        previousError.setC(error.getC());
        previousError.setD(error.getD());
        
    }
    
    public static void initiateFIS(String FIS_filename){
        FIS fis = FIS.load(FIS_filename, true);
        
        if (fis == null) {
                System.err.println("Can't load file: '" + FIS_filename + "'");
                System.exit(1);
        }
                
        fb_x = fis.getFunctionBlock("X_axis");
        fb_y = fis.getFunctionBlock("Y_axis");
        fb_z = fis.getFunctionBlock("Z_axis");               
    }
    
    static void fisEvaluate(){
        fb_x.setVariable("Error", error.getB());
        fb_x.setVariable("Error_derivative", errorDerivative.getB());
        fb_x.evaluate();
        actuation.setX((float)(fb_x.getVariable("Actuation").defuzzify()));
        
        fb_y.setVariable("Error", error.getC());
        fb_y.setVariable("Error_derivative", errorDerivative.getC());
        fb_y.evaluate();
        actuation.setY((float)(fb_y.getVariable("Actuation").defuzzify()));
        
        fb_z.setVariable("Error", error.getD());
        fb_z.setVariable("Error_derivative", errorDerivative.getD());
        fb_z.evaluate();
        actuation.setZ((float)(fb_z.getVariable("Actuation").defuzzify()));
               
    }
    
    static void applyActuation(){
//        System.out.println("actuation ="+ actuation);
        // try {
            if (abs(actuation.getX())<0.5){
                actuation.setX(2*actuation.getX());
            } else {
                actuation.setX((float)1.0*signum(actuation.getX()));
            }
            if (abs(actuation.getY())<0.5){
                actuation.setY(2*actuation.getY());
            } else {
                actuation.setY((float)1.0*signum(actuation.getY()));
            }
            if (abs(actuation.getZ())<0.5){
                actuation.setZ(2*actuation.getZ());
            } else {
                actuation.setZ((float)1.0*signum(actuation.getZ()));
            }
             
            //actuation in torque with 10% of whell comand error
            // torque.setX(actuation.getX()*WHEEL_MAX_TORQUE+0.1f*WHEEL_MAX_TORQUE);
            // torque.setY(actuation.getY()*WHEEL_MAX_TORQUE+0.1f*WHEEL_MAX_TORQUE);
            // torque.setZ(actuation.getZ()*WHEEL_MAX_TORQUE+0.1f*WHEEL_MAX_TORQUE);
            //Nominal actuation in torque
           torque.setX(actuation.getX()*WHEEL_MAX_TORQUE);
           torque.setY(actuation.getY()*WHEEL_MAX_TORQUE);
           torque.setZ(actuation.getZ()*WHEEL_MAX_TORQUE);
//            System.out.println("torque commanded ="+torque);
            
            
            //actuation in acceleration
            actuation.setX(torque.getX()/WHEEL_INERTIA);
            actuation.setY(torque.getY()/WHEEL_INERTIA);
            actuation.setZ(torque.getZ()/WHEEL_INERTIA);
            
            
//            for (int i=0; i<6;i++){
//               System.out.println("Wheel "+ i+" : "+FuzzyControlAdapter.wheelsSpeed.get(0));
//            }
            
// Get only three velocities          

//            wheelTargetVelocities.set(0, actuation.getX()*REFRESH_RATE/1000f+FuzzyControlAdapter.wheelsSpeed.get(0));
//            wheelTargetVelocities.set(1, actuation.getY()*REFRESH_RATE/1000f+FuzzyControlAdapter.wheelsSpeed.get(1));
//            wheelTargetVelocities.set(2, actuation.getZ()*REFRESH_RATE/1000f+FuzzyControlAdapter.wheelsSpeed.get(2));
            
            wheelTargetVelocities.set(0, actuation.getX()*REFRESH_RATE/1000f+Comms.wheelsSpeed.get(0));
            wheelTargetVelocities.set(1, actuation.getY()*REFRESH_RATE/1000f+Comms.wheelsSpeed.get(1));
            wheelTargetVelocities.set(2, actuation.getZ()*REFRESH_RATE/1000f+Comms.wheelsSpeed.get(2));
           
            
//            System.out.println("target = "+wheelTargetVelocities); 
// Get six velocites
// System.out.println(adcsApi.Get_ReactionWheel_All_Speeds().getINTERNAL());
//            wheelTargetVelocities.set(0, actuation.getX()*REFRESH_RATE+adcsApi.Get_ReactionWheel_All_Speeds().getINTERNAL().getX());
//            wheelTargetVelocities.set(1, actuation.getY()*REFRESH_RATE+adcsApi.Get_ReactionWheel_All_Speeds().getINTERNAL().getY());
//            wheelTargetVelocities.set(2, actuation.getZ()*REFRESH_RATE+adcsApi.Get_ReactionWheel_All_Speeds().getINTERNAL().getZ());
//            wheelTargetVelocities.set(3, actuation.getX()*REFRESH_RATE+adcsApi.Get_ReactionWheel_All_Speeds().getEXTERNAL().getU());
//            wheelTargetVelocities.set(4, actuation.getY()*REFRESH_RATE+adcsApi.Get_ReactionWheel_All_Speeds().getEXTERNAL().getV());
//            wheelTargetVelocities.set(5, actuation.getZ()*REFRESH_RATE+adcsApi.Get_ReactionWheel_All_Speeds().getEXTERNAL().getW());

         
            for (int i=0; i<3;i++){
               if (abs(wheelTargetVelocities.get(i))>WHEEL_MAX_SPEED){
                   wheelTargetVelocities.set(i,signum(wheelTargetVelocities.get(i))*WHEEL_MAX_SPEED);
               }
               switch (i){
                   case 0: torque.setX(WHEEL_INERTIA*(wheelTargetVelocities.get(i)-Comms.wheelsSpeed.get(i))/(REFRESH_RATE/1000f));
                           break;
                   case 1: torque.setY(WHEEL_INERTIA*(wheelTargetVelocities.get(i)-Comms.wheelsSpeed.get(i))/(REFRESH_RATE/1000f));
                           break;
                   case 2: torque.setZ(WHEEL_INERTIA*(wheelTargetVelocities.get(i)-Comms.wheelsSpeed.get(i))/(REFRESH_RATE/1000f));
                           break;
               }
            }
//            Comms.write42(torque);
//            System.out.println("commanding wheels");
//            System.out.println(wheelTargetVelocities.toString());
// Set only three wheel velocities
//            connector.getPlatformServices().getAutonomousADCSService().setReactionWheelSpeed(
//                    ReactionWheelIdentifier.WHEEL_X,wheelTargetVelocities.get(0));
//            connector.getPlatformServices().getAutonomousADCSService().setReactionWheelSpeed(
//                    ReactionWheelIdentifier.WHEEL_Y,wheelTargetVelocities.get(1));
//            connector.getPlatformServices().getAutonomousADCSService().setReactionWheelSpeed(
//                    ReactionWheelIdentifier.WHEEL_Z,wheelTargetVelocities.get(2));
            
// Set the six wheel velocities
            // connector.getPlatformServices().getAutonomousADCSService().setAllReactionWheelSpeeds(
            // wheelTargetVelocities.get(0), wheelTargetVelocities.get(1), wheelTargetVelocities.get(2),(float)0.0,(float)0.0,(float)0.0); 
            //wheelTargetVelocities.get(3), wheelTargetVelocities.get(4), wheelTargetVelocities.get(5));

        // } catch (NMFException | IOException | MALInteractionException | MALException ex) {
        //     Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex);
        // }
    }
    
  public static  Quaternion getTargetAttitude(){
      return targetAttitude;
  }
  
  public static void setTargetAttitude( Float qa, Float qb, Float qc, Float qd ){
      targetAttitude.setA(qa);
      targetAttitude.setB(qb);
      targetAttitude.setC(qc);
      targetAttitude.setD(qd);  
    //   try {
    //       connector.pushParameterValue(FuzzyControlAdapter.PARAMETER_ATTITUDE_TARGET_Q_A, targetAttitude.getA());
    //       connector.pushParameterValue(FuzzyControlAdapter.PARAMETER_ATTITUDE_TARGET_Q_B, targetAttitude.getB());
    //       connector.pushParameterValue(FuzzyControlAdapter.PARAMETER_ATTITUDE_TARGET_Q_C, targetAttitude.getC());
    //       connector.pushParameterValue(FuzzyControlAdapter.PARAMETER_ATTITUDE_TARGET_Q_D, targetAttitude.getD());
    //   } catch (NMFException ex) {
    //       Logger.getLogger(FuzzyControlAdapter.class.getName()).log(Level.SEVERE, null, ex);
    //   }
      
  }
  
  public static void setControllerType(String controller){
      PIDComparator.resetErrorIntegral();
      controllerType = controller;
    //   try {
        //   connector.pushParameterValue(FuzzyControlAdapter.PARAMETER_CONTROLLER_TYPE, controllerType);
          if (controllerType.equals("PID")) {                      
              log("Kpx : " + PIDComparator.Kpx.toString());
              log("Kix : " + PIDComparator.Kix.toString());
              log("Kdx : " + PIDComparator.Kdx.toString());
              log("Kpy : " + PIDComparator.Kpy.toString());
              log("Kiy : " + PIDComparator.Kiy.toString());
              log("Kdy : " + PIDComparator.Kdy.toString());
              log("Kpz : " + PIDComparator.Kpz.toString());
              log("Kiz : " + PIDComparator.Kiz.toString());
              log("Kdz : " + PIDComparator.Kdz.toString());
          }
    //   } catch (NMFException ex) {
    //       Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex);
    //   }
  }
  
  public static String getControllerType(){
      return controllerType;
  }
  
  public static void desaturateWheels(){
      desaturating = true;
//      System.out.println("Desaturating wheels");
      WheelsMomentumManager.counter=0;
    //   WheelsMomentumManager.t = TIMER.scheduleTask(new WheelsMomentumManager(), 0, 200, TimeUnit.MILLISECONDS, true);
    new WheelsMomentumManager().start();
  }
  
  static boolean desaturationNeeded(){
      for (int i=0;i<3;i++){
//          if (abs(FuzzyControlAdapter.wheelsSpeed.get(i))>0.9*WHEEL_MAX_SPEED){
//              return true;
//          }
          if (abs(Comms.wheelsSpeed.get(i))>0.9*WHEEL_MAX_SPEED){
              return true;
          }
      }
      return false;
  }
  
  public static void log( String line){
        try {
            PrintWriter logger = new PrintWriter (new FileWriter(LOG_FILE, true));
            logger.println (System.currentTimeMillis()/1000L+" "+line);
            logger.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex);
        }
      
  }
  
  static void checkFilesUpdates(){
      File scriptFile = new File(System.getProperty("user.dir")+"/fromGround/timeline.js");
      File FuzzyCPFile = new File(System.getProperty("user.dir")+"/fromGround/Fuzzy_CP.fcl");
      File FuzzyLCFile = new File(System.getProperty("user.dir")+"/fromGround/Fuzzy_LC.fcl");
      File FuzzyLEFile = new File(System.getProperty("user.dir")+"/fromGround/Fuzzy_LE.fcl");
      
      if (scriptFile.exists()){
          Path target = Paths.get(System.getProperty("user.dir")+"/timeline.js");
          Path source = scriptFile.toPath();
          try {
              Files.move(source, target, REPLACE_EXISTING);
          } catch (IOException ex) {
              Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
      
      if (FuzzyCPFile.exists()){
          Path target = Paths.get(System.getProperty("user.dir")+"/Fuzzy_CP.fcl");
          Path source = FuzzyCPFile.toPath();
          try {
              Files.move(source, target, REPLACE_EXISTING);
          } catch (IOException ex) {
              Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
      
      if (FuzzyLCFile.exists()){
          Path target = Paths.get(System.getProperty("user.dir")+"/Fuzzy_LC.fcl");
          Path source = FuzzyLCFile.toPath();
          try {
              Files.move(source, target, REPLACE_EXISTING);
          } catch (IOException ex) {
              Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
      
      if (FuzzyLEFile.exists()){
          Path target = Paths.get(System.getProperty("user.dir")+"/Fuzzy_LE.fcl");
          Path source = FuzzyLEFile.toPath();
          try {
              Files.move(source, target, REPLACE_EXISTING);
          } catch (IOException ex) {
              Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
  }
  static void stopTimeline(){
      TIMELINE_EXECUTOR.interrupt();
  }
  
  public static void updateTLE(){
      String line1 = new String();
      String line2 = new String();
      ArrayList lines = new ArrayList(2);
      try {
            BufferedReader br = new BufferedReader(new FileReader("/etc/tle"));
            int numberOfLines = 0;
            lines.add(br.readLine());
//            System.out.println("line["+numberOfLines+"] = "+lines.get(numberOfLines)); 
            while (lines.get(numberOfLines) != null) {
                numberOfLines++;
                lines.add(br.readLine());
//            System.out.println("line["+numberOfLines+"] = "+lines.get(numberOfLines)); 
            }
            br.close();
            if (numberOfLines == 2){
                line1 = lines.get(0).toString();
                line2 = lines.get(1).toString();
            } else if (numberOfLines >= 3){
                line1 = lines.get(1).toString();
                line2 = lines.get(2).toString();
            }

            
            if (TLE.isFormatOK(line1, line2)){
                log("TLE line 1 = "+line1);
                log("TLE line 2 = "+line2);
                OrbitalFrame.opsSatTLE = new TLE(line1, line2); 
                System.out.println("TLE date = "+OrbitalFrame.opsSatTLE.getDate().toString());
            }
            
      } catch (IOException ex) {
            Logger.getLogger(OrbitalFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
  
//  static void tryReconection(){
//        try {
//            //      adapter.startAdcsAttitudeMonitoring();
//            connector.getPlatformServices().getAutonomousADCSService().setDesiredAttitude(
//                    null, null);
//        } catch (NMFException | IOException | MALInteractionException | MALException ex) {
//            Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex);
//            log("Error when setting the iADCS in idle mode: trying reconnection");
//            try {
//                Thread.sleep(20000);
//
//            }   catch (InterruptedException ex1) {
//                Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex1);
//            }
//            setIADCSIdleMode();
//        }
//  }
  
  static boolean setIADCSIdleMode(){
        try {
            connector.getPlatformServices().getAutonomousADCSService().setDesiredAttitude(
                    null, null);
             return false;
        } catch (NMFException | IOException | MALInteractionException | MALException ex) {
            Logger.getLogger(FuzzyControl.class.getName()).log(Level.SEVERE, null, ex);
            log("Error when setting the iADCS in idle mode: trying reconnection");         
            return true;
        }            
  }
  
  public static void setiADCSRefreshRate(int RefreshRate){
      TIMER.stopLast();
      executeMain(RefreshRate, 2000);
  }
  
  public static void switchControlFlag(){
      controlFlag = !controlFlag;
  }
  
}

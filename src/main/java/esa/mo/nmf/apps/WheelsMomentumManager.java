/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esa.mo.nmf.apps;

import esa.mo.nmf.NMFException;
import java.io.IOException;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.sqrt;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.MALInteractionException;
import org.ccsds.moims.mo.mal.structures.FloatList;
import org.ccsds.moims.mo.platform.structures.VectorF3D;

/**
 *
 * @author Karl
 */
public class WheelsMomentumManager extends Thread{
    
    static VectorF3D m = new VectorF3D();
    static VectorF3D B = new VectorF3D();
    static VectorF3D w = new VectorF3D();
    static VectorF3D wParallel = new VectorF3D();
    static VectorF3D wPerpendicular = new VectorF3D();
    static VectorF3D wPerpendicular_u = new VectorF3D();
    static final Float MAXIMUM_DIPOLE_MOMENT_XY = 0.2f; // Am^2
    static final Float MAXIMUM_DIPOLE_MOMENT_Z = 0.1f; // Am^2
    static double normWPerpendicular;
    static int t;
    static int counter;
    
    @Override    
    public void run() {
//        encender al 80% y apagar al 40% 
//        y la maniobra 0 en principio al 10%
        
//        if (FuzzyControl.connector== null || FuzzyControl.magneticField.getZ()==null || FuzzyControlAdapter.wheelsSpeed.get(2).isNaN()) {  // The framework is still not available
//                    return;
//        }
           
        updateWheelPerpendicular();
        
//"checking wherter to apply magntoruers or not");
    
        if (normWPerpendicular>0.15*FuzzyControl.WHEEL_MAX_SPEED) {
            
            wPerpendicular_u.setX(wPerpendicular.getX()/(float)normWPerpendicular);
            wPerpendicular_u.setY(wPerpendicular.getY()/(float)normWPerpendicular);
            wPerpendicular_u.setZ(wPerpendicular.getZ()/(float)normWPerpendicular);


            m.setX(-(B.getY()*wPerpendicular_u.getZ()-B.getZ()*wPerpendicular_u.getY()));
            m.setY(-(B.getZ()*wPerpendicular_u.getX()-B.getX()*wPerpendicular_u.getZ()));
            m.setZ(-(B.getX()*wPerpendicular_u.getY()-B.getY()*wPerpendicular_u.getX()));

            Float dipoleMax = max(abs(m.getX()), max(abs(m.getY()),abs(m.getZ())));
            m.setX(m.getX()/dipoleMax);
            m.setY(m.getY()/dipoleMax);
            m.setZ(m.getZ()/dipoleMax);
            
            m.setX(m.getX()*MAXIMUM_DIPOLE_MOMENT_XY);
            m.setY(m.getY()*MAXIMUM_DIPOLE_MOMENT_XY);
            m.setZ(m.getZ()*MAXIMUM_DIPOLE_MOMENT_XY);
            
            if (abs(m.getZ())>MAXIMUM_DIPOLE_MOMENT_Z) {
                Float scale = MAXIMUM_DIPOLE_MOMENT_Z/m.getZ();
                m.setX(m.getX()*scale);
                m.setY(m.getY()*scale);
                m.setZ(m.getZ()*scale);
            }
            Comms.m.setX(m.getX());
            Comms.m.setY(m.getY());
            Comms.m.setZ(m.getZ());
//            System.out.println("B = "+B);
//            System.out.println("w = "+w);
//            System.out.println("m = "+m);

            try {
                FuzzyControl.connector.getPlatformServices().getAutonomousADCSService().setAllMagnetorquersDipoleMoments(m.getX(), m.getY(), m.getZ());
            } catch (NMFException | IOException | MALInteractionException | MALException ex) {
                Logger.getLogger(WheelsMomentumManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else{
            stopMagnetorquers();
        }
        counter++;
//        System.out.println("count = "+counter);
        if ( (counter>6000) || desaturationComplete()){
            FuzzyControl.desaturating = false;
            stopMagnetorquers();
            FuzzyControl.TIMER.stopTask(t);
        }
    }
    
    void updateWheelPerpendicular(){
//        double lenght = sqrt(FuzzyControl.magneticField.getX()*FuzzyControl.magneticField.getX()+
//                       FuzzyControl.magneticField.getY()*FuzzyControl.magneticField.getY()+
//                       FuzzyControl.magneticField.getZ()*FuzzyControl.magneticField.getZ());
//                
//        B.setX(FuzzyControl.magneticField.getX()/(float)lenght);
//        B.setY(FuzzyControl.magneticField.getY()/(float)lenght);
//        B.setZ(FuzzyControl.magneticField.getZ()/(float)lenght);
//
////         Desaturating 3 wheels        
//        w.setX(FuzzyControlAdapter.wheelsSpeed.get(0));
//        w.setY(FuzzyControlAdapter.wheelsSpeed.get(1));
//        w.setZ(FuzzyControlAdapter.wheelsSpeed.get(2));

        double lenght = sqrt(Comms.B.getX()*Comms.B.getX()+
                       Comms.B.getY()*Comms.B.getY()+
                       Comms.B.getZ()*Comms.B.getZ());                
        B.setX(Comms.B.getX()/(float)lenght);
        B.setY(Comms.B.getY()/(float)lenght);
        B.setZ(Comms.B.getZ()/(float)lenght);
        w.setX(Comms.wheelsSpeed.get(0));
        w.setY(Comms.wheelsSpeed.get(1));
        w.setZ(Comms.wheelsSpeed.get(2));

// Desaturating six wheels        
//        w.setX(FuzzyControl.adcsApi.Get_ReactionWheel_All_Speeds().getINTERNAL().getX()
//                +FuzzyControl.adcsApi.Get_ReactionWheel_All_Speeds().getEXTERNAL().getU());
//        w.setY(FuzzyControl.adcsApi.Get_ReactionWheel_All_Speeds().getINTERNAL().getY()
//                +FuzzyControl.adcsApi.Get_ReactionWheel_All_Speeds().getEXTERNAL().getV());
//        w.setZ(FuzzyControl.adcsApi.Get_ReactionWheel_All_Speeds().getINTERNAL().getZ()
//                +FuzzyControl.adcsApi.Get_ReactionWheel_All_Speeds().getEXTERNAL().getW());
        
       
        lenght = w.getX()*B.getX()+w.getY()*B.getY()+w.getZ()*B.getZ();
        
        wParallel.setX(B.getX()*(float)lenght);
        wParallel.setY(B.getY()*(float)lenght);
        wParallel.setZ(B.getZ()*(float)lenght);
                
        wPerpendicular.setX(w.getX()-wParallel.getX());
        wPerpendicular.setY(w.getY()-wParallel.getY());
        wPerpendicular.setZ(w.getZ()-wParallel.getZ());

        normWPerpendicular = sqrt(wPerpendicular.getX()*wPerpendicular.getX()+
                       wPerpendicular.getY()*wPerpendicular.getY()+
                       wPerpendicular.getZ()*wPerpendicular.getZ());
    }
    
    boolean desaturationComplete(){
        
// For 3 wheels
        for (int i=0;i<3;i++){
//          if (abs(FuzzyControlAdapter.wheelsSpeed.get(i))>0.15*FuzzyControl.WHEEL_MAX_SPEED){
//              return false;
//          }
          if (abs(Comms.wheelsSpeed.get(i))>0.15*FuzzyControl.WHEEL_MAX_SPEED){
              return false;
          }
      }
//        if (
//                abs(FuzzyControl.adcsApi.Get_ReactionWheel_All_Speeds().getINTERNAL().getX())>0.15*FuzzyControl.WHEEL_MAX_SPEED||
//                abs(FuzzyControl.adcsApi.Get_ReactionWheel_All_Speeds().getINTERNAL().getX())>0.15*FuzzyControl.WHEEL_MAX_SPEED||
//                abs(FuzzyControl.adcsApi.Get_ReactionWheel_All_Speeds().getINTERNAL().getX())>0.15*FuzzyControl.WHEEL_MAX_SPEED||
//                abs(FuzzyControl.adcsApi.Get_ReactionWheel_All_Speeds().getINTERNAL().getX())>0.15*FuzzyControl.WHEEL_MAX_SPEED||
//                abs(FuzzyControl.adcsApi.Get_ReactionWheel_All_Speeds().getINTERNAL().getX())>0.15*FuzzyControl.WHEEL_MAX_SPEED||
//                abs(FuzzyControl.adcsApi.Get_ReactionWheel_All_Speeds().getINTERNAL().getX())>0.15*FuzzyControl.WHEEL_MAX_SPEED){
//        return false;
//    }

        return true;
    }
    
    void stopMagnetorquers(){
        try {
                FuzzyControl.connector.getPlatformServices().getAutonomousADCSService().setAllMagnetorquersDipoleMoments(
                        0.0f, 0.0f, 0.0f);
            } catch (NMFException | IOException | MALInteractionException | MALException ex) {
                Logger.getLogger(WheelsMomentumManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            Comms.m.setX(0.0f);
            Comms.m.setY(0.0f);
            Comms.m.setZ(0.0f);
    }
    
}

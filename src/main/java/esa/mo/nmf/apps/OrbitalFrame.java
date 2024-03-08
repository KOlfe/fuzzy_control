/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esa.mo.nmf.apps;

import esa.mo.nmf.NMFException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.MALInteractionException;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.Quaternion;
import org.ccsds.moims.mo.platform.gps.body.GetLastKnownPositionResponse;
import org.hipparchus.geometry.euclidean.threed.Rotation;
import org.hipparchus.util.FastMath;
import org.orekit.attitudes.NadirPointing;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.Transform;
import org.orekit.propagation.analytical.tle.SGP4;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.Constants;

/**
 *
 * @author kolfe
 */
public class OrbitalFrame {
    
    static Float altitude;
    static Float latitude;
    static Float longitude;
    static double elapsed_time;
    static org.hipparchus.geometry.euclidean.threed.Vector3D nadir;
    static org.hipparchus.geometry.euclidean.threed.Vector3D sun;
    static org.hipparchus.geometry.euclidean.threed.Vector3D orekitSunVector;
    static org.hipparchus.geometry.euclidean.threed.Vector3D nmfTranformedSunVector;
    static org.ccsds.moims.mo.platform.structures.VectorF3D nmfSunVector;
    static final Frame inertialFrame = FramesFactory.getEME2000();
    static Frame TEMEFrame = FramesFactory.getTEME();
    static OneAxisEllipsoid earthShape = new OneAxisEllipsoid(
            Constants.WGS84_EARTH_EQUATORIAL_RADIUS, Constants.WGS84_EARTH_FLATTENING, inertialFrame);
    static TLE opsSatTLE;
    static private Quaternion orbitalAttitude = new Quaternion(
          Float.parseFloat("1.0"),
          Float.parseFloat("0.0"),
          Float.parseFloat("0.0"),
          Float.parseFloat("0.0")
  );
            
    static void getOrbitalFrame(){

//  NMF GPS geodetic coordinates
//  ****************************

//        try {
//            if (FuzzyControl.connector== null) {  // The framework is still not available
//                    return;
//            }
//            GetLastKnownPositionResponse pos = FuzzyControl.connector.getPlatformServices().getGPSService().getLastKnownPosition();
//            altitude = pos.getBodyElement0().getAltitude();
//            latitude = pos.getBodyElement0().getLatitude();
//            longitude = pos.getBodyElement0().getLongitude();
//            elapsed_time = pos.getBodyElement1().getValue();
//        } catch (NMFException | IOException | MALInteractionException | MALException ex) {
//            Logger.getLogger(OrbitalFrame.class.getName()).log(Level.SEVERE, null, ex);
//        }


// DATE
// ****

// Simulation date:
                AbsoluteDate OS_date = Comms.simDateTime;

// NMF GPS Data date (firs is the time the GPS acquire the data, if elapsed time is big, a propagation to current time will be needed);
        //AbsoluteDate OS_date = new AbsoluteDate(AbsoluteDate.JAVA_EPOCH, ((double)System.currentTimeMillis())/1000.0 - elapsed_time, TimeScalesFactory.getUTC());
//        AbsoluteDate OS_date = new AbsoluteDate(AbsoluteDate.JAVA_EPOCH, ((double)System.currentTimeMillis())/1000.0, TimeScalesFactory.getUTC());
//System.out.println("Date = "+OS_date);
////    
////        Calendar cal = Calendar.getInstance();
////        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH:mm:ss.SSS");
////        System.out.println("Java dice:   " + sdf.format(cal.getTime()) );
////        System.out.println("Orekit dice: " + OS_date);


//  NADIR
//  *****

//  Nadir from NMF GPS (without propagation)
//        GeodeticPoint opssatOnEarth = new GeodeticPoint(FastMath.toRadians(latitude), FastMath.toRadians(longitude), altitude);
////        System.out.println(opssatOnEarth.getNadir());
//        Frame earthFrame = FramesFactory.getITRF(IERSConventions.IERS_2010, true);
//        Transform earthToInertial = earthFrame.getTransformTo(inertialFrame, OS_date);
//        nadir = earthToInertial.transformVector(opssatOnEarth.getNadir());
//            System.out.println("orekit TLE line 1= "+opsSatTLE.getLine1());
//            System.out.println("orekit TLE line 2= "+opsSatTLE.getLine2());

//  Nadir from TLE propagation
        // SGP4 propagator = new SGP4(opsSatTLE, new NadirPointing(inertialFrame, earthShape), 5.777673);
        // nadir = propagator.propagate(OS_date).getOrbit().getPVCoordinates().getPosition().negate().normalize();
        // nadir = TEMEFrame.getTransformTo(inertialFrame, OS_date).transformPosition(nadir);
//            System.out.println("nadir: "+nadir);

// Nadir from simulation
        nadir = Comms.nadir;
        

        
//        System.out.println(nadir);
//        Quaternion inertialAttitude = FuzzyControlAdapter.getInertialAttitude();
//        System.out.println("inertial attitude = "+inertialAttitude.getA()
//                                                +inertialAttitude.getB()+
//                                                +inertialAttitude.getC()+
//                                                +inertialAttitude.getD());
        Quaternion inertialAttitude = Comms.inertialAttitude;
        Rotation bodyToInertialRotation = new Rotation(inertialAttitude.getA(),
                                        -inertialAttitude.getB(),
                                        -inertialAttitude.getC(),
                                        -inertialAttitude.getD(),false);
//        System.out.println("bodyToInertial: " + bodyToInertialRotation.getQ0()+
//                                                        bodyToInertialRotation.getQ1()+
//                                                        bodyToInertialRotation.getQ2()+
//                                                        bodyToInertialRotation.getQ3());
        
//        nmfTranformedSunVector = bodyToInertialRotation.applyTo(new org.hipparchus.geometry.euclidean.threed.Vector3D(
//                            nmfSunVector.getX(), nmfSunVector.getY(), nmfSunVector.getZ()));
        
//        orekitSunVector = CelestialBodyFactory.getSun().getPVCoordinates(OS_date.shiftedBy(elapsed_time), inertialFrame).getPosition().normalize();
//        System.out.println("getSun output = "+CelestialBodyFactory.getSun());
        orekitSunVector = CelestialBodyFactory.getSun().getPVCoordinates(OS_date, inertialFrame).getPosition().normalize();
        
//        System.out.println("NMF SunVector:    " + nmfSunVector);
//        System.out.println("Orekit SunVector: " + bodyToInertialRotation.applyTo(orekitSunVector));
       
        sun = orekitSunVector;
//        System.out.println("orekit sun = "+sun.toString());
        Rotation inertialToExperimentRotation = new Rotation(nadir, sun , 
                new org.hipparchus.geometry.euclidean.threed.Vector3D(0.0,0.0, -1.0),
                new org.hipparchus.geometry.euclidean.threed.Vector3D(-1.0,0.0,0.0));
        
//        System.out.println("Inertial to Experiment: " + inertialToExperimentRotation.getQ0()+
//                                                        inertialToExperimentRotation.getQ1()+
//                                                        inertialToExperimentRotation.getQ2()+
//                                                        inertialToExperimentRotation.getQ3());
//        System.out.println("sun:   " + inertialToExperimentRotation.applyTo(sun));
//        System.out.println("nadir: " + inertialToExperimentRotation.applyTo(nadir));
        Rotation bodyToExperiment = inertialToExperimentRotation.applyTo(bodyToInertialRotation);
//        System.out.println("bodyToExperiment = "+bodyToExperiment.getQ0()
//        +bodyToExperiment.getQ1()
//        +bodyToExperiment.getQ2()
//        +bodyToExperiment.getQ3());
        
        settOrbitalAttitude(new Float(bodyToExperiment.getQ0()),
                           new Float(-bodyToExperiment.getQ1()),
                           new Float(-bodyToExperiment.getQ2()),
                           new Float(-bodyToExperiment.getQ3()));
//        Transform inertialToExperiment = new Transform (OS_date, inertialToExperimentRotation ); 
        
        
        /*
        getSun() en spacecraft frame
        multiplicar por el cuaternión actitud para expresarlo en J2000
        Rotation (u1,u2,v1,v2) con los vectores nadir y sol (nadir {1,0,0} y Sol{0,0,1} en ejes experimento)
        Transform (date , rotation) con OS_date y Rotation
        Frame (parent, transform, name) con el J2000 como padre.
        Expresar el cuaternión actitud en el nuevo Frame.
        */
    }
    
      public static  Quaternion getOrbitalAttitude(){      
          return orbitalAttitude;
  }
  
  public static void settOrbitalAttitude( Float qa, Float qb, Float qc, Float qd ){
      orbitalAttitude.setA(qa);
      orbitalAttitude.setB(qb);
      orbitalAttitude.setC(qc);
      orbitalAttitude.setD(qd);      
  }
}

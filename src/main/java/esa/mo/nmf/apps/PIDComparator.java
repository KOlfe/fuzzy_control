/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esa.mo.nmf.apps;

import static java.lang.Math.signum;
import org.ccsds.moims.mo.platform.structures.VectorF3D;

/**
 *
 * @author kolfe
 */
public class PIDComparator {
    public static Float Kpx = 1.0f;
    public static Float Kdx = 0.0f;
    public static Float Kix = 0.0f;
								 
    public static Float Kpy = 1.0f;
    public static Float Kdy = 0.0f;
    public static Float Kiy = 0.0f;
								 
    public static Float Kpz = 1.0f;
    public static Float Kdz = 0.0f;
    public static Float Kiz = 0.0f;
    
    protected static VectorF3D error_integral = new VectorF3D(0.0f, 0.0f, 0.0f);
    
    static void updateErrorIntegral(){
        error_integral.setX(error_integral.getX()+FuzzyControl.error.getB()*FuzzyControl.REFRESH_RATE/1000);
        error_integral.setY(error_integral.getY()+FuzzyControl.error.getC()*FuzzyControl.REFRESH_RATE/1000);
        error_integral.setZ(error_integral.getZ()+FuzzyControl.error.getD()*FuzzyControl.REFRESH_RATE/1000);
        if (signum(FuzzyControl.error.getB())!=signum(FuzzyControl.previousError.getB())){
            error_integral.setX(0.0f);
        }
        if (signum(FuzzyControl.error.getC())!=signum(FuzzyControl.previousError.getC())){
            error_integral.setY(0.0f);
        }
        if (signum(FuzzyControl.error.getD())!=signum(FuzzyControl.previousError.getD())){
            error_integral.setZ(0.0f);
        }
    }
    
    static void computeActuation(){
//        VectorF3D local_error = new VectorF3D(); 
//        local_error.setX(FuzzyControl.error.getB());
//        local_error.setY(FuzzyControl.error.getC());
//        local_error.setZ(FuzzyControl.error.getD());
//        VectorF3D directionP = new VectorF3D();
//        VectorF3D directionD = new VectorF3D();
//        VectorF3D directionI = new VectorF3D();
//        directionP = Util.cross_product(Util.normalize(FuzzyControl.magneticField), local_error);
//        directionD = Util.cross_product(FuzzyControl.angularVelocity, Util.normalize(FuzzyControl.magneticField));
//        directionI = Util.cross_product(Util.normalize(FuzzyControl.magneticField), error_integral);
       FuzzyControl.actuation.setX(Kpx*FuzzyControl.error.getB()+Kdx*FuzzyControl.errorDerivative.getB()+Kix*error_integral.getX());
       FuzzyControl.actuation.setY(Kpy*FuzzyControl.error.getC()+Kdy*FuzzyControl.errorDerivative.getC()+Kiy*error_integral.getY());
       FuzzyControl.actuation.setZ(Kpz*FuzzyControl.error.getD()+Kdz*FuzzyControl.errorDerivative.getD()+Kiz*error_integral.getZ());
    
       FuzzyControl.actuation.setX(-FuzzyControl.actuation.getX());
       FuzzyControl.actuation.setY(-FuzzyControl.actuation.getY());
       FuzzyControl.actuation.setZ(-FuzzyControl.actuation.getZ());
    }
    
    static void resetErrorIntegral(){
        error_integral.setX((float)0.0);
        error_integral.setY((float)0.0);
        error_integral.setZ((float)0.0);    
    }
    
    public static void setPIDGains(Float Kpx, Float Kix, Float Kdx, Float Kpy, 
            Float Kiy, Float Kdy, Float Kpz, Float Kiz, Float Kdz){
        PIDComparator.Kpx = Kpx;
        PIDComparator.Kix = Kix;
        PIDComparator.Kdx = Kdx;
        PIDComparator.Kiy = Kiy;
        PIDComparator.Kdy = Kdy;
        PIDComparator.Kpy = Kpy;
        PIDComparator.Kpz = Kpz;
        PIDComparator.Kiz = Kiz;
        PIDComparator.Kdz = Kdz;
        
    }
}

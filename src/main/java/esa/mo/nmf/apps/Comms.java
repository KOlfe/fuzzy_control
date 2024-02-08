/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esa.mo.nmf.apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.mal.structures.FloatList;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.Quaternion;
import org.ccsds.moims.mo.platform.structures.VectorF3D;
import org.hipparchus.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author kolfe
 */
public class Comms {
    
final static String HOST_NAME = "localhost";
final static int PORT_NUMBER = 10001;
static PrintWriter out;
static BufferedReader in;
public static Quaternion inertialAttitude = new Quaternion();
public static FloatList wheelsSpeed = new FloatList(3);
public static VectorF3D m = new VectorF3D(0.0f, 0.0f, 0.0f);
public static VectorF3D B = new VectorF3D();

public static void initComms(){
    wheelsSpeed.add(0.0f);
    wheelsSpeed.add(0.0f);
    wheelsSpeed.add(0.0f);
    try {
    Socket echoSocket = new Socket(HOST_NAME, PORT_NUMBER);
    out = new PrintWriter(echoSocket.getOutputStream(), true);
    in = new BufferedReader(
            new InputStreamReader(echoSocket.getInputStream()));
    } catch (IOException ex) {
        Logger.getLogger(Comms.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public static void read42(){
    try {
        String line;
        line = in.readLine();
//        System.out.println(line);
        line = in.readLine();
//        String pos[] = line.split(" ");
//        Vector3D PosN = new Vector3D(Double.parseDouble(pos[2]), Double.parseDouble(pos[3]), Double.parseDouble(pos[4])); 
//        System.out.println("nadir 42: "+PosN.negate().normalize().toString());
        in.readLine();
        line = in.readLine();
//        System.out.println(line);
        in.readLine();
        in.readLine();
        line = in.readLine();
        String[] magneticField = line.split(" ");
        B.setX(Float.parseFloat(magneticField[2]));
        B.setY(Float.parseFloat(magneticField[3]));
        B.setZ(Float.parseFloat(magneticField[4]));
        line = in.readLine();
        String[] q = line.split(" ");
        inertialAttitude.setA(Float.parseFloat(q[5]));
        inertialAttitude.setB(Float.parseFloat(q[2]));
        inertialAttitude.setC(Float.parseFloat(q[3]));
        inertialAttitude.setD(Float.parseFloat(q[4]));
        line = in.readLine();
        String[] velocity = line.split(" ");
        wheelsSpeed.set(0, Float.parseFloat(velocity[2]));
        line = in.readLine();
        velocity = line.split(" ");
        wheelsSpeed.set(1, Float.parseFloat(velocity[2]));
        line = in.readLine();
        velocity = line.split(" ");
        wheelsSpeed.set(2, Float.parseFloat(velocity[2]));
        line = in.readLine();
//        System.out.println(line);
//        System.out.println(line.equals("[EOF]"));
        if (line.equals("[EOF]")){
            in.readLine();
            out.println("Ack");
        }
    } catch (IOException ex) {
        Logger.getLogger(Comms.class.getName()).log(Level.SEVERE, null, ex);
    }
}

static void write42(VectorF3D torque){
    try {
        //out.format("SC[0].AC.IdealTrq = %18.12e %18.12e %18.12e %n", torque.getX(), torque.getY(), torque.getZ());
        out.println("SC[0].AC.Whl[0].Tcmd = "+ torque.getX().toString());
        out.println("SC[0].AC.Whl[1].Tcmd = "+ torque.getY().toString());
        out.println("SC[0].AC.Whl[2].Tcmd = "+ torque.getZ().toString());
        out.println("SC[0].AC.MTB[0].Mcmd = "+ Comms.m.getX().toString());
        out.println("SC[0].AC.MTB[1].Mcmd = "+ Comms.m.getY().toString());
        out.println("SC[0].AC.MTB[2].Mcmd = "+ Comms.m.getZ().toString());
        out.println("[EOF]");
        String line = in.readLine();
//        System.out.println(line);
    } catch (IOException ex) {
        Logger.getLogger(Comms.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    
}

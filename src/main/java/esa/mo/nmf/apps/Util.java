/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esa.mo.nmf.apps;

import esa.mo.nmf.NMFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import static java.lang.Math.sqrt;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.platform.structures.VectorF3D;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.time.AbsoluteDate;

/**
 *
 * @author kolfe
 */
public class Util {
    
    static public String FileID;
    static public String MD5;
    
    public static String calculate_md5(String fileID) throws NoSuchAlgorithmException, FileNotFoundException, IOException{
        FileID=fileID;
        MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream is = new FileInputStream(fileID);
        byte[] buffer = new byte[1024];
        int nread;
        while ((nread = is.read(buffer)) != -1) {
            md.update(buffer, 0, nread);
        }
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        FuzzyControl.log(fileID+" "+result);
        MD5=result.toString();
        return result.toString();
    }  
    
    static VectorF3D cross_product(VectorF3D u, VectorF3D v){
        VectorF3D w = new VectorF3D();
        w.setX(u.getY()*v.getZ()-u.getZ()*v.getY());
	w.setY(-u.getX()*v.getZ()+u.getZ()*v.getX());
	w.setZ(u.getX()*v.getY()-u.getY()*v.getX());
        return w;
    }
    
    static VectorF3D normalize (VectorF3D v){
        VectorF3D u = new VectorF3D();
        double norm = v.getX()*v.getX() + 
                      v.getY()*v.getY() +
                      v.getZ()*v.getZ();
	norm=sqrt(norm);
        
        u.setX(v.getX()/(float)norm);
        u.setY(v.getY()/(float)norm);
        u.setZ(v.getZ()/(float)norm);
        
        return u;
    }
    
        static void deleteDirectory(File directory){
        String[]entries = directory.list();
        for(String s: entries){
            File currentFile = new File(directory.getPath(),s);
            currentFile.delete();
        }
        directory.delete();
    }
        
    static void extractFile(Path zipFile, String fileName, Path outputFile) throws IOException {
        // Wrap the file system in a try-with-resources statement
        // to auto-close it when finished and prevent a memory leak
        try (FileSystem fileSystem = FileSystems.newFileSystem(zipFile, null)) {
            Path fileToExtract = fileSystem.getPath(fileName);
            Files.copy(fileToExtract, outputFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }
    
    static void initOrekitFiles(){
        try {
            File orekitResourcesFileInitLocation = new File("/home/nmf/lib/orekit-resources-2.1.0-SNAPSHOT.jar");
            File orekitResourcesFileFinalLocation = new File(System.getProperty("user.dir")+"/orekit-resources-2.1.0-SNAPSHOT.jar");
            File orekitDataCompressedFile = new File(System.getProperty("user.dir")+"/orekit-data.zip");
            File orekitData = new File(System.getProperty("user.dir")+"/orekit-data");
            
            
            // if (orekitData.exists()){
            //     deleteDirectory(orekitData);
            // }
            if (!orekitData.exists()){
            orekitData.mkdirs();
            
            Files.copy(orekitResourcesFileInitLocation.toPath(), orekitResourcesFileFinalLocation.toPath(), StandardCopyOption.REPLACE_EXISTING);
            extractFile(orekitResourcesFileFinalLocation.toPath(), "orekit-data.zip",orekitDataCompressedFile.toPath());
            extractFile(orekitDataCompressedFile.toPath(), "orekit-data/DE-440-ephemerides/lnxp2021.440",
                    new File(orekitData,"lnxp2021.440").toPath());
            extractFile(orekitDataCompressedFile.toPath(), "orekit-data/UTC-TAI.history",
                    new File(orekitData,"UTC-TAI.history").toPath());

            Files.delete(new File(System.getProperty("user.dir")+"/orekit-data.zip").toPath());
            Files.delete(new File(System.getProperty("user.dir")+"/orekit-resources-2.1.0-SNAPSHOT.jar").toPath());
            }
            calculate_md5("orekit-data/lnxp2021.440");
            calculate_md5("orekit-data/UTC-TAI.history");
            
            DataProvidersManager manager = DataProvidersManager.getInstance();
            manager.addProvider(new DirectoryCrawler(orekitData));

        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static void writeTelemtry(){
          PrintWriter telemetryWriter = null;
        try {
            telemetryWriter = new PrintWriter(new FileWriter(FuzzyControl.TELEMETRY_FILE, true));
//            telemetryWriter.print(System.currentTimeMillis());
            // telemetryWriter.format("%13.8f", FuzzyControlAdapter.getInertialAttitude().getA());
            // telemetryWriter.format("%13.8f", FuzzyControlAdapter.getInertialAttitude().getB());
            // telemetryWriter.format("%13.8f", FuzzyControlAdapter.getInertialAttitude().getC());
            // telemetryWriter.format("%13.8f", FuzzyControlAdapter.getInertialAttitude().getD());
            // telemetryWriter.format("%13.8f", FuzzyControl.currentAttitude.getA());
            // telemetryWriter.format("%13.8f", FuzzyControl.currentAttitude.getB());
            // telemetryWriter.format("%13.8f", FuzzyControl.currentAttitude.getC());
            // telemetryWriter.format("%13.8f", FuzzyControl.currentAttitude.getD());
            // telemetryWriter.format("%16.7f", FuzzyControl.magneticField.getX());
            // telemetryWriter.format("%16.7f", FuzzyControl.magneticField.getY());
            // telemetryWriter.format("%16.7f", FuzzyControl.magneticField.getZ());
            // telemetryWriter.format("%16.7f", FuzzyControl.angularVelocity.getX());
            // telemetryWriter.format("%16.7f", FuzzyControl.angularVelocity.getY());
            // telemetryWriter.format("%16.7f", FuzzyControl.angularVelocity.getZ());
            // telemetryWriter.format("%16.7f", FuzzyControlAdapter.mtqDipoleMoment.getX());
            // telemetryWriter.format("%16.7f", FuzzyControlAdapter.mtqDipoleMoment.getY());
            // telemetryWriter.format("%16.7f", FuzzyControlAdapter.mtqDipoleMoment.getZ());
            // telemetryWriter.format("%16.7f", FuzzyControlAdapter.wheelsSpeed.get(0));
            // telemetryWriter.format("%16.7f", FuzzyControlAdapter.wheelsSpeed.get(1));
            // telemetryWriter.format("%16.7f", FuzzyControlAdapter.wheelsSpeed.get(2));
            // telemetryWriter.format("%16.7f", FuzzyControl.actuation.getX());
            // telemetryWriter.format("%16.7f", FuzzyControl.actuation.getY());
            // telemetryWriter.format("%16.7f", FuzzyControl.actuation.getZ());
            // telemetryWriter.format("%13.8f", FuzzyControl.error.getB());
            // telemetryWriter.format("%13.8f", FuzzyControl.error.getC());
            // telemetryWriter.format("%13.8f", FuzzyControl.error.getD());

            telemetryWriter.format(Locale.UK,"%13f", Comms.simDateTime.durationFrom(AbsoluteDate.JAVA_EPOCH));
            telemetryWriter.format(Locale.UK,"%13.8f", Comms.inertialAttitude.getA());
            telemetryWriter.format(Locale.UK,"%13.8f", Comms.inertialAttitude.getB());
            telemetryWriter.format(Locale.UK,"%13.8f", Comms.inertialAttitude.getC());
            telemetryWriter.format(Locale.UK,"%13.8f", Comms.inertialAttitude.getD());
            telemetryWriter.format(Locale.UK,"%13.8f", FuzzyControl.currentAttitude.getA());
            telemetryWriter.format(Locale.UK,"%13.8f", FuzzyControl.currentAttitude.getB());
            telemetryWriter.format(Locale.UK,"%13.8f", FuzzyControl.currentAttitude.getC());
            telemetryWriter.format(Locale.UK,"%13.8f", FuzzyControl.currentAttitude.getD());
            telemetryWriter.format(Locale.UK,"%16.7f", Comms.B.getX());
            telemetryWriter.format(Locale.UK,"%16.7f", Comms.B.getY());
            telemetryWriter.format(Locale.UK,"%16.7f", Comms.B.getZ());
            telemetryWriter.format(Locale.UK,"%16.7f", FuzzyControl.angularVelocity.getX());
            telemetryWriter.format(Locale.UK,"%16.7f", FuzzyControl.angularVelocity.getY());
            telemetryWriter.format(Locale.UK,"%16.7f", FuzzyControl.angularVelocity.getZ());
            telemetryWriter.format(Locale.UK,"%16.7f", Comms.m.getX());
            telemetryWriter.format(Locale.UK,"%16.7f", Comms.m.getY());
            telemetryWriter.format(Locale.UK,"%16.7f", Comms.m.getZ());
            telemetryWriter.format(Locale.UK,"%16.7f", Comms.wheelsSpeed.get(0));
            telemetryWriter.format(Locale.UK,"%16.7f", Comms.wheelsSpeed.get(1));
            telemetryWriter.format(Locale.UK,"%16.7f", Comms.wheelsSpeed.get(2));
            telemetryWriter.format(Locale.UK,"%16.7f", FuzzyControl.actuation.getX());
            telemetryWriter.format(Locale.UK,"%16.7f", FuzzyControl.actuation.getY());
            telemetryWriter.format(Locale.UK,"%16.7f", FuzzyControl.actuation.getZ());
            telemetryWriter.format(Locale.UK,"%13.8f", FuzzyControl.error.getB());
            telemetryWriter.format(Locale.UK,"%13.8f", FuzzyControl.error.getC());
            telemetryWriter.format(Locale.UK,"%13.8f", FuzzyControl.error.getD());
            telemetryWriter.println();
            telemetryWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            telemetryWriter.close();
        }
            
    }
            
}

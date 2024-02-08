/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esa.mo.nmf.apps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kolfe
 */
public class FCLEditor {
    static void fclEditor(String fileID, String action, int linenumber, String newString){
        String newContent;
        File fileToBeModified = new File(fileID); 
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileToBeModified));
            switch(action){
            case "append":
                newContent = append(reader,linenumber, newString);
                reader.close();
                writeContent(newContent, fileToBeModified);
                break;
            case "replace":
                newContent = replace(reader,linenumber, newString);
                reader.close();
                writeContent(newContent, fileToBeModified);
                break;
            case "delete":
                newContent = delete(reader,linenumber);
                reader.close();
                writeContent(newContent, fileToBeModified);
                break;
            }
        }
        catch (FileNotFoundException  ex ) {
            Logger.getLogger(FCLEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex){
            Logger.getLogger(FCLEditor.class.getName()).log(Level.SEVERE, null, ex);          
        }
        
        
    }

    private static String append(BufferedReader reader, int line_number, String newString) throws IOException {
        String content  = "";
        int line_counter=0;
        String line = reader.readLine();
        while( line != null){
            line_counter++;
            if (line_counter==line_number){
                content = content + line + System.lineSeparator()+ newString + System.lineSeparator();
            }
            else{
                content = content + line + System.lineSeparator();
            }
                line = reader.readLine();   
        }
        return content;
    }
    
    private static String replace(BufferedReader reader, int line_number, String newString) throws IOException {
        String content = "";
        int line_counter=0;
        String line = reader.readLine();
        while( line != null){
            line_counter++;
            if (line_counter == line_number){
                content = content +  newString + System.lineSeparator();
            }
            else{
                content = content + line + System.lineSeparator();
            }
                line = reader.readLine();   
        }
        return content;
    }
    private static String delete(BufferedReader reader, int line_number) throws IOException {
        String content = "";
        int line_counter=0;
        String line = reader.readLine();
        while( line != null){
            line_counter++;
            if (line_counter != line_number){
                content = content + line + System.lineSeparator();
            }
            line = reader.readLine();   
        }
        return content;
    }
    
    private static void writeContent(String content, File fileToBeModified){
        FileWriter writer;
        try {
            writer = new FileWriter(fileToBeModified);
            writer.write(content);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(FCLEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

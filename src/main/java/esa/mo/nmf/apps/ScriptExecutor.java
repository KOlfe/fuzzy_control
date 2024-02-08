/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esa.mo.nmf.apps;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author kolfe
 */
public class ScriptExecutor extends Thread{
    String script;
    ScriptExecutor(String script){
        this.script = script;
    }
    @Override
    public void run(){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        try {
            engine.eval(new java.io.FileReader(script));
        } catch (FileNotFoundException ex){
            Logger.getLogger(ScriptExecutor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ScriptException ex){
            Logger.getLogger(ScriptExecutor.class.getName()).log(Level.SEVERE, "Script stopped", ex);
        }
    }
    
}

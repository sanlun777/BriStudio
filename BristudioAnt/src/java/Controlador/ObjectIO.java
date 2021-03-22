/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
 */
public class ObjectIO {
    public boolean exists(String nombre){
        boolean exists;
        try{
            FileInputStream fileInput = new FileInputStream(new File(nombre));
            exists = true;
        }
        catch(Exception ex){
            exists = false;
        }
        
        return exists;
    }
    public Object lee(String nombre){
        Object objetoLeido = null;
        try{
        FileInputStream fileInput = new FileInputStream(new File(nombre));
        ObjectInputStream objectInput = new ObjectInputStream(fileInput);
        objetoLeido = objectInput.read();
        objectInput.close();
        fileInput.close();
        }
        catch(Exception ex){
            
        }
        return objetoLeido;
    }
    public void escribe(Object escribe, String nombre) throws FileNotFoundException, IOException{
        FileOutputStream fileOutput = new FileOutputStream(new File(nombre));
        ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
        objectOutput.writeObject(escribe);
        objectOutput.close();
        fileOutput.close();
    }
}

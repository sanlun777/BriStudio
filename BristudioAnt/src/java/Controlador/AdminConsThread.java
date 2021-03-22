/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import FiltradoDatos.SubidorSQL;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
 */
public class AdminConsThread implements Runnable{

    @Override
    public void run() {
        System.out.println("Inicializando server socket");
        try { 
            final ServerSocket ServerSoc = new ServerSocket(6422);
            while(true){
                try{
                final Socket client = ServerSoc.accept();
                Runnable hiloMethod = new Runnable(){
                    @Override
                    public void run() {
                        String instruccion;
                        ServerSocket soc;
                        try {
                            ObjectInputStream ins = new ObjectInputStream(client.getInputStream());
                            boolean continua = true;
                            while (continua){
                            Object insCheck = ins.readObject();
                            if(insCheck != null){
                            instruccion = AES256.decrypt((String) insCheck.toString(),"M0l3s0ftsuckw3r3");
                            System.out.println(instruccion);
                            String insFirstArg;
                            if(instruccion.contains(" ")){
                                insFirstArg = instruccion.subSequence(0, instruccion.indexOf(" ")).toString();
                            }
                            else{
                                insFirstArg = instruccion;
                            }
                            switch(instruccion){
                                case "apagar":
                                    System.exit(0);
                                break;
                                case "borrar":
                                    SubidorSQL subidor = new SubidorSQL();
                                    subidor.truncTabla(instruccion.subSequence(instruccion.indexOf(" "),instruccion.length()).toString());
                                    subidor.cerrarConexion();
                                break;
                                case "exit":
                                    client.close();
                                    continua = false;
                                break;
                                case "usuario":
                                    SubidorSQL subidore = new SubidorSQL();
                                    String crea = instruccion.subSequence(instruccion.indexOf(" "),instruccion.length()).toString();
                                    subidore.seguroSQL(new Object[]{crea,"1","20",crea+"@admin.com",crea,crea,crea,crea},new String[]{"usuario_nick","sexo","edad","correo","nombre","apellido_p","apellido_m","contrasena"},"usuarios");
                                    subidore.cerrarConexion();
                                break;
                            }
                            }
                            else{
                                client.close();
                                continua = false;
                            }
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(AdminConsole.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(AdminConsole.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(AdminConsThread.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println("SQLException");
                        }
                    }
                    
                };
                
                Thread hiloMet = new Thread(hiloMethod);
                hiloMet.start();
                
            }
                catch(IOException ex){
                    System.out.println("No se pudo conectar por un ciclo");
                }
            }
            
        } catch (IOException ex) {
            System.out.println("Falla al iniciar serverSocket");
            Logger.getLogger(AdminConsole.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

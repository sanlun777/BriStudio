/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiltradoDatos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
 */
public class Checador {
    public enum datosTipo{
        NUMERO,
        BOOLEAN,
        LETRA,
        EMAIL,
        FECHA,
        IGNORE
    }
    
    datosTipo[] tipos;
    int[][] tamanos;
    Object[] datos;
    Object max = 5;
    public boolean checa(datosTipo[] tipos,int[][] tamanos ,Object[] datos){
        this.tipos = tipos;
        this.tamanos = tamanos;
        this.datos = datos;
        int tamano = tipos.length;
        boolean correcto = true;
        for(int i = 0;i<tamano;i++){
            if(tipos[i] != datosTipo.IGNORE){
                if(datos[i] == null){
                    correcto = false;
                    break;
                }
                else{
                    String datoAnaliza = datos[i].toString();
                    switch(tipos[i]){
                        case NUMERO:
                                if(checaLength(i,datoAnaliza)){
                                    try{
                                        long lon = Long.parseLong(datoAnaliza);
                                    }
                                    catch(NumberFormatException nfe){
                                        correcto = false;
                                        System.out.println("Problema de numero en" + datoAnaliza);
                                    }
                                }
                                else{
                                    correcto = false;
                                }
                        break;

                        case LETRA:
                                if(checaLength(i,datoAnaliza)){

                                }
                                else{
                                    correcto = false;
                                    System.out.println("Problema de letra en" + datoAnaliza);
                                }
                        break;
                        case EMAIL:
                                int cont = 0;
                                int arrobaPos = 0;
                                int leng = datoAnaliza.length();
                                for(int j = 0;j<leng;j++){
                                    if(datoAnaliza.charAt(j)=='@'){
                                        cont++;
                                        arrobaPos = j;
                                    }
                                }
                                if ((cont == 1) && (arrobaPos < leng-1)){
                                    if(!checaLength(i,datoAnaliza)){
                                        correcto = false;
                                        System.out.println("Problema de email arroba en" + datoAnaliza);
                                    }
                                    else{
                                        int contDot = 0;
                                        //List<Integer> posDots = new LinkedList<Integer>();
                                        int prevPos = 0;
                                        boolean prevRun = false;
                                        if(datoAnaliza.charAt(arrobaPos+1)=='.'){
                                            correcto = false;
                                            System.out.println("Problema de email punto en" + datoAnaliza);
                                        }
                                        else{
                                            for(int j = arrobaPos+2; j<leng; j++){
                                                if(datoAnaliza.charAt(j)=='.'){
                                                    contDot++;
                                                    if(j == prevPos+1){
                                                        correcto = false;
                                                    }
                                                    else{
                                                        prevPos = j;
                                                    }
                                                    //posDots.add(j);
                                                }
                                            }
                                            if(contDot == 0){
                                                correcto = false;
                                            }
                                        }
                                    }
                                }
                                else{
                                    correcto = false;
                                }
                        break;

                        case BOOLEAN:
                            if(datoAnaliza.length()!= 1){
                                correcto = false;
                                System.out.println("Problema de boolean longitud en" + datoAnaliza);
                            }
                            else{
                                if(datoAnaliza.charAt(0) == '0' || datoAnaliza.charAt(0) == '1'){
                                }
                                else {
                                    correcto = false;
                                    System.out.println("Problema de boolean numero en" + datoAnaliza);
                                }
                            }
                        break;
                        
                        case FECHA:
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                            dateFormat.setLenient(false);
                            try{
                                dateFormat.parse(datoAnaliza);
                            }
                            catch(ParseException pe){
                                correcto = false;
                                System.out.println("Error at fecha check");
                            }
                        break;
                        
                        case IGNORE: 
                        break;

                    }
                }
            }
            if(!correcto) break;
        }
        return correcto;
    }
    boolean checaLength(int i, String analiza){
        boolean correcto = false;
        if((analiza.length() >= tamanos[i][0] && analiza.length() <= tamanos[i][1]) || (tamanos[i][0] == -1)){
            correcto = true;
        }
        if (!correcto) System.out.println("Problema de tamano en" + analiza);
        return correcto;
    }
}

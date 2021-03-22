/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiltradoDatos;

/**
 *
 * @author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
 */
public class AntiInjection {
    public boolean isSafe(String revisar){
        boolean injection = false;
        char[] forbidChars = {'*','=','"'};
        String[] forbidStrings = {"select","inner","join","delete","update"};
        for(int i = 0; i<revisar.length(); i++){
            for(int j = 0; j<forbidChars.length;j++){
                if(revisar.charAt(i) == forbidChars[j]){
                    injection = true;
                    break;
                }
            }
        }
        if(!injection){
                for(int i = 0; i<revisar.length();i++){
                    if(!injection){
                        for(int j = 0; j<forbidStrings.length;j++){
                            if(Character.toLowerCase(revisar.charAt(i)) == forbidStrings[j].charAt(0)){
                                int k = i + 1;
                                int l;
                                for (l = 1;l<forbidStrings[j].length();l++){
                                    if(k<revisar.length()){
                                        if(Character.toLowerCase(revisar.charAt(k)) == forbidStrings[j].charAt(l)){
                                            k++;
                                        }
                                        else{
                                            break;
                                        }
                                    }
                                    else{
                                        
                                        break;
                                    }
                                }
                                if(l == forbidStrings[j].length()){
                                    injection = true;
                                    break;
                                }
                            }
                        }
                    }
                    else{
                        break;
                    }
                }
        }
        return !injection;
    }
    public boolean safe (Object obj){
        boolean isSafe;
        if(!(obj == null)){
            String checa = obj.toString();
            if(isSafe(checa)){
                isSafe=true;
            }
            else{
                isSafe=false;
            }
        }
        else{
            isSafe=false;
        }
        return isSafe;
    }
    public boolean safeArray (Object[] obj){
        boolean isSafe = true;
        for(int i = 0;i<obj.length;i++){
            if(!safe(obj[i])){
                isSafe = false;
                break;
            }
        }
        return isSafe;
    }
}

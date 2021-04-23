/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiltradoDatos;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
 */
public class Permisos {
    public enum permiso{
        CREAUSUARIO,
        PERMITECREAUSUARIO,
        AUDITORIA,
        CREACURSOS,
        CREACONTENIDO,
        ADMINISTRADORCURSO,
        FAQ
    }
    
    private permiso[] permisos;
        
    
    public Permisos(String[] permiArray){
        System.out.println(Arrays.toString(permiArray));
        permisos = new permiso[permiArray.length];
        for(int i = 0; i < permisos.length; i++){
            permisos[i] = toPermiso(permiArray[i]);
        }
        System.out.println(Arrays.toString(permisos));
    }
    public permiso toPermiso(String permi){
        permiso permisoEnum = null;
        switch(permi){
            case("crea_usuario"):
                permisoEnum = permiso.CREAUSUARIO;
            break;
            case("auditoria"):
                permisoEnum = permiso.AUDITORIA;
            break;
            case("permite_crea_usuario"):
                permisoEnum = permiso.PERMITECREAUSUARIO;
            break;
            case("crea_cursos"):
                permisoEnum = permiso.CREACURSOS;
            break;
            case("crea_contenido"):
                permisoEnum = permiso.CREACONTENIDO;
            break;
            case("administrador_curso"):
                permisoEnum = permiso.ADMINISTRADORCURSO;
            break;
            case("administrador_faq"):
                permisoEnum = permiso.FAQ;
            break;
            default:
                if(isNumeric(permi)){
                    permisoEnum = toPermiso(Integer.parseInt(permi));
                }
            break;
        }
        return permisoEnum;
    }
    
    public permiso toPermiso(int permi){
        SubidorSQL buscaPermi = new SubidorSQL();
        permiso permis = null;
        try {
            permis = toPermiso(buscaPermi.busqSeparate("permisos_id","permiso","permisos",Integer.toString(permi)));
            buscaPermi.cerrarConexion();
        } catch (SQLException ex) {
            System.out.println("Error, ID no encontrado");
        }
        return permis;
    }
    
    public Boolean isNumeric(String number){
        return number.matches("-?\\d+(\\.\\d+)?");
    }
    
    public String toString(permiso permi){
        permiso permiEnum = permi;
        String permisoStr = null;
        switch(permiEnum){
            case CREAUSUARIO:
                permisoStr = "crea_usuario";
            break;
            case AUDITORIA:
                permisoStr = "auditoria";
            break;
            case PERMITECREAUSUARIO:
                permisoStr = "permite_crea_usuario";
            break;
            case CREACURSOS:
                permisoStr = "crea_cursos";
            break;
            case CREACONTENIDO:
                permisoStr = "crea_contenido";
            break;
            case ADMINISTRADORCURSO:
                permisoStr = "administrador_curso";
            break;
            case FAQ:
                permisoStr = "administrador_faq";
            break;    
        }
        return permisoStr;
    }
    
    public String toID(permiso permi){
        SubidorSQL buscaPermi = new SubidorSQL();
        String id = null;
        try {
            id = buscaPermi.busqSeparate("permiso","permisos_id","permisos",toString(permi));
            buscaPermi.cerrarConexion();
        } catch (SQLException ex) {
            System.out.println("Error, ID no encontrado");
        }
        return id;    
    }
    
    public boolean exists(permiso checa){
        boolean existe = false;
        for(permiso checando:permisos){
            if(checando == checa){
                existe = true;
                break;
            }
        }
        return existe;
    }
    
    public boolean[] boolPerms(permiso permi, permiso[] busca){
        boolean[] existe = new boolean[busca.length];
        for(int i = 0; i<busca.length;i++){
            if(busca[i] == permi){
                existe[i] = true;
                break;
            }
        }
        return existe;
    }
    public boolean permisosRequeridos(permiso otorgar){
        boolean autorizado = false;
        permiso permisoReq = null;
        switch(otorgar){
            case CREAUSUARIO:
                permisoReq = permiso.PERMITECREAUSUARIO;
            break;
            case CREACURSOS:
                permisoReq = permiso.AUDITORIA;
            break;
            case PERMITECREAUSUARIO:
                permisoReq = permiso.AUDITORIA;
            break;
            case AUDITORIA:
                permisoReq = permiso.AUDITORIA;
            break;
            case FAQ:
                permisoReq = permiso.AUDITORIA;
            break;
        }
        if(exists(permisoReq)){
            autorizado = true;
        }
        return autorizado;
    }
    
    public boolean permisosRequeridos(String otorgar){
        return permisosRequeridos(toPermiso(otorgar));
    }
    
    public boolean permisoAdd(permiso permi, String usuario_id, String curso_id){
        SubidorSQL subePermiso = new SubidorSQL();
        if(subePermiso.seguroSQL(new Object[]{curso_id,usuario_id,toID(permi)},"usuario_curso")){
            try {
                subePermiso.cerrarConexion();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return true;
        }
        return false;
    }
    
    public boolean permisoDelete(int permi, String usuario_id, String curso_id){
        boolean success = false;
        SubidorSQL subePermiso = new SubidorSQL();
        if(subePermiso.eliminarMultiParams(new String[]{usuario_id, curso_id, Integer.toString(permi)},new String[]{"usuario_id","curso_id","permiso_id"},"permiso_usuario")){
            success = true;
        }
        try {
            subePermiso.cerrarConexion();
        } catch (SQLException ex) {
            Logger.getLogger(Permisos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
    
    public permiso[] toPermisoArray(String[] permisos){
        permiso[] permi = new permiso[permisos.length];
        for(int i = 0; i<permi.length; i++){
            permi[i] = toPermiso(permisos[i]);
        }
        return permi;
    }
    
}

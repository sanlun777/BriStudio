/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiltradoDatos;

import Controlador.ConfigDB;
import Controlador.ObjectIO;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
 */
public class SubidorSQL {
    int puerto = 3306;
    String bd;
    String user;
    String pass;
    private Connection con;
    private Statement st;
    ObjectIO configuracion;
    String[] forbiddenTables;

    /**
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public SubidorSQL(){
        try{
        this.configuracion = new ObjectIO();
        this.forbiddenTables = new String[]{"permisos"};
        ConfigDB config = null;
        if(configuracion.exists("configDB.dat")){
            config = (ConfigDB)configuracion.lee("configDB.dat");
            System.out.println("leyendo objectIO");
        }
        else{
            config = new ConfigDB("bristudio","root","n0m3l0");
            System.out.println("creando nuevo archivo");
        }
        this.bd = config.getBd();
        this.user = config.getUser();
        this.pass = config.getPass();
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+bd+"?autoReconnect=true&useSSL=false",user,pass);
        st = con.createStatement();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public boolean seguroSQL(Object[] datos,String[] pos, String tabla){
        boolean sucess = true;
        if(new AntiInjection().safeArray(datos)){
            
            try {
                subirSQL(datos,pos,tabla);
            } catch (ClassNotFoundException | SQLException ex) {
                sucess = false;
            }
        }
        else{
            sucess = false;
        }
        return sucess;
    }
    public boolean seguroSQL(Object[] datos,String tabla){
        boolean sucess = true;
        if(new AntiInjection().safeArray(datos)){
            try {
                subirSQL(datos,tabla);
            } catch (ClassNotFoundException | SQLException ex) {
                sucess = false;
            }
        }
        else{
            sucess = false;
        }
        return sucess;
    }
    
    public boolean seguroSQLValidado(Object[] datos,Checador.datosTipo[] tipos,int[][] tamanos,String tabla){
        boolean sucess = true;
        if(new AntiInjection().safeArray(datos)){
            System.out.println("Pasa antiInjection at seguroSQLValidado");
            if(new Checador().checa(tipos, tamanos, datos)){
                try {
                    subirSQL(datos,tabla);
                } catch (ClassNotFoundException | SQLException ex) {
                    sucess = false;
                }
            }
            else{
                sucess = false;
            }
        }
        else{
            sucess = false;
        }
        return sucess;
    }
    public boolean seguroSQLValidado(Object[] datos,String[] pos,Checador.datosTipo[] tipos,int[][] tamanos,String tabla){
        boolean sucess = true;
        if(new AntiInjection().safeArray(datos)){
            System.out.println("pasa antiinjection");
            if(new Checador().checa(tipos, tamanos, datos)){
                System.out.println("pasa chequeo");
                try {
                    subirSQL(datos,pos,tabla);
                } catch (ClassNotFoundException | SQLException ex) {
                    sucess = false;
                    ex.printStackTrace();
                }
            }
            else{
                sucess = false;
            }
        }
        else{
            sucess = false;
        }
        return sucess;
    }
    
    public void cerrarConexion() throws SQLException{
        con.close();
    }
    public boolean inicioSesionSeguro(Object user,Object pass,String columnuser,String columnpass,String table){
        Object[] userpass = {user,pass};
        Boolean inicio = false;
        if(new AntiInjection().safeArray(userpass)){
            ResultSet sesion;
            try {
                sesion = st.executeQuery("select*from " + table + " where " + columnuser + " = '" + user.toString() + "' AND " + columnpass + " = '" + pass.toString() + "'");
                inicio = sesion.next();
                sesion.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
        return inicio;
    }
    public boolean existe(String buscar,String tabla,String column) throws SQLException{
        AntiInjection sql = new AntiInjection();
        boolean exis;
        if(sql.isSafe(buscar)){
            ResultSet existir = st.executeQuery("select*from "+ tabla +" where "+column+" = '"+buscar+"'");
            exis = existir.next();
            existir.close();
        }
        else{
            exis = false;
        }
        return exis;
    }
    public String[] columnSet(String column,String table) throws SQLException{
        String[] columnSet;
        ArrayList<String> columner = new ArrayList<String>();
        ResultSet sql = st.executeQuery("select "+column+" from "+table);
        while(sql.next()){
            columner.add(sql.getString(column));
        }
        columnSet = new String[columner.size()];
        for(int i = 0;i<columnSet.length;i++){
            columnSet[i] = columner.get(i);
        }
        sql.close();
        return columnSet;
    }
    public String busqSeparate(String columnBusca,String columnSeleccion,String tabla,String busca) throws SQLException{
        AntiInjection sqlant = new AntiInjection();
        String resultado;
        if(sqlant.isSafe(busca)){
            ResultSet sql = st.executeQuery("select "+columnSeleccion+" from "+tabla+" where " + columnBusca + " = '" + busca +"'");
            System.out.println("select "+columnSeleccion+" from "+tabla+" where " + columnBusca + " = '" + busca +"'" + "at busqSeparate");
            if(sql.next()){
                resultado = sql.getString(columnSeleccion);
            }
            else{
                resultado = null;
            }
            sql.close();
        }
        else{
            resultado = null;     
        }
        return resultado;
    }
    
    public String[] busqSeparateStringArr(String columnBusca,String columnSeleccion,String tabla,String busca) throws SQLException{
        AntiInjection sqlant = new AntiInjection();
        String[] resultados = null;
        String resultado;
        if(sqlant.isSafe(busca)){
            ResultSet sql = st.executeQuery("select "+columnSeleccion+" from "+tabla+" where " + columnBusca + " = '" + busca +"'");
            System.out.println("select "+columnSeleccion+" from "+tabla+" where " + columnBusca + " = '" + busca +"'" + "at busqSeparate");
            CachedRowSet CRS = RowSetProvider.newFactory().createCachedRowSet();
            CRS.populate(sql);
            resultados = CRStoStringArray(CRS, columnSeleccion);
            sql.close();
        }
        else{
            resultado = null;     
        }
        return resultados;
    }
    
    public String[] busqSeparateStringArr(String[] columnBusca,String[] busca,String columnSeleccion,String tabla) throws SQLException{
        AntiInjection sqlant = new AntiInjection();
        String[] resultados = null;
        String resultado;
        boolean seguro = true;
        for(String buscaSafe:busca){
            if(!sqlant.isSafe(buscaSafe)){
                seguro = false;
                break;
            }
        }
        if(seguro){
            StringBuilder query = new StringBuilder();
            query.append("select "+columnSeleccion+" from "+tabla+" where " + columnBusca[0] + " = '" + busca[0] +"'");
            for(int i = 1; i<columnBusca.length; i++){
                query.append(" and "+columnBusca[i]+" = '"+busca[i]+"'");
            }
            ResultSet sql = st.executeQuery(query.toString());
            System.out.println(query.toString());
            CachedRowSet CRS = RowSetProvider.newFactory().createCachedRowSet();
            CRS.populate(sql);
            resultados = CRStoStringArray(CRS, columnSeleccion);
            sql.close();
        }
        else{
            resultado = null;     
        }
        return resultados;
    }
    public int size(String columnIndex,String tabla) throws SQLException{
        int ind;
        ResultSet index = st.executeQuery("select " + columnIndex + " from " + tabla + " order by " + columnIndex + " desc limit 1");
        if(index.next()){
            ind = Integer.parseInt(index.getString(columnIndex));
        }
        else{
            ind = 0;
        }
        index.close();
        return ind;
    }
    public CachedRowSet getTable(String table) throws SQLException{
        ResultSet sql = st.executeQuery("select*from " + table);
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        crs.populate(sql);
        sql.close();
        return crs;
    }
    public boolean updateField (String columnSearch, String columnUpdate, String field, String buscar, String table){
        boolean exito = true;
        try {
            st.executeUpdate("update " + table + " set " + columnUpdate + " = '" + field + "' where " + columnSearch + " = '" + buscar + "'");
        } catch (SQLException ex) {
            exito = false;
            System.out.println(ex);
        }
        return exito;
    }
    public CachedRowSet getRow(String buscar, String tabla, String searchField) throws SQLException{
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try {
            ResultSet row = st.executeQuery("select * from " + tabla + " where " + searchField + " = '" + buscar + "'");
            System.out.println("select * from " + tabla + " where " + searchField + " = '" + buscar + "'" + "en rowset");
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(row);
            row.close();
            
        } catch (SQLException ex) {
            System.out.println("revento el rowsettero");
            Logger.getLogger(SubidorSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return crs;
    }
    
    public void eliminarCuenta(String usuario, String column, String table) throws SQLException{
        AntiInjection anti = new AntiInjection();
        st.executeUpdate("delete from " + table + " where " + column + " = '" + usuario + "'");
    }
    
    public boolean eliminarMultiParams(String[] buscar, String[] columns, String table){
        boolean success = true;
        AntiInjection anti = new AntiInjection();
        String query = "delete from " + table + " where " + columns[0] + " = '" + buscar[0] + "'";
        for(int i = 1; i<buscar.length; i++){
            query += " AND " + columns[i] + " = '" + buscar[i] + "'";
        }
        try {
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(SubidorSQL.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }
    
    public CachedRowSet relatedRow(String table, String field, String buscar) throws SQLException{
        ResultSet sql = st.executeQuery("select*from " + table + " where " + field + " = '" + buscar + "'");
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        crs.populate(sql);
        sql.close();
        return crs;
    }
    public boolean insertNull(String columnSearch, String columnUpdate, String buscar, String table){
        boolean exito = true;
        try {
            st.executeUpdate("update " + table + " set " + columnUpdate + " = null where " + columnSearch + " = '" + buscar + "'");
        } catch (SQLException ex) {
            exito = false;
            System.out.println(ex);
        }
        return exito;
    }
    public int relatedRowSize(String table, String target, String buscar) throws SQLException{
        int ind = 0;
        ResultSet sql = st.executeQuery("select*from " + table + " where " + target+ " = '" + buscar + "'");
        while(sql.next()){
            ind++;
        }
        sql.close();
        return ind;
    }
    public CachedRowSet innerJoiner(String[] table, String[] linker,String target,String buscar) throws SQLException{
        StringBuilder request = new StringBuilder();
        request.append("select "+linker[0]+" from "+ table[0]);
        for(int i = 1; i< table.length; i++){
            request.append(" inner join " + table[i] + " using (" + linker[i] + ")");
        }
        request.append(" where " + table[table.length-1] + "." + target + " = '" + buscar + "';");
        System.out.println(request.toString());
        ResultSet sql = st.executeQuery(request.toString());
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        crs.populate(sql);
        sql.close();
        return crs;
    }
    
    public CachedRowSet innerJoinerMultiArg(String[] table, String[] linker, String[] searchTable, String[] searchArg, String[] search) throws SQLException{
        StringBuilder request = new StringBuilder();
        request.append("select*from "+ table[0]);
        for(int i = 1; i< table.length; i++){
            request.append(" inner join " + table[i] + " using (" + linker[i] + ")");
        }
        
        request.append(" where " + searchTable[0] + "." + searchArg[0] + " = '" + search[0] + "'");
        System.out.println(request.toString());
        for(int i = 1; i<searchTable.length; i++){
            request.append(" and " + searchTable[i] + "." + searchArg[i] + " = '" + search[i] + "'");
        }
        System.out.println(request.toString());
        ResultSet sql = st.executeQuery(request.toString());
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        crs.populate(sql);
        sql.close();
        return crs;
    }
    
    
    
    public String[] CRStoStringArray(CachedRowSet rowset, String columna) throws SQLException{
        List<String> lista = new LinkedList<String>();
        String[] arr;
        while(rowset.next()){
            lista.add(rowset.getString(columna));
        }
       rowset.close();
       arr = new String[lista.size()];
       arr = lista.toArray(arr);
       System.out.println(Arrays.toString(arr));
       return arr;
    }
    
    public String[][] CRStoStringArrs(CachedRowSet rowset, String[] columna) throws SQLException{
        System.out.println(Arrays.toString(columna));
        List<String>[] lista = new LinkedList[columna.length];
        String[][] arr = new String[columna.length][];
        for(int i = 0; i < lista.length; i++){
            lista[i] = new LinkedList();
        }
        while(rowset.next()){
            for(int i = 0; i<columna.length; i++){
                lista[i].add(rowset.getString(columna[i]));
            }
        }
       rowset.close();
       for(int i = 0; i<lista.length; i++){
           arr[i] = new String[lista[i].size()];
           arr[i] = lista[i].toArray(arr[i]);
       }
       return arr;
    }

    public void subirSQL(Object[] datos,String tabla) throws ClassNotFoundException, SQLException{
        int length = datos.length;
        String[] datoString = new String[length];
        String query = "insert into " + tabla + " values('";
        for(int i = 0; i<length;i++){
            datoString[i] = datos[i].toString();
        }
        for(int j = 0; j<length-1; j++){
            query = query + datoString[j] + "','";
        }
        query = query + datoString[length-1] + "')";
        st.executeUpdate(query);
        
    }
    
    public boolean permisosCurso(Permisos.permiso permi, String usuario_id, String curso_id) throws SQLException{
        boolean permisos = false;
        AntiInjection checa = new AntiInjection();
        if(checa.isSafe(curso_id)){
            String[] permisosRec = busqSeparateStringArr(new String[]{"usuario_id","curso_id"},new String[]{usuario_id,curso_id},"permisos_id","usuario_curso");
            System.out.println(Arrays.toString(permisosRec));
            if(permisosRec.length > 0){
                System.out.println("Si pasa prueba de longitud");
                Permisos perm = new Permisos(permisosRec);
                if (perm.exists(permi)){
                    permisos = true;
                }
            }
        }
        return permisos;
    }

    public void subirSQL(Object[] datos, String[] pos, String tabla) throws ClassNotFoundException, SQLException {
        int length = datos.length;
        String[] datoString = new String[length];
        String query = "insert into " + tabla + "(";
        for(int i = 0; i<length;i++){
            datoString[i] = datos[i].toString();
        }
        for(int i = 0; i<length-1;i++){
            query = query + pos[i] + ",";
        }
        query = query + pos[length-1]+ ") values('";
        for(int j = 0; j<length-1; j++){
            query = query + datoString[j] + "','";
        }
        query = query + datoString[length-1] + "')";
        System.out.println(query);
        st.executeUpdate(query);
    }
    
    public int lastInsertID(){
        int lastInsertint = -1;
        try {
            ResultSet lastInsert = st.executeQuery("select last_insert_id()");
            lastInsert.next();
            lastInsertint = Integer.parseInt(lastInsert.getString("last_insert_id()"));
        } catch (SQLException ex) {
            Logger.getLogger(SubidorSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastInsertint;
    }
    
    public boolean truncTabla(String trunc){
        boolean success;
        boolean forbiddenTab = false;
        for(int i = 0; i<forbiddenTables.length; i++){
            if(trunc.equals(forbiddenTables[i])){
                forbiddenTab = true;
                break;
            }
        }
        try {
            if(!forbiddenTab){
                st.executeUpdate("truncate "+trunc);
                success = true;
            }
            else{
                success = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubidorSQL.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }

}

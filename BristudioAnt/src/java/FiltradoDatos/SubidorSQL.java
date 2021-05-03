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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz
 * Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
 */
public class SubidorSQL {

    int puerto = 3306;
    String bd;
    String user;
    String pass;
    private Connection con;
    BCryptPasswordEncoder pwdEncoder;
    private Statement st;
    private PreparedStatement lastIns;
    ObjectIO configuracion;
    String[] forbiddenTables;

    /**
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public SubidorSQL() {
        this.pwdEncoder = new BCryptPasswordEncoder(10);
        try {
            this.configuracion = new ObjectIO();
            this.forbiddenTables = new String[]{"permisos"};
            ConfigDB config = null;
            if (configuracion.exists("configDB.dat")) {
                config = (ConfigDB) configuracion.lee("configDB.dat");
                System.out.println("leyendo objectIO");
            } else {
                config = new ConfigDB("bristudio", "root", "n0m3l0");
                System.out.println("creando nuevo archivo");
            }
            this.bd = config.getBd();
            this.user = config.getUser();
            this.pass = config.getPass();
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + bd + "?autoReconnect=true&useSSL=false", user, pass);
            st = con.createStatement();
            lastIns = con.prepareStatement("select last_insert_id()");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean seguroSQL(Object[] datos, String[] pos, String tabla) {
        boolean sucess = true;
        try {
            subirSQL(datos, pos, tabla);
        } catch (ClassNotFoundException | SQLException ex) {
            sucess = false;
        }
        return sucess;
    }

    public boolean seguroSQL(Object[] datos, String tabla) {
        boolean sucess = true;
        try {
            subirSQL(datos, tabla);
        } catch (ClassNotFoundException | SQLException ex) {
            sucess = false;
        }
        return sucess;
    }

    public boolean seguroSQLValidado(Object[] datos, Checador.datosTipo[] tipos, int[][] tamanos, String tabla) {
        boolean sucess = true;
        if (new Checador().checa(tipos, tamanos, datos)) {
            try {
                subirSQL(datos, tabla);
            } catch (ClassNotFoundException | SQLException ex) {
                sucess = false;
            }
        } else {
            sucess = false;
        }
        return sucess;
    }

    public boolean seguroSQLValidado(Object[] datos, String[] pos, Checador.datosTipo[] tipos, int[][] tamanos, String tabla) {
        boolean sucess = true;
        if (new Checador().checa(tipos, tamanos, datos)) {
            System.out.println("pasa chequeo");
            try {
                subirSQL(datos, pos, tabla);
            } catch (ClassNotFoundException | SQLException ex) {
                sucess = false;
            }
        } else {
            sucess = false;
            System.out.println("NO PASA ANTIINJECTION");
        }
        return sucess;
    }

    public boolean seguroUpdateSQLValidado(Object[] datos, String[] pos, Checador.datosTipo[] tipos, int[][] tamanos, String tabla, int searchField) {
        boolean sucess = true;
        if (new Checador().checa(tipos, tamanos, datos)) {
            System.out.println("pasa chequeo");
            try {
                System.out.println("Si llega a etapa de subida");
                updateSQL(datos, pos, tabla, searchField);
            } catch (ClassNotFoundException | SQLException ex) {
                sucess = false;
                ex.printStackTrace();
            }
        } else {
            sucess = false;
        }
        return sucess;
    }

    public void cerrarConexion() throws SQLException {
        con.close();
    }

    public boolean inicioSesionSeguro(Object user, Object pass, String columnuser, String columnpass, String table) {
        Boolean inicio = false;
        PreparedStatement pst;
        ResultSet sesion;
        try {
            pst = con.prepareStatement("select contrasena from " + table + " where " + columnuser + " = ?");
            pst.setString(1, user.toString());
            pst.setString(2, pass.toString());
            sesion = pst.executeQuery();
            if(sesion.next()){
                if(pwdEncoder.matches(sesion.getString("contrasena"), pass.toString())){
                    inicio = true;
                }
            }
            sesion.close();
            pst.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return inicio;
    }

    public boolean existe(String buscar, String tabla, String column) throws SQLException {
        boolean exis;
        PreparedStatement pst = con.prepareStatement("select*from " + tabla + " where " + column + " = ?");
        pst.setString(1, buscar);
        ResultSet existir = pst.executeQuery();
        exis = existir.next();
        existir.close();
        return exis;
    }
    
    public boolean existe(String[] buscar, String tabla, String column[]) throws SQLException {
        boolean exis;
        String query = "select*from " + tabla + " where " + column[0] + " = ?";
        for(int i = 1; i < buscar.length; i++){
            query = query + " and " + column[i] + " = ?";
        }
        PreparedStatement pst = con.prepareStatement(query);
        for(int i = 0; i < buscar.length; i++){
            pst.setString(i+1,buscar[i]);
        }
        ResultSet existir = pst.executeQuery();
        exis = existir.next();
        existir.close();
        pst.close();
        return exis;
    }

    public String[] columnSet(String column, String table) throws SQLException {
        String[] columnSet;
        ArrayList<String> columner = new ArrayList<String>();
        ResultSet sql = st.executeQuery("select " + column + " from " + table);
        while (sql.next()) {
            columner.add(sql.getString(column));
        }
        columnSet = new String[columner.size()];
        for (int i = 0; i < columnSet.length; i++) {
            columnSet[i] = columner.get(i);
        }
        sql.close();
        return columnSet;
    }

    public String busqSeparate(String columnBusca, String columnSeleccion, String tabla, String busca) throws SQLException {
        String resultado;
        try (PreparedStatement pst = con.prepareStatement("select " + columnSeleccion + " from " + tabla + " where " + columnBusca + " = ?")) {
            System.out.println("select " + columnSeleccion + " from " + tabla + " where " + columnBusca + " = ?");
            pst.setString(1, busca);
            ResultSet sql = pst.executeQuery();
            if (sql.next()) {
                resultado = sql.getString(columnSeleccion);
            } else {
                resultado = null;
            }
            sql.close();
        }
        return resultado;
    }

    public String[] busqSeparateStringArr(String columnBusca, String columnSeleccion, String tabla, String busca) throws SQLException {
        AntiInjection sqlant = new AntiInjection();
        String[] resultados = null;
        String resultado;
        PreparedStatement pst = con.prepareStatement("select " + columnSeleccion + " from " + tabla + " where " + columnBusca + " = ?");
        pst.setString(1, busca);
        ResultSet sql = pst.executeQuery();
        System.out.println("select " + columnSeleccion + " from " + tabla + " where " + columnBusca + " = ?" + "at busqSeparate");
        CachedRowSet CRS = RowSetProvider.newFactory().createCachedRowSet();
        CRS.populate(sql);
        resultados = CRStoStringArray(CRS, columnSeleccion);
        sql.close();
        return resultados;
    }

    public String[] busqSeparateStringArr(String[] columnBusca, String[] busca, String columnSeleccion, String tabla) throws SQLException {
        String[] resultados;
        String resultado;
        StringBuilder query = new StringBuilder();
        query.append("select ").append(columnSeleccion).append(" from ").append(tabla).append(" where ").append(columnBusca[0]).append(" = ?");
        for (int i = 1; i < columnBusca.length; i++) {
            query.append(" and ").append(columnBusca[i]).append(" = ?");
        }

        PreparedStatement pst = con.prepareStatement(query.toString());
            for (int i = 0; i < columnBusca.length; i++) {
                pst.setString(i + 1, busca[i]);
            }
            try (ResultSet sql = pst.executeQuery()) {
                CachedRowSet CRS = RowSetProvider.newFactory().createCachedRowSet();
                CRS.populate(sql);
                resultados = CRStoStringArray(CRS, columnSeleccion);
            }
        return resultados;
    }
    
    public CachedRowSet busqSeparateCachedRowSet(String[] columnBusca, String[] busca, String[] columnSeleccion, String tabla) throws SQLException {
        String[] resultados;
        String resultado;
        StringBuilder query = new StringBuilder();
        query.append("select ");
        for(int i = 0; i < columnSeleccion.length-1; i++){
            query.append(columnSeleccion[i]).append(", ");
        }
        query.append(columnSeleccion[columnSeleccion.length-1]);
        query.append(" from ").append(tabla).append(" where ").append(columnBusca[0]).append(" = ?");
        for (int i = 1; i < columnBusca.length; i++) {
            query.append(" and ").append(columnBusca[i]).append(" = ?");
        }

        try (PreparedStatement pst = con.prepareStatement(query.toString())) {
            for (int i = 0; i < columnBusca.length; i++) {
                pst.setString(i + 1, busca[i]);
            }
            try (ResultSet sql = pst.executeQuery()) {
                CachedRowSet CRS = RowSetProvider.newFactory().createCachedRowSet();
                CRS.populate(sql);
                return CRS;
            }
        }
    }

    public int size(String columnIndex, String tabla) throws SQLException {
        int ind;
        try (ResultSet index = st.executeQuery("select " + columnIndex + " from " + tabla + " order by " + columnIndex + " desc limit 1")) {
            if (index.next()) {
                ind = Integer.parseInt(index.getString(columnIndex));
            } else {
                ind = 0;
            }
        }
        return ind;
    }

    public CachedRowSet getTable(String table) throws SQLException {
        ResultSet sql = st.executeQuery("select*from " + table);
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        crs.populate(sql);
        sql.close();
        return crs;
    }

    public boolean updateField(String columnSearch, String columnUpdate, String field, String buscar, String table) {
        boolean exito = true;
        try (PreparedStatement pst = con.prepareStatement("update " + table + " set " + columnUpdate + " = ? where " + columnSearch + " = ?")) {
            pst.setString(1, field);
            pst.setString(2, buscar);
            pst.executeUpdate();
        } catch (SQLException ex) {
            exito = false;
            Logger.getLogger(SubidorSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exito;
    }

    public CachedRowSet getRow(String buscar, String tabla, String searchField) throws SQLException {
        CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
        try (PreparedStatement pst = con.prepareStatement("select * from " + tabla + " where " + searchField + " = ?")) {
            pst.setString(1, buscar);
            ResultSet row = pst.executeQuery();
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(row);
            row.close();
            pst.close();

        } catch (SQLException ex) {
            System.out.println("revento el rowsettero");
            Logger.getLogger(SubidorSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return crs;
    }

    public boolean eliminarBusca(String elimina, String column, String table) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement("delete from " + table + " where " + column + " = ?")) {
            pst.setString(1, elimina);
            pst.executeUpdate();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean eliminarMultiParams(String[] buscar, String[] columns, String table) {
        boolean success = true;
        String query = "delete from " + table + " where " + columns[0] + " = ?";
        for (int i = 1; i < buscar.length; i++) {
            query += " AND " + columns[i] + " = ?";
        }

        try (PreparedStatement pst = con.prepareStatement(query)) {
            for (int i = 0; i < buscar.length; i++) {
                pst.setString(i + 1, buscar[i]);
            }

            pst.executeUpdate();
            pst.close();

        } catch (SQLException ex) {
            Logger.getLogger(SubidorSQL.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }

    public CachedRowSet relatedRow(String table, String field, String buscar) throws SQLException {
        CachedRowSet crs;
        try (PreparedStatement pst = con.prepareStatement("select*from " + table + " where " + field + " = ?")) {
            pst.setString(1, buscar);
            ResultSet sql = pst.executeQuery("select*from " + table + " where " + field + " = ?");
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(sql);
            sql.close();
        }
        return crs;
    }

    public boolean insertNull(String columnSearch, String columnUpdate, String buscar, String table) {
        boolean exito = true;
        try {
            PreparedStatement pst = con.prepareStatement("update " + table + " set " + columnUpdate + " = null where " + columnSearch + " = ?");
            pst.setString(1, buscar);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            exito = false;
            System.out.println(ex);
        }
        return exito;
    }

    public int relatedRowSize(String table, String target, String buscar) throws SQLException {
        int ind = 0;
        PreparedStatement pst = con.prepareStatement("select*from " + table + " where " + target + " = ?");
        pst.setString(1, buscar);
        ResultSet sql = pst.executeQuery();
        while (sql.next()) {
            ind++;
        }
        sql.close();
        pst.close();
        return ind;
    }

    public CachedRowSet innerJoiner(String[] table, String[] linker, String target, String buscar) throws SQLException {
        StringBuilder request = new StringBuilder();
        request.append("select ").append(linker[0]).append(" from ").append(table[0]);
        for (int i = 1; i < table.length; i++) {
            request.append(" inner join ").append(table[i]).append(" using (").append(linker[i]).append(")");
        }
        request.append(" where ").append(table[table.length - 1]).append(".").append(target).append(" = ?;");
        System.out.println(request.toString());
        CachedRowSet crs;
        try (PreparedStatement pst = con.prepareStatement(request.toString())) {
            pst.setString(1, buscar);
            try (ResultSet sql = pst.executeQuery()) {
                crs = RowSetProvider.newFactory().createCachedRowSet();
                crs.populate(sql);
            }
        }
        return crs;
    }

    public CachedRowSet innerJoinerMultiArg(String[] table, String[] linker, String[] searchTable, String[] searchArg, String[] search) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select*from ").append(table[0]);
        for (int i = 1; i < table.length; i++) {
            query.append(" inner join ").append(table[i]).append(" using (").append(linker[i]).append(")");
        }

        //request.append(" where " + searchTable[0] + "." + searchArg[0] + " = '" + search[0] + "'");
        query.append(" where ").append(searchTable[0]).append(".").append(searchArg[0]).append(" = ?");
        System.out.println(query.toString());
        for (int i = 1; i < searchTable.length; i++) {
            //request.append(" and " + searchTable[i] + "." + searchArg[i] + " = '" + search[i] + "'");
            query.append(" and ").append(searchTable[i]).append(".").append(searchArg[i]).append(" = ?");
        }
        System.out.println(query.toString());
        PreparedStatement pst = con.prepareStatement(query.toString());
        for (int i = 0; i < searchTable.length; i++) {
            pst.setString(i + 1, search[i]);
        }
        CachedRowSet crs;
        try (ResultSet sql = pst.executeQuery()) {
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(sql);
        }
        return crs;
    }

    public String[] CRStoStringArray(CachedRowSet rowset, String columna) throws SQLException {
        List<String> lista = new LinkedList<>();
        String[] arr;
        while (rowset.next()) {
            lista.add(rowset.getString(columna));
        }
        rowset.close();
        arr = new String[lista.size()];
        arr = lista.toArray(arr);
        System.out.println(Arrays.toString(arr));
        return arr;
    }

    public String[][] CRStoStringArrs(CachedRowSet rowset, String[] columna) throws SQLException {
        System.out.println(Arrays.toString(columna));
        LinkedList<String>[] lista = new LinkedList[columna.length];
        String[][] arr = new String[columna.length][];
        for (int i = 0; i < lista.length; i++) {
            lista[i] = new LinkedList();
        }
        while (rowset.next()) {
            for (int i = 0; i < columna.length; i++) {
                lista[i].add(rowset.getString(columna[i]));
            }
        }
        rowset.close();
        for (int i = 0; i < lista.length; i++) {
            arr[i] = new String[lista[i].size()];
            arr[i] = lista[i].toArray(arr[i]);
        }
        return arr;
    }

    public void subirSQL(Object[] datos, String tabla) throws ClassNotFoundException, SQLException {
        int length = datos.length;
        String[] datoString = new String[length];
        String query = "insert into " + tabla + " values(";
        for (int i = 0; i < length; i++) {
            datoString[i] = datos[i].toString();
        }
        for (int j = 0; j < length - 1; j++) {
            query = query + "?,";
        }
        query = query + "?)";
        PreparedStatement pst = con.prepareStatement(query);
        for (int j = 0; j < length; j++) {
            pst.setString(j + 1, datoString[j]);
        }
        pst.executeUpdate();
        pst.close();

    }

    public boolean permisosCurso(Permisos.permiso permi, String usuario_id, String curso_id) throws SQLException {
        boolean permisos = false;
        String[] permisosRec = busqSeparateStringArr(new String[]{"usuario_id", "curso_id"}, new String[]{usuario_id, curso_id}, "permisos_id", "usuario_curso");
        System.out.println(Arrays.toString(permisosRec));
        if (permisosRec.length > 0) {
            System.out.println("Si pasa prueba de longitud");
            Permisos perm = new Permisos(permisosRec);
            if (perm.exists(permi)) {
                permisos = true;
            }
        }
        return permisos;
    }

    public void subirSQL(Object[] datos, String[] pos, String tabla) throws ClassNotFoundException, SQLException {
        int length = datos.length;
        String[] datoString = new String[length];
        String query = "insert into " + tabla + "(";
        for (int i = 0; i < length; i++) {
            datoString[i] = datos[i].toString();
        }
        for (int i = 0; i < length - 1; i++) {
            query = query + pos[i] + ",";
        }
        query = query + pos[length - 1] + ") values(";
        for (int j = 0; j < length - 1; j++) {
            query = query + "?,";
        }
        query = query + "?)";
        PreparedStatement pst = con.prepareStatement(query);

        for (int j = 0; j < length; j++) {
            pst.setString(j + 1, datoString[j]);
        }

        System.out.println(query);
        pst.executeUpdate();
        pst.close();
    }

    public void updateSQL(Object[] datos, String[] pos, String tabla, int fieldSearch) throws ClassNotFoundException, SQLException {
        int length = datos.length;
        String[] datoString = new String[length];
        String query = "update " + tabla + " set ";
        for (int i = 0; i < length; i++) {
            datoString[i] = datos[i].toString();
            System.out.println(datoString[i]);
        }
        for (int i = 0; i < length - 1; i++) {
            if (i != fieldSearch) {
                query = query + pos[i] + " = ?, ";
            }
        }

        query = query + pos[length - 1] + " = ? where " + pos[fieldSearch] + " = ?";

        PreparedStatement pst = con.prepareStatement(query);
        int j = 1;
        for (int i = 0; i < length; i++) {
            if (i != fieldSearch) {
                pst.setString(j, datoString[i]);
                j++;
            }
        }

        pst.setString(j, datoString[fieldSearch]);

        System.out.println(query);
        try{
        pst.executeUpdate();
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    public int lastInsertID() {
        int lastInsertint = -1;
        try {
            ResultSet lastInsert = lastIns.executeQuery();
            lastInsert.next();
            lastInsertint = Integer.parseInt(lastInsert.getString("last_insert_id()"));
        } catch (SQLException ex) {
            Logger.getLogger(SubidorSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastInsertint;
    }

}

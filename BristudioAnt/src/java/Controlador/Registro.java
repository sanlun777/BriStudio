/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import FiltradoDatos.Checador;
import FiltradoDatos.Permisos;
import FiltradoDatos.SubidorSQL;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
 */
@WebServlet(name = "Registro", urlPatterns = {"/Registro"})
@MultipartConfig
public class Registro extends HttpServlet {
    SubidorSQL subidor;
    BCryptPasswordEncoder passEnc = new BCryptPasswordEncoder(10);

    public Registro() {
        this.subidor = new SubidorSQL();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Registro</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Registro at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            Permisos permisos = (Permisos)request.getSession().getAttribute("permisos_usuario");
            if(permisos.exists(Permisos.permiso.CREAUSUARIO)){
                String[] params = {"usuario_id", "usuario_nick", "sexo", "edad", "correo", "nombre", "apellido_p", "apellido_m", "contrasena"};
                Object[] recupera = new Object[params.length];
                int[][] tamanos = {{10,10},{2,20},{1,1},{1,3},{5,254},{2,30},{2,15},{2,15},{8,30}};
                Checador.datosTipo[] tipoDatos = {Checador.datosTipo.NUMERO,Checador.datosTipo.LETRA,Checador.datosTipo.BOOLEAN,Checador.datosTipo.NUMERO,Checador.datosTipo.EMAIL,Checador.datosTipo.LETRA,Checador.datosTipo.LETRA,Checador.datosTipo.LETRA,Checador.datosTipo.LETRA};
                for(int i = 0; i<params.length; i++){
                    Object param = request.getParameter(params[i]);
                    recupera[i] = param;
                }
                recupera[8] = passEnc.encode(recupera[8].toString());
                System.out.println(Arrays.toString(recupera));
                System.out.println(Arrays.toString(tamanos));
                System.out.println(Arrays.toString(tipoDatos));
                if(subidor.seguroSQLValidado(recupera,params,tipoDatos,tamanos,"usuarios")){
                    String[] permisoParams = {"crea_usuario","auditoria","crea_cursos","permite_crea_usuario"};
                    Permisos.permiso[] permisoParms = {Permisos.permiso.CREAUSUARIO, Permisos.permiso.AUDITORIA, Permisos.permiso.CREACURSOS,Permisos.permiso.PERMITECREAUSUARIO};
                    for(int i = 0; i<permisoParams.length; i++){
                        if((request.getParameter(permisoParams[i]) != null) && permisos.permisosRequeridos(permisoParams[i])){
                            Object[] busca = null;
                                busca = new Object[]{permisos.toID(permisoParms[i]),recupera[0].toString()};
                            try {
                                subidor.subirSQL(busca,new String[]{"permisos_id","usuario_id"},"permiso_usuario");
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Registro.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                Logger.getLogger(Registro.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    response.sendRedirect("/");
                }
                else{
                    response.sendRedirect("/HTML/error.html");
                }
            }
            else{
                PrintWriter out = response.getWriter();
                response.sendRedirect("/HTML/error.html");
            }
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

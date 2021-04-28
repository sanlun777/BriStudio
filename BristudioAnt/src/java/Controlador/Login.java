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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet{
    SubidorSQL subidor;

    public Login() {
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
            out.println("<title>Servlet Login</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Login at " + request.getContextPath() + "</h1>");
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
        //processRequest(request, response); NO PERMITIR ESTE COMANDO!!!!
        request.setCharacterEncoding("UTF-8");
        Object usuario = request.getParameter("usuario_nick");
        Object contrasena = request.getParameter("contrasena");
        boolean login = false;
        login = subidor.inicioSesionSeguro(usuario,contrasena,"usuario_nick","contrasena","usuarios");
        if(login){
            String userID = null;
            try {
                userID = subidor.busqSeparate("usuario_nick","usuario_id","usuarios",usuario.toString());
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            HttpSession sesion = request.getSession();
            sesion.setAttribute("id_usuario",usuario);
            sesion.setAttribute("num_usuario",userID);
            String[] table = {"permisos","permiso_usuario","usuarios"};
            String[] linker = {"permiso","permisos_id","usuario_id"};
            Permisos permisosSesion = null;
            //String[] cursos = {};
            //String[] cursosNombre = {};
            //Permisos.permiso[] permiCursos;
            try {
                permisosSesion = new Permisos(subidor.CRStoStringArray(subidor.innerJoiner(table,linker,"usuario_nick",usuario.toString()), "permiso"));
                System.out.println(permisosSesion);
                //CachedRowSet cursosCRS = subidor.getRow(userID,"usuario_curso","usuario_id");
                //CachedRowSet cursosCRSName = subidor.innerJoinerMultiArg(new String[]{"curso","usuario_curso"},new String[]{"curso","curso_id"},new String[]{"usuario_curso"},new String[]{"usuario_id"},new String[]{userID});
                //CachedRowSet cursosCRSPermi = subidor.innerJoiner(new String[]{"permisos","usuario_curso"},new String[]{"permiso","permiso_id"},"usuario_id",userID);
                //String[][] arrayParams = subidor.CRStoStringArrs(cursosCRSName,new String[]{"curso_id","curso"});
                //cursos = arrayParams[0];
                //cursosNombre = arrayParams[1];
                //permiCursos = permisosSesion.toPermisoArray(subidor.CRStoStringArray(cursosCRS,"permiso"));
                //sesion.setAttribute("cursos_id",Checador.stringAHtml(cursos));
                //System.out.println(Arrays.toString(cursos));
                //sesion.setAttribute("cursos_name",Checador.stringAHtml(cursosNombre));
                //System.out.println(Arrays.toString(cursosNombre));
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            sesion.setAttribute("permisos_usuario",permisosSesion);
        }
        response.sendRedirect("/");
        processRequest(request, response);
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

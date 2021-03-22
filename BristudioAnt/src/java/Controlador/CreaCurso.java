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

/**
 *
 * @author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
 */
@WebServlet(name = "CreaCurso", urlPatterns = {"/CreaCurso"})
public class CreaCurso extends HttpServlet {
    SubidorSQL subidor = new SubidorSQL();

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
            out.println("<title>Servlet CreaCurso</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreaCurso at " + request.getContextPath() + "</h1>");
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession();
            Permisos permisos = (Permisos)sesion.getAttribute("permisos_usuario");
            String usuarioID = sesion.getAttribute("num_usuario").toString();
            String[] cursosID = (String[]) sesion.getAttribute("cursos_id");
            String[] cursosName = (String[]) sesion.getAttribute("cursos_name");
            Permisos.permiso[] permisosCurso = (Permisos.permiso[])sesion.getAttribute("permisos_curso");
            if(permisos.exists(Permisos.permiso.CREACURSOS)){
                String[] params = {"curso"};
                Boolean[] retrieve = {true};
                Object[] recupera = new Object[params.length];
                int[][] tamanos = {{2,30}};
                Checador.datosTipo[] tipoDatos = {Checador.datosTipo.LETRA};
                for(int i = 0; i<params.length; i++){
                    if(retrieve[i]){
                        Object param = request.getParameter(params[i]);
                        recupera[i] = param;
                    }
                }
                
                
                System.out.println(Arrays.toString(recupera));
                System.out.println(Arrays.toString(tamanos));
                System.out.println(Arrays.toString(tipoDatos));
                if(subidor.seguroSQLValidado(recupera,params,tipoDatos,tamanos,"curso")){
                    try {
                        String cursoID = subidor.busqSeparate("curso","curso_id","curso",recupera[0].toString());
                        if(permisos.permisoAdd(Permisos.permiso.ADMINISTRADORCURSO,usuarioID,cursoID)){
                            if(cursosID == null){
                                cursosID = new String[]{cursoID};
                                cursosName = new String[]{recupera[0].toString()};
                                sesion.setAttribute("cursos_id", cursosID);
                                sesion.setAttribute("cursos_name", cursosName);
                            }
                            else{
                                String[] cursosID2 = new String[cursosID.length+1];
                                System.arraycopy(cursosID, 0, cursosID2, 0, cursosID.length);
                                cursosID2[cursosID.length] = cursoID;
                                sesion.setAttribute("cursos_id", cursosID2);
                                String[] cursosName2 = new String[cursosName.length+1];
                                System.arraycopy(cursosName, 0, cursosName2, 0, cursosName.length);
                                cursosName2[cursosName.length] = recupera[0].toString();
                                sesion.setAttribute("cursos_name", cursosName2);
                                response.sendRedirect("/BristudioAnt/");
                            }
                            
                        }
                        else{
                            response.sendRedirect("/BristudioAnt/HTML/error.html");
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(CreaCurso.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    response.sendRedirect("/BristudioAnt/HTML/error.html");
                }
            }
            else{
                response.sendRedirect("/BristudioAnt/HTML/error.html");
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

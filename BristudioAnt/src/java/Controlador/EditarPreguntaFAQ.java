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
 * @author Santi
 */
@WebServlet(name = "EditarPreguntaFAQ", urlPatterns = {"/EditarPreguntaFAQ"})
public class EditarPreguntaFAQ extends HttpServlet {
    SubidorSQL subidor;

    public EditarPreguntaFAQ() {
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
            out.println("<title>Servlet EditarPreguntaFAQ</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditarPreguntaFAQ at " + request.getContextPath() + "</h1>");
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
            HttpSession sesion = request.getSession();
            Permisos permisos = (Permisos)sesion.getAttribute("permisos_usuario");
            String usuarioID = sesion.getAttribute("num_usuario").toString();
            
            if(permisos.exists(Permisos.permiso.AUDITORIA) || permisos.exists(Permisos.permiso.FAQ)){
                
                String[] params = {"pregunta", "respuesta", "preg_id", "id_usuario_pregunta", "id_usuario_respuesta"};
                Boolean[] retrieve = {true, true, true, false, false};
                Object[] recupera = new Object[params.length];
                int[][] tamanos = {{0,255},{0,511},{-1,0},{10,10},{10,10}};
                Checador.datosTipo[] tipoDatos = {Checador.datosTipo.LETRA, Checador.datosTipo.LETRA, Checador.datosTipo.NUMERO, Checador.datosTipo.IGNORE, Checador.datosTipo.IGNORE};
                for(int i = 0; i<params.length; i++){
                    if(retrieve[i]){
                        Object param = request.getParameter(params[i]);
                        recupera[i] = param;
                    }
                }
                boolean cambio = true;
                boolean[] actualizar = new boolean[2];
                if(recupera[0].toString().length() != 0){
                    actualizar[0] = true;
                }
                if(recupera[1].toString().length() != 0){
                    actualizar[1] = true;
                }
                
                recupera[3] = usuarioID;
                recupera[4] = usuarioID;
                String[] paramsStore; /*= new Object[params.length - (actualizar[0] ? 1 : 0) - (actualizar[1] ? 1 : 0)];*/
                Object[] recuperaStore;
                int[][] tamanosStore;
                if(actualizar[0]){
                    if(!actualizar[1]){
                        paramsStore = new String[]{params[0], params[2], params[3]};
                        recuperaStore = new Object[]{recupera[0], recupera[2], recupera[3]};
                        tamanosStore = new int[][]{tamanos[0], tamanos[2], tamanos[3]};
                        params = paramsStore;
                        recupera = recuperaStore;
                        tamanos = tamanosStore;
                    }
                }
                else{
                    if(actualizar[1]){
                        paramsStore = new String[]{params[1], params[2], params[4]};
                        recuperaStore = new Object[]{recupera[1], recupera[2], recupera[4]};
                        tamanosStore = new int[][]{tamanos[1], tamanos[2], tamanos[4]};
                        params = paramsStore;
                        recupera = recuperaStore;
                        tamanos = tamanosStore;
                        
                    }
                    else cambio = false;
                }
                
                if(cambio){
                    System.out.println(Arrays.toString(recupera));
                    System.out.println(Arrays.toString(tamanos));
                    System.out.println(Arrays.toString(tipoDatos));
                    if(subidor.seguroUpdateSQLValidado(recupera,params,tipoDatos,tamanos,"faqPregunta",1)){
                        response.sendRedirect("/HTML/faqalum.jsp");
                    }
                    else{
                        response.sendRedirect("/HTML/error.html");
                    }
                }
                else{
                    response.sendRedirect("/HTML/faqalum.jsp");
                }
            }
            else{
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

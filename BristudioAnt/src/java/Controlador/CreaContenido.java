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
import java.time.LocalDateTime;
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
@WebServlet(name = "CreaContenido", urlPatterns = {"/CreaContenido"})
public class CreaContenido extends HttpServlet {

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
            out.println("<title>Servlet CreaContenido</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreaContenido at " + request.getContextPath() + "</h1>");
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
        HttpSession sesion = request.getSession();
        Object usuarioID = sesion.getAttribute("num_usuario");
        Object cursoID = request.getParameter("curso_id");
        if(usuarioID != null && cursoID != null){
            SubidorSQL subidor = new SubidorSQL();
            try {
                System.out.println("Checado de permisos");
                if(subidor.permisosCurso(Permisos.permiso.ADMINISTRADORCURSO,usuarioID.toString(),cursoID.toString()) || subidor.permisosCurso(Permisos.permiso.CREACONTENIDO,usuarioID.toString(),cursoID.toString())){
                    System.out.println("Si tiene permisos");
                    String[] params = {"curso_id","maestro_id","titulo_contenido","texto_descripcion","tiempo_creacion","tiempo_limite"};
                    Object[] recupera = new Object[params.length];
                    int[][] tamanos = {{0,100},{0,0},{0,30},{0,5000},{0,-1},{0,20}};
                    Checador.datosTipo[] checa = {Checador.datosTipo.NUMERO, Checador.datosTipo.IGNORE, Checador.datosTipo.LETRA, Checador.datosTipo.LETRA,Checador.datosTipo.IGNORE,Checador.datosTipo.FECHA};
                    for(int i = 0; i<recupera.length; i++){
                        recupera[i] = request.getParameter(params[i]);
                    }
                    LocalDateTime now = LocalDateTime.now();
                    recupera[0] = cursoID;
                    recupera[1] = usuarioID;
                    recupera[4] = now;
                    System.out.println(Arrays.toString(params));
                    System.out.println(Arrays.toString(recupera));
                    System.out.println(Arrays.toString(tamanos));
                    System.out.println(Arrays.toString(checa));
                    System.out.println("Previo a subir contenido");
                    if(subidor.seguroSQLValidado(recupera, params, checa, tamanos,"contenido")){
                        System.out.println("Si se subio contenido");
                        int actividadInt = subidor.lastInsertID();
                        boolean continua = true;
                        int i = 0;
                        while(continua){
                            Object prega = request.getParameter("prega" + i);
                            if(prega == null){
                                Object pregc = request.getParameter("pregc" + i);
                                if(pregc == null){
                                    continua = false;
                                }
                                else{
                                    String[] paramsPregc = {"contenido_id","tipo_pregunta_id","texto_pregunta"};
                                    Object[] recuperaPregc = {Integer.toString(actividadInt),"0",pregc};
                                    int[][] tamanosPregc = {{0,-1},{0,1},{0,200}};
                                    Checador.datosTipo[] checaPregc = {Checador.datosTipo.IGNORE, Checador.datosTipo.IGNORE, Checador.datosTipo.LETRA};
                                    Object correcta = request.getParameter("radg"+i);
                                    if(subidor.seguroSQLValidado(recuperaPregc, paramsPregc, checaPregc, tamanosPregc,"pregunta")){
                                        int preguntaInt = subidor.lastInsertID();
                                        boolean continuaInci = true;
                                        int j = 0;
                                        boolean correctaEncontrada = false;
                                        while(continuaInci){
                                            String inciReq = "pregc"+i+"_"+j;
                                            Object inci = request.getParameter(inciReq);
                                            if(inci != null && correcta != null){
                                                int correctaInt = 0;
                                                if(inciReq.equals(correcta.toString()) && !correctaEncontrada){
                                                    correctaInt = 1;
                                                    correctaEncontrada = true;
                                                }
                                                String[] paramsInci = {"pregunta_id","inciso","correcta"};
                                                Object[] recuperaInci = {preguntaInt,inci,Integer.toString(correctaInt)};
                                                int[][] tamanosInci = {{0,-1},{0,100},{0,-1}};
                                                Checador.datosTipo[] checaInci = {Checador.datosTipo.IGNORE,Checador.datosTipo.LETRA,Checador.datosTipo.IGNORE};
                                                subidor.seguroSQLValidado(recuperaInci, paramsInci, checaInci, tamanosInci,"inciso");
                                            }
                                            else{
                                                continuaInci = false;
                                            }
                                            j++;
                                        }
                                    }
                                }
                            }
                            else{
                                String[] paramsPrega = {"contenido_id","tipo_pregunta_id","texto_pregunta"};
                                Object[] recuperaPrega = {Integer.toString(actividadInt),"1",prega};
                                int[][] tamanosPrega = {{0,-1},{0,1},{0,200}};
                                Checador.datosTipo[] checaPrega = {Checador.datosTipo.IGNORE, Checador.datosTipo.IGNORE, Checador.datosTipo.LETRA};
                                if(subidor.seguroSQLValidado(recuperaPrega, paramsPrega, checaPrega, tamanosPrega,"pregunta")){
                                    //Subida con exito
                                }
                            }
                            i++;
                        }
                    }
                    
                }
            } catch (SQLException ex) {
                Logger.getLogger(CreaContenido.class.getName()).log(Level.SEVERE, null, ex);
            }
                
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

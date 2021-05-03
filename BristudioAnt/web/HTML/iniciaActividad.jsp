<%-- 
    Document   : iniciaActividad
    Created on : Apr 28, 2021, 6:28:50 PM
    Author     : Santi
--%>

<%@page import="FiltradoDatos.Checador"%>
<%@page import="javax.sql.rowset.CachedRowSet"%>
<%@page import="FiltradoDatos.SubidorSQL"%>
<%@page import="FiltradoDatos.Permisos"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="/CSS/estilosIndex.css" rel="stylesheet" type="text/css" media="all">
    <link href="https://fonts.googleapis.com/css2?family=Alfa+Slab+One&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Comfortaa:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Balsamiq+Sans:ital,wght@0,400;0,700;1,400;1,700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Questrial&display=swap" rel="stylesheet">
    <title>Bristudio</title>
</head>

<body>
    <div class="general">

        <header class="cabecera">
            <div class="logo">
                <a>Bristudio</a>
            </div>

            <nav>
                <%
                    HttpSession sesion = request.getSession();
                    Object idUsuario = sesion.getAttribute("id_usuario");
                    Object numUsuario = sesion.getAttribute("num_usuario");
                    Object contenidoID = request.getParameter("contenido_id");
                    
                    Permisos permisosUsuario = null;
                    boolean checar = false;
                    if(idUsuario != null){
                        out.println("<a href='#' class='hueco' font-color:#a4031f></a>");
                        out.println("<a href='HTML/cierraSesion.jsp'>Cerrar sesión</a>");
                        checar = true;
                        permisosUsuario = (Permisos)sesion.getAttribute("permisos_usuario");
                            if(permisosUsuario.exists(Permisos.permiso.CREAUSUARIO)){
                                out.println("<a href='#' class='hueco' font-color:#a4031f></a>");
                                out.println("<a href='HTML/register.jsp'>Registrar</a>");
                            }
                    }

                %>
            </nav>
        </header>
        <aside class="sidebar">
            <p>Bristudio es una plataforma que busca unificar las funciones de las plataformas normalmente utilizadas en una sola. La siguiente demostración incluye funcionalidades de login, creación de usuarios, y creación de cursos</p>
        </aside>
            <%
                    out.println("<main class='contenido'>");
                    if(checar){
                        SubidorSQL sube = new SubidorSQL();
                        Object idConten = request.getParameter("contenido_id");
                        if(contenidoID == null){
                            out.println("<p>Error</p>");
                        }
                        else{
                        String contenidoName = sube.busqSeparate("contenido_id","titulo_contenido","contenido",idConten.toString());
                        out.println("<h1>" + contenidoName + "</h1>");
                        //boolean permisosAdmin = sube.permisosCurso(Permisos.permiso.ADMINISTRADORCURSO,numUsuario.toString(),idCursos.toString());
                        //boolean permisosContenido = sube.permisosCurso(Permisos.permiso.CREACONTENIDO,numUsuario.toString(),idCursos.toString());
                        //boolean perteneceCurso = sube.existe(new String[]{idConten.toString(),numUsuario.toString()},"usuario_curso", new String[]{"curso_id","usuario_id"});
                        String cursoCuest = sube.busqSeparate("contenido_id","curso_id","contenido",idConten.toString());
                        out.println("</main>");
                        if(cursoCuest == null){
                            response.sendRedirect("/");
                        }else{
                        boolean perteneceCurso = sube.existe(new String[]{cursoCuest,numUsuario.toString()},"usuario_curso", new String[]{"curso_id","usuario_id"});
                            /*if(permisosAdmin || permisosContenido){
                                out.println("<br>");
                                    out.println("<form action = 'creaContenido.jsp'>");
                                    out.println("<input type=hidden id='curso_id' name='curso_id' value='"+idCursos.toString()+"'>");
                                    out.println("<input type=submit class='boton' value='Crear contenido de "+cursoName.toString()+"'>");
                                    out.println("</form>");
                            }*/
                            if(perteneceCurso){
                                System.out.println("Si pertenece al cursonnnnn--------------------------------; CICLOCONTENIDOSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                                CachedRowSet preguntas = sube.busqSeparateCachedRowSet(new String[]{"contenido_id"},new String[]{idConten.toString()},new String[]{"texto_pregunta","tipo_pregunta_id","pregunta_id"},"pregunta");
                                int i = 0;
                                out.println("<form action = 'respondePreguntas.jsp' method = 'post'>");
                                out.println("<input type=hidden id='contenido_id' name='contenido_id' value='"+idConten+"'>");
                                while(preguntas.next()){
                                    out.println("<main class='contenido'>");
                                    System.out.println("CICLO Preguntas------------------------------------------------");
                                    out.println("<section class='formulario-registro'>");
                                    out.println("<p>"+i+": "+Checador.stringAHtml(preguntas.getString("texto_pregunta"))+"</p>");
                                    System.out.println(preguntas.getString("tipo_pregunta_id"));
                                    if(preguntas.getString("tipo_pregunta_id") == "true"){
                                        out.println("<input type=text id='pregunta_id' name='"+preguntas.getString("pregunta_id")+"''>");
                                    }
                                    else{
                                        String preguntaID = preguntas.getString("pregunta_id");
                                        CachedRowSet incisos = sube.busqSeparateCachedRowSet(new String[]{"pregunta_id"},new String[]{preguntaID},new String[]{"inciso_id","inciso"},"inciso");
                                        while(incisos.next()){
                                            out.println("<input type=radio id='"+incisos.getString("inciso_id")+"' name='"+preguntaID+"' value='"+incisos.getString("inciso_id")+"'");
                                            out.println("<label for='"+incisos.getString("inciso_id")+"'>"+incisos.getString("inciso")+"</label>");
                                        }
                                        incisos.close();
                                        out.println("</section>");
                                    }
                                    //out.println("<input type=hidden id='pregunta_id' name='pregunta_id' value='"+preguntas.getString("pregunta_id")+"'>");
                                    //out.println("<input type=submit class='boton' value='"+Checador.stringAHtml(preguntas.getString("titulo_contenido"))+"'>");
                                    //out.println("<p>Fecha límite: "+Checador.stringAHtml(preguntas.getString("tiempo_limite"))+"</p>");
                                    /*if(permisosAdmin || permisosContenido){
                                        out.println("<form action = 'eliminaActividad.jsp' method='post'>");
                                        out.println("<input type=hidden id='cciaActividadontenido_id' name='contenido_id' value='"+actividades.getString("contenido_id")+"'>");
                                        out.println("<input type=submit class='boton' value='Eliminar "+Checador.stringAHtml(actividades.getString("titulo_contenido"))+"'>");
                                        out.println("</form>");
                                    }*/
                                    i++;
                                    out.println("</main>");
                                }
                                out.println("<input type=submit class='boton' value='Enviar cuestionario'>");
                                preguntas.close();
                                out.println("</form>");
                            }
                            
                            
                            /*if(permisosAdmin){
                                    out.println("<br>");
                                    out.println("<form action = '/EliminarCurso' method = 'post'>");
                                    out.println("<input type=hidden id='curso_id' name='curso_id' value='"+idCursos.toString()+"'>");
                                    out.println("<input type=submit class='boton' value='Eliminar "+cursoName.toString()+"'>");
                                    out.println("</form>");
                            }*/
                        }
                        }
                    }
                    else{
                        response.sendRedirect("/");
                    }
                    
                %>

        </main>
        
<!--

        <div class="widget-1">
            <h3>widget 1</h3>
        </div>

        <div class="widget-2">
            <h3>widget 2</h3>
        </div>

-->

        <footer class="footer">
            <p>Copyright © 2021 - Todos los derechos reservados - <a href="http://www.molesoft.net">Molesoft.net</a>  Hecha por el Equipo de Molesoft</p>
        </footer>

    </div>
</body>

</html>

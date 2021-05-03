<%-- 
    Document   : curso
    Created on : Dec 20, 2020, 2:07:09 PM
    author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
--%>

<%@page import="FiltradoDatos.Checador"%>
<%@page import="javax.sql.rowset.CachedRowSet"%>
<%@page import="FiltradoDatos.SubidorSQL"%>
<%@page import="FiltradoDatos.Permisos"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                    
                    Permisos permisosUsuario = null;
                    boolean checar = false;
                    if(idUsuario == null){
                    }
                    else{
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
        <main class="contenido">
            <%
                    if(checar){
                        SubidorSQL sube = new SubidorSQL();
                        Object idCursos = request.getParameter("curso_id");
                        if(idCursos == null){
                            out.println("<p>Error</p>");
                        }
                        else{
                        String cursoName = sube.busqSeparate("curso_id","curso","curso",idCursos.toString());
                        out.println("<h1>" + cursoName + "</h1>");
                        boolean permisosAdmin = sube.permisosCurso(Permisos.permiso.ADMINISTRADORCURSO,numUsuario.toString(),idCursos.toString());
                        boolean permisosContenido = sube.permisosCurso(Permisos.permiso.CREACONTENIDO,numUsuario.toString(),idCursos.toString());
                        boolean perteneceCurso = sube.existe(new String[]{idCursos.toString(),numUsuario.toString()},"usuario_curso", new String[]{"curso_id","usuario_id"});
                            if(permisosAdmin || permisosContenido){
                                out.println("<br>");
                                    out.println("<form action = 'creaContenido.jsp'>");
                                    out.println("<input type=hidden id='curso_id' name='curso_id' value='"+idCursos.toString()+"'>");
                                    out.println("<input type=submit class='boton' value='Crear contenido de "+cursoName.toString()+"'>");
                                    out.println("</form>");
                            }
                            if(perteneceCurso || permisosAdmin){
                                System.out.println("Si pertenece al cursonnnnn--------------------------------");
                                CachedRowSet actividades = sube.busqSeparateCachedRowSet(new String[]{"curso_id"},new String[]{idCursos.toString()},new String[]{"titulo_contenido","contenido_id","tiempo_limite"},"contenido");
                                while(actividades.next()){
                                    out.println();
                                    System.out.println("CICLO ACTIVIDADES------------------------------------------------");
                                    out.println("<form action = 'iniciaActividad.jsp' method = 'post'>");
                                    out.println("<input type=hidden id='contenido_id' name='contenido_id' value='"+actividades.getString("contenido_id")+"'>");
                                    out.println("<input type=submit class='boton' value='"+Checador.stringAHtml(actividades.getString("titulo_contenido"))+"'>");
                                    out.println("<p>Fecha límite: "+Checador.stringAHtml(actividades.getString("tiempo_limite"))+"</p>");
                                    out.println("</form>");
                                    if(permisosAdmin || permisosContenido){
                                        out.println("<form action = 'eliminaActividad.jsp' method='post'>");
                                        out.println("<input type=hidden id='cciaActividadontenido_id' name='contenido_id' value='"+actividades.getString("contenido_id")+"'>");
                                        out.println("<input type=submit class='boton' value='Eliminar "+Checador.stringAHtml(actividades.getString("titulo_contenido"))+"'>");
                                        out.println("</form>");
                                    }
                                }
                            }
                            
                            
                            if(permisosAdmin){
                                    out.println("<br>");
                                    out.println("<form action = '/EliminarCurso' method = 'post'>");
                                    out.println("<input type=hidden id='curso_id' name='curso_id' value='"+idCursos.toString()+"'>");
                                    out.println("<input type=submit class='boton' value='Eliminar "+cursoName.toString()+"'>");
                                    out.println("</form>");
                            }
                        }
                    }
                    else{
                        out.println("<p>Inicie sesión para revisar este curso</p>");
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

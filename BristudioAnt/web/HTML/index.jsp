<%-- 
    Document   : index
    Created on : Dec 15, 2020, 8:38:34 AM
    author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
--%>

<%@page import="FiltradoDatos.Permisos"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="/BristudioAnt/CSS/estilosIndex.css" rel="stylesheet" type="text/css" media="all">
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
                    String[] cursosID = (String[])sesion.getAttribute("cursos_id");
                    String[] cursosName = (String[])sesion.getAttribute("cursos_name");
                    
                    Permisos permisosUsuario = null;
                    boolean checar = false;
                    if(idUsuario == null){
                        out.println("<a href='HTML/login.jsp'>Iniciar sesión</a>");
                        out.println("<a href='#' class='hueco' font-color:#a4031f></a>");
                        out.println("<a href='HTML/chat.html'>Chat general Bristudio</a>");
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
                        if(permisosUsuario.exists(Permisos.permiso.CREACURSOS)){
                            out.println("<br>");
                            out.println("<a href='HTML/creaCurso.jsp'>Crear nuevo curso</a>");
                            out.println("</br>");
                        }
                        if(cursosID == null){
                            out.println("<br>");
                            out.println("<a href='HTML/creaCurso.jsp'>No hay cursos a los que se pertenezca que mostrar</a>");
                            out.println("</br>");
                        }
                        else{
                            for(int i = 0; i<cursosID.length; i++){
                                out.println("<br>");
                                out.println("<form action = 'HTML/curso.jsp'>");
                                out.println("<input type=hidden id='curso_id' name='curso_id' value='"+cursosID[i]+"'>");
                                out.println("<input type=submit class='boton' value='"+cursosName[i]+"'>");
                                out.println("</form>");
                            }
                        }
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

<%-- 
    Document   : creaCurso
    Created on : Dec 20, 2020, 12:17:59 PM
    author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
--%>

<%@page import="FiltradoDatos.Permisos"%>
<%@page import="FiltradoDatos.SubidorSQL"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="/BristudioAnt/CSS/estilosRegister.css" rel="stylesheet" type="text/css" media="all">
    <link href="https://fonts.googleapis.com/css2?family=Alfa+Slab+One&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Comfortaa:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Balsamiq+Sans:ital,wght@0,400;0,700;1,400;1,700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Questrial&display=swap" rel="stylesheet">
    <title>Crear cuenta</title>
</head>

<body>
    <div class="general">

        <header class="cabecera">

            <div class="encabezado">
                <h2>Registrate</h2>
            </div>

            <div class="logo">
                <a href="/BristudioAnt/">Bristudio</a>
            </div>

        </header>

                <%
                    HttpSession sesion = request.getSession();
                    Object idUsuario = sesion.getAttribute("id_usuario");
                    if(idUsuario == null){
                        out.println("<h3>Inicia sesión para poder registrar usuarios</h3>");
                    }
                    else{
                        Permisos permisosUsuario = (Permisos)sesion.getAttribute("permisos_usuario");
                        if(permisosUsuario.exists(Permisos.permiso.CREACURSOS)){
                            out.println("<main class='contenido'>");

                                out.println("<center><p class='tituloPanel'>Crear nuevo curso</p></center>");

                                    out.println("<section class='formulario-registro'>");

                                        out.println("<form action = '/BristudioAnt/CreaCurso' method = 'post'>");
                                            out.println("<input class='campos' type='text' name='curso' id='curso' placeholder='Inserte título del curso' minlength='2' maxlength='30' required title='Inserte título del curso'>");
                                            out.println("<input type=submit class='boton' value='Crear curso'>");
                                        out.println("</form>");
                                    out.println("</section>");
                            out.println("</main>");
                        }
                    }
                %>
        
<!--

        <div class="widget-1">
            <h3>widget 1</h3>
        </div>

        <div class="widget-2">
            <h3>widget 2</h3>
        </div>

-->

        <footer class="footer">
            <p>Copyright © 2020 - Todos los derechos reservados - <a href="http://www.molesoft.net">Molesoft.net</a>  Hecha por el Equipo de Molesoft</p>
        </footer>

    </div>
</body>

</html>

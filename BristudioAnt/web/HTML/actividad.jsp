<%-- 
    Document   : Register
    Created on : Dec 17, 2020, 5:52:50 PM
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
    <link href="/CSS/estilosRegister.css" rel="stylesheet" type="text/css" media="all">
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
                <a href="/">Bristudio</a>
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

                                out.println("<center><p class='tituloPanel'>Crear nuevo contenido</p></center>");

                                    out.println("<section class='formulario-registro'>");

                                        out.println("<form action = '/Registro' method = 'post'>");
                                            out.println("<input class='campos' type='number' name='titulo_contenido' id='titulo_contenido' placeholder='inserte título' minlength='10' maxlength='10' max='9999999999' required title='inserte título de la actividad'>");
                                            out.println("<input class='campos' type='text' name='texto_descripcion' id='texto_descripcion' placeholder='Descripción de la actividad' required minlength='2' maxlength='20' title='Escribe la descripción de la actividad'>");
                                            out.println("<input class='campos' type='datetime-local' name='tiempo_limite' id='nombre' placeholder='Nombre' required pattern='[a-zA-Záéíóú]+' minlength='2' maxlength='20' title='Escribe un nombre válido, utilizando mayúsculas y minúsculas.'>");
                                            out.println("<input class='campos' type='text' name='apellido_p' id='apellidos_p' placeholder='Apellido paterno' required pattern='[A-Za-záéíóú]+' minlength='2' maxlength='20' title='Escribe un apellido válido, utilizando mayúsculas y minúsculas.'>");
                                            out.println("<input class='campos' type='text' name='apellido_m' id='apellidos_m' placeholder='Apellido materno' required pattern='[A-Za-záéíóú]+' minlength='2' maxlength='20' title='Escribe un apellido válido, utilizando mayúsculas y minúsculas.'>");
                                            out.println("<input class='campos' type='number' name='edad' id='edad' placeholder='Edad' min='7' max='100' required title='La edad mínima para usar Bristudio es 7, la edad máxima es 100'>");

                                            out.println("<p class='sexo'>Sexo</p>");
                                            out.println("<input type='radio' name='sexo' id='0' value='0' required>");
                                            out.println("<label for='0'>Mujer</label>");
                                            out.println("<br>");
                                            out.println("<input type='radio' name='sexo' id='1' value='1' required>");
                                            out.println("<label for='1'>Hombre</label>");
                                            out.println("<br>");

                                            out.println("<input class='campos' type='email' name='correo' id='correo' placeholder='Correo' required>");
                                            out.println("<input class='campos' type='password' name='contrasena' id='contrasena' placeholder='Contraseña nueva' required pattern='[A-Za-z0-9!?-]{8,20}' title='Una contraseña segura consta de mínimo 8 caracteres y de máximo 20.'>");
                                            

                                            out.println("<input type=submit class='boton' value='Registrar'>");
                                            out.println("<p class='cuenta'><a href='../HTML/login.html'>¿Ya tienes una cuenta?</a></p>");
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
            <p>Copyright © 2021 - Todos los derechos reservados - <a href="http://www.molesoft.net">Molesoft.net</a>  Hecha por el Equipo de Molesoft</p>
        </footer>

    </div>
</body>

</html>

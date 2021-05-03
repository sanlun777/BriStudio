<%-- 
    Document   : creaCurso
    Created on : Dec 20, 2020, 12:17:59 PM
    author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
--%>

<%@page import="javax.sql.rowset.CachedRowSet"%>
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
    <title>FAQ</title>
</head>

<body>
    <div class="general">

        <header class="cabecera">

            <div class="encabezado">
                <h2>FAQ</h2>
            </div>

            <div class="logo">
                <a href="/">Bristudio</a>
            </div>

        </header>
        <main>
                <%
                    HttpSession sesion = request.getSession();
                    Object idUsuario = sesion.getAttribute("id_usuario");
                    if(idUsuario == null){
                        out.println("<h3>Inicia sesión para poder acceder a la FAQ</h3>");
                    }
                    else{
                        Permisos permisosUsuario = (Permisos)sesion.getAttribute("permisos_usuario");
                        if(permisosUsuario.exists(Permisos.permiso.FAQ) || permisosUsuario.permisosRequeridos(Permisos.permiso.FAQ)){
                            SubidorSQL sql = new SubidorSQL();
                            CachedRowSet preguntas = sql.getTable("faqPregunta");
                            int i = 1;
                            while(preguntas.next()){
                                    out.println("<section class='formulario-registro'>");
                                        out.println("<form action = '/EditarPreguntaFAQ' method = 'post'>");
                                            out.println("<input class='campos' type='text' name='pregunta' id='pregunta' placeholder='"+i+": "+preguntas.getString("pregunta")+"' minlength='2' maxlength='255' title='Editar pregunta'>");
                                            out.println("<h3>Pregunta hecha por: "+sql.busqSeparate("usuario_id", "usuario_nick", "usuarios", preguntas.getString("id_usuario_pregunta"))+"</h3>");
                                            out.println("<input class='campos' type='text' name='respuesta' id='respuesta' placeholder='"+i+": "+preguntas.getString("respuesta")+"' minlength='2' maxlength='255' title='Editar respuesta'>");
                                            out.println("<h3>Pregunta respondida por: "+sql.busqSeparate("usuario_id", "usuario_nick", "usuarios", preguntas.getString("id_usuario_respuesta"))+"</h3>");
                                            out.println("<input type='hidden' name='preg_id' id='preg_id' value='" + preguntas.getString("preg_id") + "'>");
                                            out.println("<input type=submit class='boton' value='Editar'>");
                                        out.println("</form>");
                                        out.println("<form action = '/EliminarPreguntasFAQ' method = 'post'>");
                                            out.println("<input type='hidden' name='preg_id' id='preg_id' value='" + preguntas.getString("preg_id") + "'>");
                                            out.println("<input type=submit class='boton' value='Eliminar pregunta'>");
                                        out.println("</form>");
                                    out.println("</section>");
                                i++;
                            }
                            if(i == 1){
                                out.println("No existen preguntas a mostrar. Puedes agregar preguntas");
                            }
                            out.println("<h3>Crear preguntas FAQ</h3>");
                            out.println("<section class='formulario-registro'>");
                            out.println("<form action = '/CrearPreguntaFAQ' method = 'post'>");
                                out.println("<input class='campos' type='text' name='pregunta' id='pregunta' placeholder='Inserte pregunta' minlength='2' maxlength='255' required title='Pregunta'>");
                                out.println("<input class='campos' type='text' name='respuesta' id='respuesta' placeholder='Inserte Respuesta' minlength='2' maxlength='255' required title='Respuesta'>");
                                out.println("<input type=submit class='boton' value='Crear nueva pregunta'>");
                            out.println("</form>");
                            out.println("</section>");
                        }
                        else{
                            SubidorSQL sql = new SubidorSQL();
                            CachedRowSet preguntas = sql.getTable("faqPregunta");
                            int i = 1;
                            out.println("<section class='formulario-registro'>");
                            while(preguntas.next()){
                                    out.println("<h3>"+i+":"+preguntas.getString("pregunta")+"</h3>");
                                    out.println("<h3>R: "+preguntas.getString("respuesta")+"</h3>");
                                    out.println("<br>");
                                i++;
                            }
                            if(i == 1){
                                out.println("No existen preguntas a mostrar");
                            }
                            out.println("</section>");
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

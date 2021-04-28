<%-- 
    Document   : cierraSesion
    Created on : Dec 19, 2020, 11:51:55 AM
    author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%request.getSession().invalidate();%>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="../CSS/estilosLogout.css" rel="stylesheet" type="text/css" media="all">
        <link href="https://fonts.googleapis.com/css2?family=Alfa+Slab+One&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Comfortaa:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Balsamiq+Sans:ital,wght@0,400;0,700;1,400;1,700&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Questrial&display=swap" rel="stylesheet">
        <title>Log out</title>
    </head>

    <body>
        <div class="general">

            <header class="cabecera">

                <div class="encabezado">
                    <h2>Log out</h2>
                </div>

                <div class="logo">
                    <a href="/">Bristudio</a>
                </div>

            </header>

            <main class="contenido">

                <center> <p class="tituloPanel">Cierre de sesión</p> </center>

                <section class="formulario-login">

                    <center>

                    <h2> Tu sesión se cerró satisfactoriamente. </h2>
                    
                    <br>

                    <br>
                    
                    <p> Da clic al botón para regresar a la página principal. </p>
                    
                    <br>
            
                    <br>

                    </center>

                    <form action="/" method="post">
                        <input type="submit" class="boton" value="Regresar">
                    </form>

                </section>


            </main>

            <!-- Widget coqueto por si ocupas 
            
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

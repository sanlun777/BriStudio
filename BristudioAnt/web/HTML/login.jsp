<%-- 
    Document   : login
    Created on : Dec 17, 2020, 5:08:46 PM
    author Axotla Ibañez Bruno Patricio , Ortega Mendoza Jorge Uriel , Quiroz Simon Alexia , Romero Mendez Francisco , Vásquez Luna Santiago Daniel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="../CSS/estilosLogin.css" rel="stylesheet" type="text/css" media="all">
    <link href="https://fonts.googleapis.com/css2?family=Alfa+Slab+One&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Comfortaa:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Balsamiq+Sans:ital,wght@0,400;0,700;1,400;1,700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Questrial&display=swap" rel="stylesheet">
    <title>Iniciar sesión</title>
</head>

<body>
    <div class="general">

        <header class="cabecera">

            <div class="encabezado">
                <h2>Inicia sesión</h2>
            </div>

            <div class="logo">
                <a href="/BristudioAnt/">Bristudio</a>
            </div>

        </header>

        <main class="contenido">

            <center> <p class="tituloPanel">Log in</p> </center>
            
                <section class="formulario-login">

                    <form action = "/BristudioAnt/Login" method = "post">
                        <input class="campos" type="text" name="usuario_nick" id="usuario_nick" placeholder="Nickname" required>
                        <input class="campos" type="password" name="contrasena" id="contrasena" placeholder="Contrasena" required pattern="[A-Za-z0-9!?-]{8,20}" title="Una contraseña segura consta de mínimo 8 caracteres y de máximo 20."> 

                        <input type=submit class="boton" value="Iniciar sesión">
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

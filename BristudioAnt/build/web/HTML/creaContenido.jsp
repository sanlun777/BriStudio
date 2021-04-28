<%-- 
    Document   : creaContenido
    Created on : Jan 5, 2021, 11:57:50 AM
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
    <link href="../CSS/estilosContenido.css" rel="stylesheet" type="text/css" media="all">
    <link href="https://fonts.googleapis.com/css2?family=Alfa+Slab+One&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Comfortaa:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Balsamiq+Sans:ital,wght@0,400;0,700;1,400;1,700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Questrial&display=swap" rel="stylesheet">
    <title>Crear contenido</title>
    <script>
        var i = 0;
        var j = [];
        function anadirPreguntas() {
                //var br = document.createElement("br");
          var pregLbl = document.createElement("label");
          var pregLblFor = document.createAttribute("for");
          var tipoPregElem = document.getElementById("preguntaTipo");
          var tipoPreg = tipoPregElem.value;
          var preg = document.createElement("input");
          var attType = document.createAttribute("type");
          var attClass = document.createAttribute("class");
          var attName = document.createAttribute("name");
          var attID = document.createAttribute("id");
          attType.value = "text";
          attClass.value = "campos";
          preg.setAttributeNode(attType);
          preg.setAttributeNode(attClass);
          
          var lblAttClass = document.createAttribute("class");
          
          if(tipoPreg === "abierta"){
          lblAttClass.value = "campos";
          pregLblFor.value = "prega" + i;
          attName.value = "prega" + i;
          attID.value = "prega" + i;
          pregLbl.innerHTML = i;
          preg.setAttributeNode(attName);
          preg.setAttributeNode(attID);
          pregLbl.setAttributeNode(pregLblFor);
          pregLbl.setAttributeNode(lblAttClass);
          //preg.appendChild(text);
          document.querySelector("#anadeHere").appendChild(pregLbl);
          pregLbl.insertAdjacentHTML('afterEnd', preg.outerHTML);
          j.push(-1);
          //br.appendChild(preg);
         }
         else{
            lblAttClass.value = "campos";
            pregLblFor.value = "pregc" + i;
            attName.value = "pregc" + i;
            attID.value = "pregc" + i;
            var lblAttID = document.createAttribute("id");
            lblAttID.value = "addh" + i;
            pregLbl.innerHTML = i;
            preg.setAttributeNode(attName);
            preg.setAttributeNode(attID);
            pregLbl.setAttributeNode(pregLblFor);
            pregLbl.setAttributeNode(lblAttClass);
            pregLbl.setAttributeNode(lblAttID);
            document.querySelector("#anadeHere").appendChild(pregLbl);
            pregLbl.insertAdjacentHTML('beforeEnd', preg.outerHTML);
            var e;
            for(e = 0; e<2; e++){
                var value = "pregc"+i+"_"+e;
                
                var incisoRadio = document.createElement("input");
                var typeIncisoRadio = document.createAttribute("type");
                typeIncisoRadio.value = "radio";
                var nameIncisoRadio = document.createAttribute("name");
                nameIncisoRadio.value = "radg"+i;
                //var classIncisoRadio = document.createAttribute("class");
                //classIncisoRadio.value = "campos";
                incisoRadio.setAttributeNode(typeIncisoRadio);
                incisoRadio.setAttributeNode(nameIncisoRadio);
                //incisoRadio.setAttributeNode(classIncisoRadio);
                var IDIncRad = document.createAttribute("value");
                IDIncRad.value = value;
                incisoRadio.setAttributeNode(IDIncRad);
                pregLbl.insertAdjacentHTML('beforeEnd', incisoRadio.outerHTML);
                
                var txtInciso = document.createElement("input");
                var typeInciso = document.createAttribute("type");
                typeInciso.value = "text";
                var nameInciso = document.createAttribute("name");
                nameInciso.value = value;
                var classInciso = document.createAttribute("class");
                classInciso.value = "campos";
                txtInciso.setAttributeNode(typeInciso);
                txtInciso.setAttributeNode(nameInciso);
                txtInciso.setAttributeNode(classInciso);
                pregLbl.insertAdjacentHTML('beforeEnd', txtInciso.outerHTML);
                
            }
            j.push(2);
            var boton = document.createElement("button");
            var btnType = document.createAttribute("type");
            btnType.value = "button";
            boton.setAttributeNode(btnType);
            var btnClass = document.createAttribute("class");
            btnClass.value = "boton";
            boton.setAttributeNode(btnClass);
            var btnOnClick = document.createAttribute("onclick");
            btnOnClick.value = "anadirIncisos("+i+")";
            boton.setAttributeNode(btnOnClick);
            boton.innerHTML = "Agregar incisos";
            pregLbl.insertAdjacentHTML('afterEnd', boton.outerHTML);
            
            //br.appendChild(preg);
         }
         i++;
        }
        function anadirIncisos(h){
            var anadeHere = document.getElementById("addh"+h);
            var f = j[h];
            var value = "pregc"+h+"_"+f;
                
            var incisoRadio = document.createElement("input");
            var typeIncisoRadio = document.createAttribute("type");
            typeIncisoRadio.value = "radio";
            var nameIncisoRadio = document.createAttribute("name");
            nameIncisoRadio.value = "radg"+h;
            //var classIncisoRadio = document.createAttribute("class");
            //classIncisoRadio.value = "campos";
            incisoRadio.setAttributeNode(typeIncisoRadio);
            incisoRadio.setAttributeNode(nameIncisoRadio);
            //incisoRadio.setAttributeNode(classIncisoRadio);
            var IDIncRad = document.createAttribute("value");
            IDIncRad.value = value;
            incisoRadio.setAttributeNode(IDIncRad);
            anadeHere.insertAdjacentHTML('beforeEnd', incisoRadio.outerHTML);
                
            var txtInciso = document.createElement("input");
            var typeInciso = document.createAttribute("type");
            typeInciso.value = "text";
            var nameInciso = document.createAttribute("name");
            nameInciso.value = value;
            var classInciso = document.createAttribute("class");
            classInciso.value = "campos";
            txtInciso.setAttributeNode(typeInciso);
            txtInciso.setAttributeNode(nameInciso);
            txtInciso.setAttributeNode(classInciso);
            anadeHere.insertAdjacentHTML('beforeEnd', txtInciso.outerHTML);
            j[h]++;
            
        }
    </script>
    
</head>

<body>
    <div class="general">

        <header class="cabecera">

            <div class="encabezado">
                <h2>Crear contenido</h2>
            </div>

            <div class="logo">
                <a href="/">Bristudio</a>
            </div>

        </header>

        <main class="contenido">

            <center> <p class="tituloPanel">Crea actividad</p> </center>
            <%
                Object idCurso = request.getParameter("curso_id");
                if(idCurso == null){
                    out.println("<p>Error<p>");
                }
                else{
                    HttpSession sesion = request.getSession();
                    Object numUsuario = sesion.getAttribute("num_usuario");
                    if(numUsuario == null){
                        out.println("<p>Error<p>");
                    }
                    else{
                        SubidorSQL subidor = new SubidorSQL();
                        if(subidor.permisosCurso(Permisos.permiso.ADMINISTRADORCURSO,numUsuario.toString(),idCurso.toString()) || subidor.permisosCurso(Permisos.permiso.CREACONTENIDO,numUsuario.toString(),idCurso.toString())){
                            out.println("<section class='formulario-login'>");
                                out.println("<form action = '/CreadorContenido' method = 'post'>");
                                    out.println("<input type = 'hidden' id='curso_id' name='curso_id' value='" + idCurso.toString() + "'>");
                                    out.println("<input class='campos' type='text' name='titulo_contenido' id='titulo_contenido' placeholder='Titulo del contenido' required>");
                                    out.println("<input class='campos' type='textarea' name='texto_descripcion' id='texto_descripcion' placeholder='Descripcion' title='Describa el contenido aquí'>");
                                    out.println("<input class='campos' type='datetime-local' name='tiempo_limite' id='tiempo_limite' placeholder='Límite de tiempo'>");
                                    out.println("<input type=submit class='boton' value='Crear contenido'>");
                                    out.println("<select class = 'campos' id = 'preguntaTipo'>");
                                    out.println("<option value = 'abierta'>Abierta</option>");
                                    out.println("<option value = 'multiOpcion'>Multi-Opción</option>");
                                    out.println("</select>");
                                    out.println("<button onclick = 'anadirPreguntas()' class = 'boton' id = 'anade' type = 'button'>Crear pregunta</button>");
                                    out.println("<p id= 'anadeHere'></p>");
                                out.println("</form>");
                            out.println("</section>");
                        }
                    }
               }
            %>
            

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

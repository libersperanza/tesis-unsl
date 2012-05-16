<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="layout" content="main"/>
<title>Lista de estructuras</title>
</head>
<body>
  <div class="body">
  <h1>Opciones de la implementacion basica</h1>
  <h2>Llenado del Hash de Categorias</h2>
  <g:form controller="basicImplementation" action="fillCategs">
	<div><label for="file_name_cat">Nombre de Archivo:</label><input type="text" id="file_name_cat" name="file_name" value="../../test_data/categs.csv"/></div>
  	<div><label for="separator_cat">Separador de campos:</label><input type="text" id="separator_cat" name="separator" value=";" /></div>
  	<g:submitButton name="cargar" value="Cargar Archivo"></g:submitButton><g:link controller="basicImplementation" action="listCategs">Listar</g:link>
  </g:form>
  <h2>Inicializacion de pivotes</h2>
  <g:form controller="basicImplementation" action="fillPivotes">
	<div><label for="file_name_piv">Nombre de Archivo:</label><input type="text" id="file_name_piv" name="file_name" value="../../test_data/Pivotes001.csv"/></div>
  	<div><label for="separator_piv">Separador de campos:</label><input type="text" id="separator_piv" name="separator" value=";" /></div>
  	<div><label for="cant_piv">Cantidad de pivotes:</label><input type="text" id="cant_piv" name="cant" /></div>
	<g:submitButton name="cargar" value="Cargar Archivo"></g:submitButton><g:link controller="basicImplementation" action="listPivotes">Listar</g:link>
  </g:form>
  <h2>Calculo de firmas y Creacion de archivo de ITems</h2>
  <g:form controller="basicImplementation" action="createSignatures">
	<div><label for="file_name_it">Nombre de Archivo:</label><input type="text" id="file_name_it" name="file_name" value="../../test_data/Items001.csv"/></div>
  	<div><label for="separator_it">Separador de campos:</label><input type="text" id="separator_it" name="separator" value=";" /></div>
	<g:submitButton name="calcular" value="Calcular Firmas"></g:submitButton>
  </g:form>
  </div>
</body>
</html>
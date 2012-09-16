<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="layout" content="main"/>
<title>Lista de estructuras</title>
</head>
<body>
  <div class="body">
  <h1>Parametrización del índice</h1>
  <g:form controller="basicImplementation" action="initIndex">
	<div><label for="file_name_cat">Nombre de Archivo de Categorías:</label><input type="text" id="file_name_cat" name="file_name_cat" lenght="100" value="./test_data/categs.csv"/></div>
	<div><label for="file_name_piv">Nombre de Archivo de Pivotes:</label><input type="text" id="file_name_piv" name="file_name_piv" lenght="100" value="./test_data/Pivotes001.csv"/></div>
  	<div><label for="cant_piv">Cantidad de pivotes:</label><input type="text" id="cant_piv" name="cant" /></div>
	<div><label for="file_name_it">Nombre de Archivo de Items:</label><input type="text" id="file_name_it" name="file_name_it" lenght="100" value="./test_data/items.csv"/></div>
  	<div><label for="separator">Separador de campos para todos los archivos:</label><input type="text" id="separator" name="separator" value=";" /></div>
	<g:submitButton name="calcular" value="Inicializar"></g:submitButton>
  </g:form>
  <g:link controller="basicImplementation" action="listCategs">Listar Categorías</g:link>
  <g:link controller="basicImplementation" action="listPivotes">Listar Pivotes</g:link>
  <g:link controller="basicImplementation" action="listItems">Listar items de una categoria</g:link>
  
  </div>
</body>
</html>
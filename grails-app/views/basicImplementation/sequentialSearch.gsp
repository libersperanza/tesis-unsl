<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="layout" content="layouts"/>
<title>Search</title>
</head>
<body>
  <div class="body">
  <g:link controller="basicImplementation" action="index">Volver al Menu</g:link>
  <hr>
  ${info}
  <g:if test="${itemsFound}">
    <g:each var="elem" in="${itemsFound}">
	<p><label><b>Id: </b></label>${elem?.itemId}; <label><b>Title: </b></label>${elem?.itemTitle}</p>
  </g:each>
  <hr>
  <g:link controller="basicImplementation" action="listItems">Volver al Menu</g:link>

  </g:if>
  <g:else>
  <h1>Listar items por categoría</h1>
	  <g:form controller="basicImplementation" action="sequentialSearch">
		<p class="ch-form-row ch-form-required"><label for="file_name_cat">Categoria:</label><input type="text" id="categ" name="categ" value=""/></p> 
		<p class="ch-form-row ch-form-required"><label for="file_name_title">Title:</label><input type="text" id="itemTitle" name="itemTitle" value=""/></p>
		<p class="ch-form-row ch-form-required"><label for="file_name_radio">Radio:</label><input type="text" id="radio" name="radio" value=""/></p>  
	  	<p class="ch-form-actions">
	  	<g:submitButton name="buscar" class="ch-btn" value="Buscar Items"></g:submitButton>
	  	</p>
	  </g:form>
<hr>
  <g:link controller="basicImplementation" action="index">Volver al Menu</g:link>
  
  </g:else>
  </div>
</body>
</html>
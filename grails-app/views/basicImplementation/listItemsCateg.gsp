<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="layout" content="layouts"/>
<title>Search</title>
</head>
<body>
  <div class="body">
 
  <g:if test="${itemsFound}">
   <g:link controller="basicImplementation" action="listItemCategForm">Volver al Menu</g:link>
    <g:each var="elem" in="${itemsFound}">
	<div>${elem}</div>
  </g:each>
    <g:link controller="basicImplementation" action="listItemCategForm">Volver al Menu</g:link>
  </g:if>
  <g:else>
      <h1>List Items</h1>
      <hr>
	  <g:form controller="basicImplementation" action="listItemCateg">
		<p class="ch-form-row ch-form-required"><label for="file_name_cat">Categoria:</label><input type="text" id="categ" name="categ" value=""/></p>		
		<p class="ch-form-actions"> 
	  	<g:submitButton class="ch-btn" name="buscar" value="Buscar Items"></g:submitButton>
	  	</p>
	  </g:form>
<hr>
  <g:link controller="basicImplementation" action="index">Volver al Menu</g:link>
  
  </g:else>
  </div>
</body>
</html>
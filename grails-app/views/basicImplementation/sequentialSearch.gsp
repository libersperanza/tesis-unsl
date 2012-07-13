<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="layout" content="main"/>
<title>Search</title>
</head>
<body>
  <div class="body">
  <g:link controller="basicImplementation" action="index">Volver al Menu</g:link>
  ${info}
  <g:if test="${itemsFound}">
    <g:each var="elem" in="${itemsFound?}">
	<div>${elem}</div>
  </g:each>
  </g:if>
  <g:else>
	  <g:form controller="basicImplementation" action="sequentialSearch">
		<div><label for="file_name_cat">Categoria:</label><input type="text" id="categ" name="categ" value=""/></div> 
		<div><label for="file_name_title">Title:</label><input type="text" id="itemTitle" name="itemTitle" value=""/></div>
		<div><label for="file_name_radio">Radio:</label><input type="text" id="radio" name="radio" value=""/></div>  
	  	<g:submitButton name="buscar" value="Buscar Items"></g:submitButton>
	  </g:form>

  <g:link controller="basicImplementation" action="index">Volver al Menu</g:link>
  
  </g:else>
  </div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="layout" content="main"/>
<title>Lista de estructuras</title>
</head>
<body>
  <div class="body">
  <g:link controller="basicImplementation" action="index">Volver al Menu</g:link>
  <div>${tit}</div>
  <g:each var="elem" in="${lista?}">
	<div>${elem}</div>
  </g:each>
  <g:link controller="basicImplementation" action="index">Volver al Menu</g:link>
  </div>
</body>
</html>
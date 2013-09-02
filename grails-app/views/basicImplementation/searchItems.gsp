<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="layout" content="layouts"/>
<title>Search</title>
</head>
<body>
  <div class="body">
  <h1>Search items - algorithm pivots</h1>  

  <g:if test="${itemsFound}">
  <g:link controller="basicImplementation" action="searchItems">Volver al Menu</g:link>
  <hr>
  <label>Results: ${itemsFound.size()}</label>
    <g:each var="elem" in="${itemsFound?}">
	<p><label><b>Id: </b></label>${elem?.itemId}; <label><b>Title: </b></label>${elem?.itemTitle}</p>
  </g:each>
  <g:link controller="basicImplementation" action="searchItems">Volver al Menu</g:link>
  </g:if>
  
  <g:else>
      
      <hr>
	  <g:form controller="basicImplementation" action="${params.method=='knn'?'knnSearch':'rankSearch'}">
		<p class="ch-form-row ch-form-required"><label for="file_name_cat">Categoria:</label><input type="text" id="categ" name="categ" value=""/></p> 
		<p class="ch-form-row ch-form-required"><label for="file_name_title">Title:</label><input type="text" id="itemTitle" name="itemTitle" value=""/></p>
		<p class="ch-form-row ch-form-required"><label>${params.method=='knn'?'kNeighbors':'Radio'}:</label><input type="text" name="${params.method=='knn'?'neighbors':'radio'}" value=""/> 
    </p> 
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
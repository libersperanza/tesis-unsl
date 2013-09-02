<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link rel="stylesheet" type="text/css"
	href="https://a248.e.akamai.net/secure.mlstatic.com/org-img/ch/ui/0.11/chico-mesh.min.css" />
<title>Lista de estructuras</title>
</head>
<body>
	<div class="body">
		<h1>Parametrización del índice</h1>
		<hr>
		<g:form controller="basicImplementation" action="initIndex">
			<h1>Modo</h1>
			<p class="ch-form-row ch-form-required">
				<g:radio name="initMode" id="load" value="load" checked="checked"></g:radio><label for="load"> Usar datos precalculados</label>
			</p>
			<p class="ch-form-row ch-form-required">
				<g:radio name="initMode" id="init" value="init"></g:radio><label for="init"> Inicializar datos</label>
			</p>
			<hr>
			<h1>Estrategia</h1>
			<p class="ch-form-row ch-form-required">
				<g:radio name="pivotStrategy" id="random_samePivotes" value="random_samePivotes" checked="checked"/>
				<label for="random_samePivotes">Random, mismos pivots para todas las categorias</label>
			</p>
			<p class="ch-form-row ch-form-required">
				<g:radio name="pivotStrategy" id="random_differentPivotes" value="random_differentPivotes" />
				<label for="random_differentPivotes">Random, pivots por categoria</label>
			</p>
			<p class="ch-form-row ch-form-required">
				<g:radio name="pivotStrategy" id="incremental_samePivotes" value="incremental_samePivotes" />
				<label for="incremental_samePivotes">Incremental, mismos pivots para todas las categoras</label>
			</p>
			<p class="ch-form-row ch-form-required">
				<g:radio name="pivotStrategy" id="incremental_differentPivotes" value="incremental_differentPivotes"/>
				<label for="incremental_differentPivotes">Incremental, pivots por categoria</label>
			</p>
			<hr>
			<p class="ch-form-row ch-form-required">
				<label>Cantidad de pivotes:</label><input
					type="text" name="pivotsQty" value="10" />
			</p>
			
			<g:submitButton class="ch-btn" name="calcular" value="Inicializar"></g:submitButton>
		</g:form>
		<hr>
		<g:link controller="basicImplementation" action="listCategs">Listar Categorías </g:link>
		|
		<g:link controller="basicImplementation" action="listPivotes">Listar Pivotes </g:link>
		|
		<g:link controller="basicImplementation" action="listItemCategForm">Listar items de una categoria </g:link>
		|
		<g:link controller="basicImplementation" action="listItems">Busqueda secuencial </g:link>
		|
		<g:link controller="basicImplementation" action="searchItems" params="['method':'rank']">Busqueda por rango </g:link>
		|
		<g:link controller="basicImplementation" action="searchItems" params="['method':'knn']">Busqueda por knn </g:link>

	</div>
	<script type="text/javascript"
		src="http://code.jquery.com/jquery-1.9.0.min.js"></script>
	<script type="text/javascript">
	var loading;
	loading = 	$("<a>").transition({
		content:"....",
	    width:150,
	    height:120
	});
	$(".ch-btn").bind("click",function(){
		loading.show();
		});
	  
  </script>
</body>
</html>
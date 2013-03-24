package tesis

import java.util.ArrayList;

import tesis.file.manager.TextFileManager;

import org.codehaus.groovy.grails.web.json.JSONObject


import tesis.data.CategDto
import tesis.data.ItemDto
import tesis.data.ItemSignature;
import tesis.file.manager.RandomAccessFileManager
import tesis.utils.Utils;
import org.codehaus.groovy.grails.commons.ConfigurationHolder


class BasicImplementationController
{
	IndexManager mgr
	SearchService searchService
	
	def index =	{
		render(view:"index") 
	} 

	def initIndex =
	{	
		log.info("Creando indice con parametros: $params")
		try
		{
			ConfigurationHolder.config.strategy = "${params.pivotStrategy}_${params.pivotsQty?:'5'}"
		println ConfigurationHolder.config.strategy
			mgr = new IndexManager(params.initMode);
		
			render(view:"fillFile", model:[result:"INICIALIZACION CORRECTA - MODO: $params.initMode"])
		}
		catch(Exception e)
		{
			e.printStackTrace()
			render(view:"fillFile", model:[result:e.message])
		}
	}
	def listCategs =
	{
		mgr.categs.printValues()
		render(view:"list", model:[tit:"Categorias",lista:[]])
	}

	def listPivotes =
	{
		render(view:"list", model:[tit:"Pivotes", lista:mgr.pivots])
	}
	def searchItems =
	{ render(view:"searchItems") }
	def listItems =
	{ render(view:"sequentialSearch") }
	def searchItemsCateg =
	{
		int radio = Integer.valueOf(params.radio?:"5")
		String itemTitle = Utils.removeSpecialCharacters(params.itemTitle).toUpperCase()
		def itemsFound = searchService.simpleSearch(itemTitle,params.categ,radio,mgr)
		
		render(view:"searchItems", model:[tit:"Items",itemsFound:itemsFound])
	}

	def sequentialSearch=
	{
		int radio = Integer.valueOf(params.radio?:"5")
		String itemTitle = Utils.removeSpecialCharacters(params.itemTitle).toUpperCase()
		def itemsFound = searchService.sequentialSearch(itemTitle,params.categ,radio,mgr)
		
		render(view:"sequentialSearch", model:[tit:"Items",itemsFound:itemsFound])

	}
	def listItemCategForm = {
		render (view:"listItemsCateg")
		}
	def listItemCateg =
	{
		def itemsFound = searchService.getAllItemsByCateg(mgr, params.categ)
		render(view:"listItemsCateg", model:[tit:"Items",itemsFound:itemsFound])

	}
	def saveData = {
		mgr.createIndexFiles()
	}
	
	def getData = {
		
	}
}
	


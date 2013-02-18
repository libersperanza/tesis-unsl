package tesis

import java.util.ArrayList;

import tesis.file.manager.SimpleFileManager;

import org.codehaus.groovy.grails.web.json.JSONObject


import tesis.data.CategDto
import tesis.data.ItemDto
import tesis.data.ItemSignature;
import tesis.file.manager.RandomAccessFileManager
import tesis.utils.Utils;

class BasicImplementationController
{
	SessionService sessionService
	SearchService searchService
	
	def index =	{
		render(view:"index") 
	} 

	def initIndex =
	{
		int cant = Integer.valueOf(params.cant?:"5")
		//TODO: tomar la estrategia de seleccion de pivotes desde los parámetros
		IndexManager mgr = new IndexManager(params.pivotStrategy, cant);
		try
		{
			sessionService.init()
			sessionService.setIndex(mgr)
			render(view:"fillFile", model:[result:"INICIALIZACION CORRECTA"])
		}
		catch(Exception e)
		{
			e.printStackTrace()
			render(view:"fillFile", model:[result:e.message])
		}
	}
	def listCategs =
	{
		sessionService.getIndex().categs.printValues()
		render(view:"list", model:[tit:"Categorias",lista:[]])
	}

	def listPivotes =
	{
		render(view:"list", model:[tit:"Pivotes", lista:sessionService.getIndex().pivots])
	}
	def searchItems =
	{ render(view:"searchItems") }
	def listItems =
	{ render(view:"sequentialSearch") }
	def searchItemsCateg =
	{
		IndexManager mgr = sessionService.getIndex()
		int radio = Integer.valueOf(params.radio?:"5")
		String itemTitle = Utils.removeSpecialCharacters(params.itemTitle).toUpperCase()
		def itemsFound = searchService.simpleSearch(itemTitle,params.categ,radio,mgr)
		
		render(view:"searchItems", model:[tit:"Items",itemsFound:itemsFound])
	}

	def sequentialSearch=
	{
		IndexManager mgr = sessionService.getIndex()
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
		IndexManager mgr = sessionService.getIndex()
		def itemsFound = searchService.getAllItemsByCateg(mgr, params.categ)
		render(view:"listItemsCateg", model:[tit:"Items",itemsFound:itemsFound])

	}
	def saveData = {
		IndexManager mgr = sessionService.getIndex()
		mgr.createIndexFiles()
	}
	
	def getData = {
		IndexManager mgr = new IndexManager();
	}
}
	


package tesis

import java.util.ArrayList;

import tesis.file.manager.TextFileManager;

import org.codehaus.groovy.grails.web.json.JSONObject


import tesis.data.CategDto
import tesis.data.ItemDto
import tesis.data.ItemSignature;
import tesis.data.PivotDto
import tesis.file.manager.RandomAccessFileManager
import tesis.utils.Utils;
import org.codehaus.groovy.grails.commons.ConfigurationHolder


class BasicImplementationController
{
	
	SearchService searchService
	SessionService sessionService
	
	def index =	{
		render(view:"index") 
	} 

	def initIndex =
	{	
		log.info("Creando indice con parametros: $params")
		try
		{
			ConfigurationHolder.config.strategy = "${params.pivotStrategy}_${params.pivotsQty?:'5'}"
		
			IndexManager mgr = new IndexManager(params.initMode);
			sessionService.init()
			sessionService.setIndex(mgr)
		
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
		int radio = Integer.valueOf(params.radio?:"5")
		String itemTitle = Utils.removeSpecialCharacters(params.itemTitle).toUpperCase()
		def itemsFound = searchService.simpleSearch(itemTitle,params.categ,radio,sessionService.getIndex(), params.method)
		
		render(view:"searchItems", model:[tit:"Items",itemsFound:itemsFound])
	}

	def sequentialSearch=
	{
		int radio = Integer.valueOf(params.radio?:"5")
		String itemTitle = Utils.removeSpecialCharacters(params.itemTitle).toUpperCase()
		def itemsFound = searchService.sequentialSearch(itemTitle,params.categ,radio,sessionService.getIndex())
		
		render(view:"sequentialSearch", model:[tit:"Items",itemsFound:itemsFound])

	}
	def listItemCategForm = {
		render (view:"listItemsCateg")
		}
	def listItemCateg =
	{
		def itemsFound = searchService.getAllItemsByCateg(sessionService.getIndex(), params.categ)
		render(view:"listItemsCateg", model:[tit:"Items",itemsFound:itemsFound])

	}
	def saveData = {
		sessionService.getIndex().createIndexFiles()
		render(view:"fillFile", model:[result:"Save: DONE!! - Mode: ${ConfigurationHolder.config.strategy}"])
	}
	
	def getData = {
		
	}
	def createFilePivotes = {
		TextFileManager fm = new TextFileManager(ConfigurationHolder.config.itemsBaseFileName, ConfigurationHolder.config.textDataSeparator);
		TextFileManager fmCateg = new TextFileManager(ConfigurationHolder.config.categsBaseFileName, ConfigurationHolder.config.textDataSeparator);
		ArrayList<CategDto> listC = new ArrayList<CategDto>()
		
		if(fmCateg.openFile(0))
		{
			CategDto dto;
			while((dto = fmCateg.nextCateg()))
			{
				if(dto){listC.add(dto)}
			}
			fmCateg.closeFile();
		}
		else
		{
			throw new Exception("Error al abrir el archivo")
		}
		Map pv = [:]
		PivotDto pivote 
		Random rand
		if(fm.openFile(0))
		{
				
				while(pivote = fm.nextPivot()){
					
						
						if(!pv?."${pivote.categ}"){
							pv?."${pivote.categ}" = new ArrayList<PivotDto>()
						}
						//if(pv?."${pivote.categ}".size()<50){
							pv?."${pivote.categ}".add(pivote)
						//}
						/*rand = new Random()
						(1..rand.nextInt(2)).each
						{
							fm.nextPivot()
						}
						*/
				
				}
			
		}
		
		def  pList = pv.findAll{ it.value?.size() >= 50}
		
		println "categ con mas de 50 pivots: " + pList.size()
		
		File file2 = new File(ConfigurationHolder.config.pivotsFileName.replaceAll("#strategy#","New"))
		file2.withObjectOutputStream { oos ->
			oos.writeObject(pList)
		}
		println "ok"
	}
	def readPivotes = {
		
		def pivots
		
		File file3 = new File(ConfigurationHolder.config.pivotsFileName.replaceAll("#strategy#","New"))
		file3.withObjectInputStream(getClass().classLoader){ ois ->
			pivots = ois.readObject()
		}
		def p =  pivots.findAll{it.value.size()< 50}

		p.each{
			println it.key
		}
	
		
	}
}
	


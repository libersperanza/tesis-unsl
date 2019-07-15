package tesis

import java.util.ArrayList
import tesis.file.manager.TextFileManager
import tesis.data.CategDto
import tesis.data.PivotDto
import tesis.utils.Utils
import org.codehaus.groovy.grails.commons.ConfigurationHolder


class BasicImplementationController
{
	
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
		
			IndexManager mgr = new IndexManager(params.initMode);
			
			servletContext["index"] = mgr
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
		servletContext["index"].categs.printValues()
		render(view:"list", model:[tit:"Categorias",lista:[]])
	}

	def listPivotes =
	{
		log.info servletContext["index"].pivots
		//render(view:"list", model:[tit:"Pivotes", lista:servletContext["index"].pivots])
		render(view:"list", model:[tit:"Pivotes", lista:[]])
	}
	def searchItems = { 
		render(view:"searchItems") 
	}

	def listItems = { render(view:"sequentialSearch") }
	
	def knnRankSearch =
	{
		try
		{
			int radio = Integer.valueOf(params.radio?:ConfigurationHolder.config.radio)
			int kNeighbors = Integer.valueOf(params.neighbors?:ConfigurationHolder.config.kNeighbors)
			String itemTitle = Utils.removeSpecialCharacters(params.itemTitle).toUpperCase()
			def itemsFound = searchService.knnByRankSearchV2(itemTitle,params.categ,radio,kNeighbors,servletContext["index"])
			if(params.flat=="Y")
			{
				render itemsFound.size//render itemsFound+ "\n"
			}
			else
			{
				render(view:"searchItems", model:[tit:"Items",itemsFound:itemsFound, searchMethod : "${params.method}"])
			}
		}
		catch(Exception e)
		{
			e.printStackTrace()
			render "PARAMS: [title=${params.itemTitle}, categ=${params.categ}] ERROR: $e\n"
		}
	}

	def rankSearch =
	{
		try 
		{
			int radio = Integer.valueOf(params.radio?:ConfigurationHolder.config.radio)
			String itemTitle = Utils.removeSpecialCharacters(params.itemTitle).toUpperCase()
			def itemsFound = searchService.rankSearch(itemTitle,params.categ,radio,servletContext["index"])
			
			if(params.flat=="Y")
			{
				render itemsFound.size//render itemsFound+ "\n"
			}
			else
			{
				render(view:"searchItems", model:[tit:"Items",itemsFound:itemsFound, searchMethod : "${params.method}"])
			}
		}
		catch(Exception e)
		{
			e.printStackTrace()
			render "PARAMS: [title=${params.itemTitle}, categ=${params.categ}] ERROR: $e\n"
		}
		
		
	}

	def sequentialSearch=
	{
		try 
		{
			int radio = Integer.valueOf(params.radio?:"5")
			String itemTitle = Utils.removeSpecialCharacters(params.itemTitle).toUpperCase()
			def itemsFound = searchService.sequentialSearch(itemTitle,params.categ,radio,servletContext["index"])
			
			if(params.flat=="Y")
			{
				render itemsFound.size//render itemsFound+ "\n"
			}
			else
			{
				render(view:"sequentialSearch", model:[tit:"Items",itemsFound:itemsFound])
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace()
			render "PARAMS: [title=${params.itemTitle}, categ=${params.categ}] ERROR: $e\n"
		}
	}
	def listItemCategForm = {
		render (view:"listItemsCateg")
		}
	def listItemCateg =
	{
		try 
		{
			def itemsFound = searchService.getAllItemsByCateg(servletContext["index"], params.categ)
			if(params.flat=="Y")
			{
				render ""//render itemsFound+ "\n"
			}
			else
			{
				
				render(view:"listItemsCateg", model:[tit:"Items",itemsFound:itemsFound])
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace()
			render "PARAMS: [title=${params.itemTitle}, categ=${params.categ}] ERROR: $e\n"
		}

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
				pv?."${pivote.categ}".add(pivote)
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
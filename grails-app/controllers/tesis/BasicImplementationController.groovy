package tesis

import java.util.ArrayList
import tesis.file.manager.TextFileManager
import tesis.data.CategDto
import tesis.data.PivotDto
import tesis.data.ItemDto
import tesis.utils.Utils
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import java.net.URLEncoder
import com.sun.xml.internal.bind.v2.util.EditDistance

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
			List itemsFound = searchService.knnByRankSearchV2(itemTitle,params.categ,radio,kNeighbors,servletContext["index"])
			switch(params.response_format) {
				case "empty":
					render "\n"
					break
				case "size":
					render itemsFound.size
					break
				case "ids":
					render itemsFound.collect{it.itemId}.sort().join(",")
					break
				default:
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
			List itemsFound = searchService.rankSearch(itemTitle,params.categ,radio,servletContext["index"])
			
			switch(params.response_format) {
				case "empty":
					render "\n"
					break
				case "size":
					render itemsFound.size
					break
				case "ids":
					render itemsFound.collect{it.itemId}.sort().join(",")
					break
				default:
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
			List itemsFound = searchService.sequentialSearch(itemTitle,params.categ,radio,servletContext["index"])
			
			switch(params.response_format) {
				case "empty":
					render "\n"
					break
				case "size":
					render itemsFound.size
					break
				case "ids":
					render itemsFound.collect{it.itemId}.sort().join(",")
					break
				default:
					render(view:"searchItems", model:[tit:"Items",itemsFound:itemsFound, searchMethod : "${params.method}"])

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
			List itemsFound = searchService.getAllItemsByCateg(servletContext["index"], params.categ)
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
		Map pivotesByCateg = [:]
		PivotDto pivote 
		if(fm.openFile(0))
		{
				
			while(pivote = fm.nextPivot()){
				if(!pivotesByCateg.containsKey(pivote.categ)){
					pivotesByCateg.put(pivote.categ, new ArrayList<PivotDto>())
				}
				pivotesByCateg.get(pivote.categ).add(pivote)
			}
			
		}
		fm.closeFile()
		
		File file2 = new File(ConfigurationHolder.config.pivotsFileName.replaceAll("#strategy#","New"))
		file2.withObjectOutputStream { oos ->
			oos.writeObject(pivotesByCateg)
		}
		println "ok"
	}
	def createFileSearchTitles = {
		
		TextFileManager fm = new TextFileManager(ConfigurationHolder.config.itemsBaseFileName, ConfigurationHolder.config.textDataSeparator);
		Map itemsByCateg = [:]
		if(fm.openFile(0))
		{
			ItemDto item 
			while(item = fm.nextItem()){
				if(["MLA5725", "MLA3025"].contains(item.categ))
				{
					if(!itemsByCateg.containsKey(item.categ)){
						itemsByCateg.put(item.categ,new ArrayList<String>())
					}
					itemsByCateg.get(item.categ).add(item.itemTitle)
				}
			}	
		}
		fm.closeFile()

		itemsByCateg.each{ categ,items ->
			File resFileSearchTitles = new File("./test_data/search_titles/${categ}.txt")
			log.info "Writing results file ${resFileSearchTitles.name}"
			resFileSearchTitles.withWriter{ out ->
				//Obtengo el 10% de la BD
				int sampleSize = items.size()/10 
				Random rand = new Random();
			    for (int i = 0; i < sampleSize; i++) {
			        int randomIndex = rand.nextInt(items.size());
			        String randomTitle =  items.get(randomIndex);
			        items.remove(randomIndex);
			        out.println "${categ}&itemTitle=${URLEncoder.encode(randomTitle, "UTF-8")}"
			    }
			}
		}

		println "ok"
	}
	def readPivotes = {
		
		Map pivots
		
		File file3 = new File(ConfigurationHolder.config.pivotsFileName.replaceAll("#strategy#","New"))
		file3.withObjectInputStream(getClass().classLoader){ ois ->
			pivots = ois.readObject()
		}
		/*def p =  pivots.findAll{it.value.size()< 50}

		p.each{
			println it.key
		}*/
		pivots.keySet().each{
			categ ->
			
			List lengths = pivots.get(categ).collect{it.searchTitle}

			println "[$categ][max:${lengths.max()}][min:${lengths.min()}][avg:${lengths.sum()/lengths.size()}]"
		}
	}

	def editDistanceTime = {
		long id = java.lang.Thread.currentThread().getId();
		log.info("ANTES DE TOMAR TIEMPO CPU")
		long startTimeCPU = tesis.utils.Utils.getCpuTime([id])
		log.info("ANTES DE TOMAR TIEMPO USR")
		long startTimeUser = tesis.utils.Utils.getUserTime([id])
		log.info("ANTES DE CALCULAR DIST")
		int dist = EditDistance.editDistance(params.title1, params.title2)
		log.info("ANTES DE TOMAR TIEMPO CPU")
		long elapsedCPUTime = tesis.utils.Utils.getCpuTime([id]) - startTimeCPU;
		log.info("ANTES DE TOMAR TIEMPO USR")
		long elapsedUserTime = tesis.utils.Utils.getUserTime([id]) - startTimeUser;
		log.info("[DIST: $dist, TIME_CPU: $elapsedCPUTime, TIME_USR: $elapsedUserTime]")

	}
	
	def histogram = {
		log.info params
		log.info params.categ.getClass()
		log.info servletContext["index"].getClass()
		searchService.getHistogramByRadio(params.categ,servletContext["index"],params.percentage?Integer.valueOf(params.percentage):null)
	}
}
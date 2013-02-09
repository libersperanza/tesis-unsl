package tesis

import java.util.ArrayList;

import tesis.file.manager.SimpleFileManager;

import org.codehaus.groovy.grails.web.json.JSONObject

import com.sun.xml.internal.bind.v2.util.EditDistance;

import tesis.data.CategDto
import tesis.data.ItemDto
import tesis.data.ItemSignature;
import tesis.file.manager.RandomAccessFileManager
import tesis.utils.Utils;

class BasicImplementationController
{
	SessionService sessionService
	
	def index =	{
		render(view:"index") 
	} 

	def initIndex =
	{
		IndexManager mgr = new IndexManager(params.file_name_cat, params.file_name_it,
				"./test_data/Items.dat", params.file_name_piv, params.separator);
		try
		{
			println params
			String result = mgr.initIndex(Integer.parseInt(params.cant))
			sessionService.init()
			sessionService.setCategs(mgr.categs)
			sessionService.setPivots(mgr.pivots)
				
			if(!result)
			{
				result = "INICIALIZACION CORRECTA"
			}
			render(view:"fillFile", model:[result:result])
		}
		catch(Exception e)
		{
			e.printStackTrace()
			render(view:"fillFile", model:[result:e.message])
		}
	}
	def listCategs =
	{
		render(view:"list", model:[tit:"Categorias",lista:sessionService.getCategs().getValues()])
	}

	def listPivotes =
	{
		render(view:"list", model:[tit:"Pivotes", lista:sessionService.getPivots()])
	}
	def searchItems =
	{ render(view:"searchItems") }
	def listItems =
	{ render(view:"sequentialSearch") }
	def searchItemsCateg =
	{
		IndexManager mgr = new IndexManager(sessionService.getCategs(),sessionService.getPivots())
		int radio = Integer.valueOf(params.radio?:"5")
	
		def signatures = mgr.searchItemsByCateg(Utils.removeSpecialCharacters(params.itemTitle)?.toUpperCase(), params.categ, radio)
		def itemsFound = null
		RandomAccessFileManager rfm = new RandomAccessFileManager("./test_data/Items.dat")
	
		if (signatures){
			if (rfm.openFile("rw"))
			{
				itemsFound = new ArrayList()

				signatures.each
				{
					def item =  new JSONObject(rfm.getItem(it.itemPosition,it.itemSize))
					def dist = EditDistance.editDistance(Utils.removeSpecialCharacters(params.itemTitle)?.toUpperCase(), item.searchTitle)					
					if(dist < radio)
					{ 
						itemsFound.add(item)
					}					
				}
				rfm.closeFile()
			}
		}
		println "Total de cadidatos: ${signatures?.size()}"
		render(view:"searchItems", model:[tit:"Items",itemsFound:itemsFound])
	}

	def sequentialSearch=
	{
		
		IndexManager mgr = new IndexManager(sessionService.getCategs(),sessionService.getPivots())
		int radio = Integer.valueOf(params.radio?:"5")
		int pos = sessionService.getCategs().search(new CategDto(categName:params.categ,signatures:null))
		def signatures = sessionService.getCategs().get(pos)?.signatures
		def itemsFound = null
		RandomAccessFileManager rfm = new RandomAccessFileManager("./test_data/Items.dat")
		if (signatures){
			if (rfm.openFile("rw"))
			{
				itemsFound = new ArrayList()
			
				signatures.each
				{
					def item =  new JSONObject(rfm.getItem(it.itemPosition,it.itemSize))
				
					def dist = EditDistance.editDistance(Utils.removeSpecialCharacters(params.itemTitle)?.toUpperCase(), item.searchTitle)
					if(dist < radio)
					{	
						itemsFound.add(item)
					}
								
				}
			
				rfm.closeFile()
			}
		}
		render(view:"sequentialSearch", model:[tit:"Items",itemsFound:itemsFound])

	}
	def listItemCategForm = {
		render (view:"listItemsCateg")
		}
	def listItemCateg =
	{
		
		IndexManager mgr = new IndexManager(sessionService.getCategs(),sessionService.getPivots())
	println params.categ
		int pos = sessionService.getCategs().search(new CategDto(categName:params.categ,signatures:null))
		println pos
		def signatures = sessionService.getCategs().get(pos)?.signatures
		println  sessionService.getCategs().get(pos)
		def itemsFound = null
		RandomAccessFileManager rfm = new RandomAccessFileManager("./test_data/Items.dat")
		if (signatures){
			if (rfm.openFile("rw"))
			{
				itemsFound = new ArrayList()
				signatures.each
				{
					def item =  new JSONObject(rfm.getItem(it.itemPosition,it.itemSize))							
					itemsFound.add(item)					
								
				}
				rfm.closeFile()
			}
		}
		println "Total de items en la categ ${params.categ} : ${itemsFound?.size()}"
		render(view:"listItemsCateg", model:[tit:"Items",itemsFound:itemsFound])

	}
	def saveData = {
		SimpleFileManager fm = new SimpleFileManager("./test_data/signatures.dat","\n")
		def categs = sessionService.getCategs()
		def pivots = sessionService.getPivots()
		if(fm.openFileW())
		{
			categs.getValues().each{				
				fm.insertObject(it)				
			}
			fm.closeFileW()
		}
		fm = new SimpleFileManager("./test_data/pivotes.dat","\n")
		if(fm.openFileW()){
			pivots?.ALL?.each { 
				fm.insertObject(it)
			}
			fm.closeFileW()
		}
	}
	
	def getData = {
		def start = System.currentTimeMillis()
		SimpleFileManager fm = new SimpleFileManager("./test_data/signatures.dat", "\n");
		ArrayList categs = new ArrayList<CategDto>()
		ArrayList signatures
		def i = 0
		if(fm.openFile(0))
		{
			String currentObj
			def obj
			CategDto categ
			ItemSignature signature
			while(currentObj = fm.nextLine()){
				try{
				obj = new JSONObject(currentObj)
				//println obj
				signatures = new ArrayList<ItemSignature>()
				for(o in obj?.signatures){
	
					signature = new ItemSignature( o?.dists, Long.valueOf(o.itemPosition), o?.itemSize)
					signatures.add(signature)
				}				
				categ = new CategDto(obj.categName,signatures)
				
				categs.add(categ)		
				}catch(Exception e){
				i++
				}		
			}
			println "categ con error: " + i
			sessionService.setCategs(categs)
			fm.closeFile()
		}
		fm = new SimpleFileManager("./test_data/pivotes.dat", "\n");
		i = 0
		def pivs = []
		if(fm.openFile(0))
		{
			String currentObj
			def obj
			CategDto categ
			ItemSignature signature
			while(currentObj = fm.nextLine()){
				try{
				obj = new JSONObject(currentObj)
				pivs.add(new ItemDto(itemId:obj.itemId,categ:obj.categ,itemTitle:obj.itemTitle,searchTitle:obj.searchTitle))
				}catch(Exception e){
					i++
				}
			}
			println "pivotes con error: " + i
			sessionService.setPivots(["ALL":pivs])	
			println sessionService.getPivots()
			fm.closeFile()
		}
		println "Tiempo total de procesamiento de archivo: "+ System.currentTimeMillis() - start
	}
}
	


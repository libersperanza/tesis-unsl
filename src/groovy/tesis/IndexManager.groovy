/**
 * 
 */
package tesis

import grails.converters.JSON
import java.util.Map;

import tesis.data.CategDto;
import tesis.data.ItemDto;
import tesis.data.ItemSignature;
import tesis.data.PivotDto
import tesis.file.manager.RandomAccessFileManager;
import tesis.file.manager.SimpleFileManager;
import tesis.structure.CategsHash;
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * @author lsperanza
 *
 */
class IndexManager
{
	CategsHash categs = null;
	//Contiene la categoria y la lista de pivotes asociada.
	//Si se utiliza el mismo conjunto de pivotes p/todas
	//las categorias, se carga el par "ALL",[lista_pivotes]
	Map pivots = null;

	public IndexManager(String pivotStrategy, int cantPivots)
	{
		createCategsHash(createCategListFromFile());
		
		if("RANDOM".equals(pivotStrategy))
		{
			createPivots(cantPivots)
		}
		else if ("BY_CATEG_RND".equals(pivotStrategy))
		{
			//TODO: Implementar random por categoría
		}
		else
		{
			//TODO: la otra implementacion
		}
		
		createSignatures()
	}
	public IndexManager()
	{
		long startTime = System.currentTimeMillis()
		SimpleFileManager fm = new SimpleFileManager(ConfigurationHolder.config.categsFileName,"\n")
		ArrayList categsList = new ArrayList<CategDto>()
		
		if(fm.openFile(0))
		{
			String currentObj
			def obj
			CategDto categ
			ItemSignature signature
			while(currentObj = fm.nextLine())
			{
				obj = new JSONObject(currentObj)
					ArrayList signatures = new ArrayList<ItemSignature>()
					for(o in obj.signatures){
		
						signature = new ItemSignature( o.dists, Long.valueOf(o.itemPosition), o.itemSize)
						signatures.add(signature)
					}				
					categ = new CategDto(obj.categName,signatures)
					
					categsList.add(categ)				
			}
			fm.closeFile()
			createCategsHash(categsList)
		}
		else
		{
			throw new Exception("Error al abrir el archivo para lectura ${ConfigurationHolder.config.categsFileName}")
		}
		
		fm = new SimpleFileManager(ConfigurationHolder.config.pivotsFileName,"\n")
		pivots = [:]
		if(fm.openFile(0))
		{
			String currentObj
			JSONObject pivotMap
			CategDto categ
			ItemSignature signature
			while(currentObj = fm.nextLine()){
				pivotMap = new JSONObject(currentObj)
			}
			fm.closeFile()
			pivotMap.each
			{
				key,value ->
				pivots.put(key.toString(), value.collect{new PivotDto(itemId:it.itemId,categ:it.categ,searchTitle:it.searchTitle)})
			}
			
		}
		else
		{
			throw new Exception("Error al abrir el archivo para escritura ${ConfigurationHolder.config.pivotsFileName}")
		}
		println "Creacion de indice desde archivo: ${System.currentTimeMillis()-startTime} ms"
	}
	
	private createCategListFromFile()
	{
		long startTime = System.currentTimeMillis()
		SimpleFileManager fm = new SimpleFileManager(ConfigurationHolder.config.categsFileName, ConfigurationHolder.config.textDataSeparator);
		ArrayList<CategDto> list=new ArrayList<CategDto>()
		
		if(fm.openFile(0))
		{
			CategDto dto;
			while((dto = fm.nextCateg()))
			{
				if(dto){list.add(dto)}
			}
			fm.closeFile();
		}
		else
		{
			throw new Exception("Error al abrir el archivo")
		}
		println "Lectura de categorias desde archivo: ${System.currentTimeMillis()-startTime} ms"
		return list
	}
	
	private void createCategsHash(ArrayList<CategDto> list)
	{
		long startTime = System.currentTimeMillis()
		categs = new CategsHash(list.size(), 0.4)
		for(CategDto c:list)
		{
			if(categs.add(c)==-1)
			{
				println "No se pudo agregar la categoria: ${c.getCategName()}"
			}
		}
		println "Cargado de categorias en el hash: ${System.currentTimeMillis()-startTime} ms"
	}
	private void createPivots(int cant)
	{
		pivots = [:]
		long startTime = System.currentTimeMillis()
		SimpleFileManager fm = new SimpleFileManager(ConfigurationHolder.config.itemsBaseFileName, ConfigurationHolder.config.textDataSeparator);
		String res;
		if(cant <= 50)
		{
			if(fm.openFile(0))
			{
				def pivs = []
				(1..cant).each
				{
					pivs.add(fm.nextPivot())
					Random rand = new Random()
					(1..rand.nextInt(5)).each
					{
						fm.nextPivot()
					}
				}
				fm.closeFile()
				pivots.put("ALL",pivs)
				println "${cant} pivotes cargados con exito"
			}
			else
			{
				throw new Exception("Error al abrir el archivo")
			}
		}
		else
		{
			throw new Exception("Cantidad de pivotes mayor a la permitida (50)")
		}
		println "Creacion de pivotes: ${System.currentTimeMillis()-startTime} ms"
	}
	
	private void createSignatures()
	{
		long startTime = System.currentTimeMillis()
		def noCateg=0
		//def listCategsNoFound = []
		String res
		SimpleFileManager fm = new SimpleFileManager(ConfigurationHolder.config.itemsBaseFileName, ConfigurationHolder.config.textDataSeparator);
		RandomAccessFileManager rfm = new RandomAccessFileManager(ConfigurationHolder.config.itemsDataFileName)

		if(fm.openFile(0))
		{
			if(rfm.openFile("rw"))
			{
				rfm.resetFile();
				ItemDto curItem
				while(curItem = fm.nextItem())
				{
					ItemSignature sig = new ItemSignature(curItem.getSearchTitle(), getPivotsForCateg(curItem.getCateg()))
					CategDto catForSearch = new CategDto(categName:curItem.categ,signatures:null)
					int pos = categs.search(catForSearch)
					
					if (categs.get(pos).equals(catForSearch)){
						sig.setItemPosition(rfm.insertItem(curItem))
						sig.setItemSize(curItem.toString().length())
						categs.get(pos).getSignatures().add(sig)
					}else{
						//listCategsNoFound.add(catForSearch.categName)
						noCateg++
					}
				}
				rfm.closeFile()
				println "Items no almacenados por categoria invalida: " + noCateg
				//println (listCategsNoFound as Set)
			}
			else
			{
				throw new Exception("Error al abrir el archivo para lectura/escritura")
			}
			fm.closeFile()
		}
		else
		{
			throw new Exception("Error al abrir el archivo ${ConfigurationHolder.config.itemsBaseFileName}")
		}
		
		println "Creacion de firmas: ${System.currentTimeMillis()-startTime} ms"
	}
	
	def getPivotsForCateg(String categName)
	{
		def ret = pivots.get(categName)
		if(!ret)
		{
			ret = pivots.get("ALL")
		}
		return ret
	}
	
	def createIndexFiles()
	{
		long startTime = System.currentTimeMillis()
		
		SimpleFileManager fm = new SimpleFileManager(ConfigurationHolder.config.categsFileName,"\n")
		
		if(fm.openFileW())
		{
			categs.getValues().each
			{
				fm.insertObject(it.toJSON())
			}
			fm.closeFileW()
		}
		else
		{
			throw new Exception("Error al abrir el archivo para escritura ${ConfigurationHolder.config.categsFileName}")
		}
		fm = new SimpleFileManager(ConfigurationHolder.config.pivotsFileName,"\n")
		if(fm.openFileW()){
			Map pivs = [:]
			
			pivots.each 
			{
				key,value ->
				pivs.put(key, value.collect {it.toJSON()})
			}
			
			fm.insertObject(pivs as JSON)
			
			fm.closeFileW()
		}
		else
		{
			throw new Exception("Error al abrir el archivo para escritura ${ConfigurationHolder.config.pivotsFileName}")
		}
		println "Creacion de archivos de categs y pivots: ${System.currentTimeMillis()-startTime} ms"
	}
}

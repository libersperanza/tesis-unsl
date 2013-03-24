/**
 * 
 */
package tesis

import grails.converters.JSON
import java.util.Map;
import java.util.List;
import tesis.data.CategDto;
import tesis.data.ElementsPairs
import tesis.data.ItemDto;
import tesis.data.ItemSignature;
import tesis.data.PivotDto
import tesis.file.manager.RandomAccessFileManager;
import tesis.file.manager.TextFileManager;
import tesis.structure.CategsHash;
import tesis.utils.Utils;
import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import com.sun.xml.internal.bind.v2.util.EditDistance;
/**
 * @author lsperanza
 *
 */
class IndexManager
{
	Log log = LogFactory.getLog(IndexManager.class.getName())
	CategsHash categs = null;
	String strategy
	Integer pivotsQty
	//Contiene la categoria y la lista de pivotes asociada.
	//Si se utiliza el mismo conjunto de pivotes p/todas
	//las categorias, se carga el par "ALL",[lista_pivotes]
	HashMap<String,PivotDto> pivots = null;

	public IndexManager(String initMode)
	{
		long startTime = System.currentTimeMillis()
		strategy = ConfigurationHolder.config.strategy.split("_")[0]
		pivotsQty = Integer.valueOf(ConfigurationHolder.config.strategy.split("_")[2])
		
		if(initMode == "load"){
			
			File file = new File(ConfigurationHolder.config.categsFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))
			//TODO: Leer la cantidad de categs del archivo
			ArrayList<CategDto> categsList = new ArrayList<CategDto>(13071)
			file.withObjectInputStream(getClass().classLoader){ ois ->
				for(int i=0;i < 13071;i++)
				{	try{
						categsList.add((CategDto)ois.readObject())
					}catch(EOFException e){}
				}
			}			
			createCategsHash(categsList)
	
			File file2 = new File(ConfigurationHolder.config.pivotsFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))
			file2.withObjectInputStream(getClass().classLoader){ ois ->
				try{
					pivots = (HashMap<String,PivotDto>)ois.readObject()
				}catch(EOFException e){}
			}
			
			log.info "index_creation_from_file|${System.currentTimeMillis()-startTime}"
		}else{
			createCategsHash(createCategListFromFile());
			
			if("random".equals(strategy))
			{
				createPivots()
			}
			else if ("BY_CATEG_RND".equals(strategy))
			{
				//TODO: Implementar random por categoría, NO HACE FALTA
			}else{
				createIncrementalPivots()
			}
			
			createSignatures()
			log.info "index_creation|${System.currentTimeMillis()-startTime}"
		}
	}
	
	private createCategListFromFile()
	{
		long startTime = System.currentTimeMillis()
		TextFileManager fm = new TextFileManager(ConfigurationHolder.config.categsBaseFileName, ConfigurationHolder.config.textDataSeparator);
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
		log.info "reading_categs_from_file|${System.currentTimeMillis()-startTime}"
		return list
	}
	
	private void createCategsHash(ArrayList<CategDto> list)
	{
		long startTime = System.currentTimeMillis()
		categs = new CategsHash(list.size(), 0.4)
		for(CategDto c:list)
		{
			categs.add(c)
			/*if(categs.add(c)==-1)
			{
				log.info "No se pudo agregar la categoria: ${c.categName}"
			}*/
		}
		//log.info "Cargado de categorias en el hash: ${System.currentTimeMillis()-startTime} ms"
	}
	private void createPivots()
	{
		pivots = [:]
		long startTime = System.currentTimeMillis()
		TextFileManager fm = new TextFileManager(ConfigurationHolder.config.itemsBaseFileName, ConfigurationHolder.config.textDataSeparator);
		String res;
		if(pivotsQty <= 50)
		{
			if(fm.openFile(0))
			{
				def pivs = []
				(1..pivotsQty).each
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
				//log.info "${cant} pivotes cargados con exito"
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
		log.info "pivot_creation|${System.currentTimeMillis()-startTime}"
	}
	private void createIncrementalPivots(){
		def (aQty,nQty) = [ConfigurationHolder.config.elementsPairs,ConfigurationHolder.config.sizeSample]
		pivots = [:]
		ArrayList<ElementsPairs> elemPairs
		long startTime = System.currentTimeMillis()
		TextFileManager fm = new TextFileManager(ConfigurationHolder.config.itemsBaseFileName, ConfigurationHolder.config.textDataSeparator);
		String res;
		def pCandidate
		def max, min	
		
		if(pivotsQty <= 50)
		{
			if(fm.openFile(0))
			{
				def pivs = []
				/** el primer pivot es elegido al azar*/
				pCandidate = getRandomPivot(fm)
				pivs.add(pCandidate)
				elemPairs = getElementsPairs(aQty,pivotsQty,pCandidate,fm)
				
				max = getMediaD(elemPairs,pivs, null)
				
				while(pivs.size() < pivotsQty){
					pCandidate = getRandomPivot(fm)
					while(pivs?.find{it.itemId == pCandidate.itemId}){						
						pCandidate = getRandomPivot(fm)
					}
					min = getMediaD(elemPairs,pivs,pCandidate)
					if(min>max){
						max=min
						for (e in elemPairs){
							e.addDistance(pCandidate)
						}
						pivs.add(pCandidate)
					}
				}				
				fm.closeFile()
				pivots.put("ALL",pivs)
				//log.info "${pivotsQty} pivotes cargados con exito"
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
		log.info "pivot_creation|${System.currentTimeMillis()-startTime}"
	}
	
	private void createSignatures()
	{
		long startTime = System.currentTimeMillis()
		def noCateg=0
		
		String res
		TextFileManager fm = new TextFileManager(ConfigurationHolder.config.itemsBaseFileName, ConfigurationHolder.config.textDataSeparator);
		RandomAccessFileManager rfm = new RandomAccessFileManager(ConfigurationHolder.config.itemsDataFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))

		if(fm.openFile(0))
		{
			if(rfm.openFile("rw"))
			{
				rfm.resetFile();
				ItemDto curItem
				while(curItem = fm.nextItem())
				{
					ItemSignature sig = new ItemSignature(curItem.getSearchTitle(), getPivotsForCateg(curItem.getCateg()))
					CategDto catForSearch = new CategDto(categName:curItem.categ,itemQty:0,signatures:null)
					int pos = categs.search(catForSearch)
					if (categs.get(pos).equals(catForSearch)){
						sig.itemPosition = rfm.insertItem(curItem)
						sig.itemSize = curItem.toJSON().toString().length()
						categs.get(pos).signatures.add(sig)
					}else{
						
						noCateg++
					}
				}
				rfm.closeFile()
				//log.info "Items no almacenados por categoria invalida: " + noCateg
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
		
		log.info "signature_creation|${System.currentTimeMillis()-startTime}"
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

		CategDto virgin = new CategDto(categName:ConfigurationHolder.config.VIRGIN_CELL,itemQty:0,signatures:null);
		CategDto used = new CategDto(categName:ConfigurationHolder.config.USED_CELL,itemQty:0,signatures:null);
		List<CategDto> listCategs = categs.getValues()

		File file = new File(ConfigurationHolder.config.categsFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))
		file.withObjectOutputStream { oos ->
			for(int i=0;i < listCategs.size; i++)
			{
				if((!listCategs[i].equals(virgin))&&(!listCategs[i].equals(used)))
				{
					oos.writeObject(listCategs[i])
				}
			}
		}
		
		File file2 = new File(ConfigurationHolder.config.pivotsFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))
		file2.withObjectOutputStream { oos ->
			oos.writeObject(pivots)
		}

		log.info "index_file_creation|${System.currentTimeMillis()-startTime}"
	}
	
	def getRandomPivot(fm){
		Random rand = new Random()
		(1..rand.nextInt(5)).each
		{
			fm.nextPivot()
		}
		return fm.nextPivot()
	}
	
	def getElementsPairs(pairsQty,pivotsQty,pivot,file){
		ArrayList<ElementsPairs> pairs = new ArrayList<ElementsPairs>(pairsQty)
		ElementsPairs pair
		for (int i=0; i<pairsQty;i++){
			pair = new ElementsPairs()
			pair.a = getRandomPivot(file)
			pair.b = getRandomPivot(file)
			while (pair.a.itemId == pair.b.itemId){
				pair.b = getRandomPivot(file)
				log.info "mismo random en el par"
			}
			pair.initDist(pivotsQty,pivot)			
			pairs.add(pair)
		}
		return pairs
	}
	def getMediaD(pairs,pivots,pivotCandidate){
		def max
		def value
		def media = 0
		List a
		List b
		for (pair in pairs){
			a = pair.aDists.clone()
			b = pair.bDists.clone()
			if(pivotCandidate){
				a[Utils.firtsFree(pair.aDists)] = EditDistance.editDistance(pair.a.searchTitle, pivotCandidate.searchTitle)
				b[Utils.firtsFree(pair.aDists)] = EditDistance.editDistance(pair.b.searchTitle, pivotCandidate.searchTitle)
			}
			max = (a[0]-b[0]).abs()
			for(int i=1; i< a.size() && a[i]!=-1; i++){
				value = (a[i]-b[i]).abs()
				if(value>max){
					max=value
				}
			}
			media += max			
		}		
		return media/pairs.size()
	}
}
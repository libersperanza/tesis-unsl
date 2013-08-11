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
	//Contiene la categoria y la lista de pivotes asociada.
	//Si se utiliza el mismo conjunto de pivotes p/todas
	//las categorias, se carga el par "ALL",[lista_pivotes]
	HashMap<String,PivotDto> pivots = null;
	def pivotsByCateg = null;

	public IndexManager(String initMode)
	{
		long startTime = System.currentTimeMillis()
		def strategyParams = ConfigurationHolder.config.strategy.split("_")
		String strategy = strategyParams[0]
		String pivotSelection = strategyParams[1]
		int pivotsQty = Integer.valueOf(strategyParams[2])

		
		if(initMode == "load"){
			
			File file = new File(ConfigurationHolder.config.categsFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))
			ArrayList<CategDto> categsList
			file.withObjectInputStream(getClass().classLoader){ ois ->
				int size = (Integer)ois.readObject()
				categsList = new ArrayList<CategDto>(size)
				for(int i=0;i < size ;i++)
				{	
					categsList.add((CategDto)ois.readObject())
				}
			}			
			createCategsHash(categsList)
	
			File file2 = new File(ConfigurationHolder.config.pivotsFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))
			file2.withObjectInputStream(getClass().classLoader){ ois ->
				pivots = (HashMap<String,PivotDto>)ois.readObject()
			}
			
			log.info "$ConfigurationHolder.config.strategy|index_creation_from_file|${System.currentTimeMillis()-startTime}"
		}else{
			createCategsHash(createCategListFromFile());
			
			if("random".equals(strategy))
			{
				createPivots(pivotSelection,pivotsQty)
			}
			else if ("BY_CATEG_RND".equals(strategy))
			{
				//TODO: Implementar random por categoría, NO HACE FALTA
			}else{
				createIncrementalPivots(pivotSelection,pivotsQty)
			}
			
			createSignatures()
			log.info "$ConfigurationHolder.config.strategy|index_creation|${System.currentTimeMillis()-startTime}"
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
		log.info "$ConfigurationHolder.config.strategy|reading_categs_from_file|${System.currentTimeMillis()-startTime}"
		return list
	}
	
	private void createCategsHash(ArrayList<CategDto> list)
	{
		long startTime = System.currentTimeMillis()
		categs = new CategsHash(list.size(), 0.4)
		for(CategDto c:list)
		{
			categs.add(c)
		}
	}
	private void createPivots(String pivotSelection, int pivotsQty)
	{
		pivots = [:]
		long startTime = System.currentTimeMillis()
		TextFileManager fm = new TextFileManager(ConfigurationHolder.config.itemsBaseFileName, ConfigurationHolder.config.textDataSeparator);
		String res;
		if(pivotsQty <= 30)
		{
			if(fm.openFile(0))
			{
				if("differentPivotes" == pivotSelection)
				{
					TextFileManager fm2 = new TextFileManager(ConfigurationHolder.config.categsBaseFileName, ConfigurationHolder.config.textDataSeparator);					
					if(fm2.openFile(0))
					{
						CategDto dto;
						while((dto = fm2.nextCateg()))
						{
							ArrayList<PivotDto> p = new ArrayList<PivotDto>(30)
							pivots.put(dto.categName, p)
						}
						fm2.closeFile();
					}
					else
					{
						throw new Exception("Error al abrir el archivo")
					}
					while(!pivots.every{it.value.size == 30} || pivots.isEmpty())
					{
						def piv = fm.nextPivot()
						if(pivots.get(piv.categ))
						{
							if(pivots.get(piv.categ).size < 30)
							{
								pivots.get(piv.categ).add(piv)
							}
							else
							{								
								println  pivots.findAll{it.value.size ==30}.keySet().size()
							}
						}
						else
						{
							ArrayList<PivotDto> p = new ArrayList<PivotDto>(30)
							p.add(piv)
							pivots.put(piv.categ, p)
						}
					}
					pivots.each{ k, v->
						while(v.size > pivotsQty) {
							Random rand = new Random()
							v.remove(rand.nextInt(v.size))
						}
					}
				}
				else
				{
					def pivs = []
					(1..pivotsQty).each
					{
						pivs.add(fm.nextPivot())
						Random rand = new Random()
						(1..rand.nextInt(10)).each
						{
							fm.nextPivot()
						}
					}
					pivots.put("ALL",pivs)
				}
				fm.closeFile()
			}
			else
			{
				throw new Exception("Error al abrir el archivo")
			}
		}
		else
		{
			throw new Exception("Cantidad de pivotes mayor a la permitida (30)")
		}
		log.info "$ConfigurationHolder.config.strategy|pivot_creation|${System.currentTimeMillis()-startTime}"
	}
	private void createIncrementalPivots(String pivotSelection, int pivotsQty){
		def (aQty,nQty) = [ConfigurationHolder.config.elementsPairs,ConfigurationHolder.config.sizeSample]
		pivots = [:]
		ArrayList<ElementsPairs> elemPairs
		long startTime = System.currentTimeMillis()
		TextFileManager fm = new TextFileManager(ConfigurationHolder.config.itemsBaseFileName, ConfigurationHolder.config.textDataSeparator);
		String res;
		def pivote
		def max	
		println "pivote size" + pivots.size() + "categ size" +  categs.size
		
		if(pivotsQty <= 50)
		{
			if(fm.openFile(0))
			{
				def pivs = []
				
	
					
				if("differentPivotes" == pivotSelection){
					initPivotsByCateg()
					int i = 1
					int j = 1
					println  "categorias: " + pivotsByCateg.size()
					pivotsByCateg?.each{ obj ->
						pivote = getRandomElement(obj.value)
						pivs.add(pivote)
						elemPairs = getElementsPairs(aQty,pivotsQty,pivote,fm,obj.value)
						max = getMediaD(elemPairs,pivs, null)

						println "count categ elements: " + obj.value?.size()
						
						if(!pivots.get(obj.key)){
							pivots.put(obj.key, [])
						}

						PivotDto piv

						while(pivots.get(obj.key)?.size() < pivotsQty)
							{
								if (j == 4311){
									println "name categ: " + obj.key
									println "size: ${pivots?.get(obj.key).size()}"
									println "categ : " + i

								}
								if(i==obj.value.size()){
									println "MAX: 0"
									max = 0
									i = 1
								}
								i++
								if ((piv = getIncrementalPivot(pivs,max,elemPairs,fm,obj.value))){						
									
									pivots.get(piv.categ).add(piv)
									
								}
								
							}
						
						println "categ completas : " + j++
							
					}
				println "end categorias"
				
				
				/*
				// el primer pivot es elegido al azar
				pivote = getRandomPivot(fm)
				pivs.add(pivote)
				elemPairs = getElementsPairs(aQty,pivotsQty,pivote,fm)
				
				PivotDto piv 
				max = getMediaD(elemPairs,pivs, null)
				println "count: " + categs.elemCount
				if("differentPivotes" == pivotSelection){
					while(!pivots.every{it.value.size() ==pivotsQty} 
					|| pivots.isEmpty() || categs.elemCount >= pivots.size())
					{
						if ((piv = getIncrementalPivot(pivs,max,elemPairs,fm))){
							if(pivots.get(piv.categ) && pivots.get(piv.categ).size() < pivotsQty)
							{
								pivots.get(piv.categ).add(piv)
							}
							else if(!pivots.get(pivote.categ)) 
							{
								pivots.put(piv.categ, [piv])
							}
						}
					
					}
				*/
				
//					while(!pivots?.every{ it?.value?.size() == pivotsQty } || pivots.isEmpty() ) // ||  pivots?.size() < categs.size , pueden haber categ sin items
//					{
//						pivote = getIncrementalPivot(pivs,max,elemPairs,fm)
//						if(pivote){
//							if(pivots.get(pivote.categ) && pivots.get(pivote.categ).size < pivotsQty)
//							{
//								pivots.get(pivote.categ).add(pivote)
//							}
//							else if(!pivots.get(pivote.categ))
//							{
//								pivots.put(pivote.categ, [pivote])
//							}
//						}
//					}
					
				}else{
					while(pivs.size() < pivotsQty){
						pivote = getIncrementalPivot(pivs,max,elemPairs,fm)
						if(pivote){
							pivs.add(pivote)
						}
//						pCandidate = getRandomPivot(fm)
//						while(pivs?.find{it.itemId == pCandidate.itemId}){						
//							pCandidate = getRandomPivot(fm)
//						}
//						min = getMediaD(elemPairs,pivs,pCandidate)
//						if(min>max){
//							max=min
//							for (e in elemPairs){
//								e.addDistance(pCandidate)
//							}
//							pivs.add(pCandidate)
//						}
					}				
					fm.closeFile()
					pivots.put("ALL",pivs)
				}
				
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

		println "END createIncrementalPivots"
		log.info "$ConfigurationHolder.config.strategy|pivot_creation|${System.currentTimeMillis()-startTime}"
	}
	// TODO ver
	private def getIncrementalPivot(pivs,max,elemPairs,fm,pivots=null){
		def pCandidate = pivots? getRandomElement(pivots) : getRandomPivot(fm) 
		while(pivs?.find{it.itemId == pCandidate.itemId}){
			pCandidate = pivots? getRandomElement(pivots) : getRandomPivot(fm) 
		}
		def min = getMediaD(elemPairs,pivs,pCandidate)
		if(min>max){
			max=min
			for (e in elemPairs){
				e.addDistance(pCandidate)
			}
			return pCandidate
		}
		return null
	}
	private void initPivotsByCateg(){
		File filePv = new File(ConfigurationHolder.config.pivotsFileName.replaceAll("#strategy#","New"))
		filePv.withObjectInputStream(getClass().classLoader){ ois ->
			pivotsByCateg = ois.readObject()
		}
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
		
		log.info "$ConfigurationHolder.config.strategy|signature_creation|${System.currentTimeMillis()-startTime}"
	}
	
	def getPivotsForCateg(String categName)
	{
		def ret = pivots.get(categName)
		println "pivotes por categs: " + ret?.size()
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
			oos.writeObject(categs.elemCount)
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

		log.info "$ConfigurationHolder.config.strategy|index_file_creation|${System.currentTimeMillis()-startTime}"
	}
	
	def getRandomPivot(fm){
		Random rand = new Random()
		(1..rand.nextInt(5)).each
		{
			fm.nextPivot()
		}
		return fm.nextPivot()
	}
	def getRandomElement(elements){
		Random rand = new Random()
		return elements[Math.abs(rand.nextInt()) % elements?.size()]
	}
	
	def getElementsPairs(pairsQty,pivotsQty,pivot,file, pivots=null){
		ArrayList<ElementsPairs> pairs = new ArrayList<ElementsPairs>(pairsQty)
		ElementsPairs pair
		for (int i=0; i<pairsQty;i++){
			pair = new ElementsPairs()
			pair.a =  pivots? getRandomElement(pivots):getRandomPivot(file) 
			pair.b =  pivots? getRandomElement(pivots) : getRandomPivot(file) 
			while (pair.a.itemId == pair.b.itemId){
				pair.b = pivots? getRandomElement(pivots) : getRandomPivot(file)
				//log.info "mismo random en el par"
			}
			pair.initDist(pivotsQty,pivot)			
			pairs.add(pair)
		}
		return pairs
	}
	/**
	 * return criterio de eficiencia
	 * @param pairs
	 * @param pivots
	 * @param pivotCandidate
	 * @return
	 */
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
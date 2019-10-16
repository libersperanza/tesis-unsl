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
import tesis.file.manager.TextFileManager;
import tesis.structure.CategsHash;
import tesis.utils.Utils;
import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.ConfigurationHolder

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
	HashMap<String, ArrayList<PivotDto>> pivots = null;
	HashMap<String, ItemDto> items = null;

	HashMap<String, ArrayList<PivotDto>> allPivotsByCateg = null;


	public IndexManager(String initMode)
	{
		long startTime = System.currentTimeMillis()
		List strategyParams = ConfigurationHolder.config.strategy.split("_")
		String strategy = strategyParams[0]
		String pivotSelection = strategyParams[1]
		int pivotsQty = Integer.valueOf(strategyParams[2])

		//Si existe el file de items, lo cargo
		File file3 = new File(ConfigurationHolder.config.itemsDataFileName)

		if(file3.exists())
		{	
			file3.withObjectInputStream(getClass().classLoader){ ois ->
				items = (HashMap<String,ItemDto>)ois.readObject()
			}
		}
		
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
				pivots = (HashMap<String,ArrayList<PivotDto>>)ois.readObject()
			}
			
			log.info "$ConfigurationHolder.config.strategy|index_creation_from_file|${System.currentTimeMillis()-startTime}"
		}else{

			createCategsHash(createCategListFromFile(pivotsQty));

			fillPivotsByCategFromFile()
			if("random".equals(strategy)) //La estrategia puede ser mismos pivotes para todas las categorías o distintos pivotes para cada categoría
			{
				createRandomPivots(pivotSelection,pivotsQty)
			}
			else
			{
				createIncrementalPivots(pivotSelection,pivotsQty)
			}
			boolean fillItemsData = (items == null)
			if(fillItemsData)
			{
				items = new HashMap<String,ItemDto>()
			}
			createSignatures(fillItemsData)
			createIndexFiles()
			log.info "$ConfigurationHolder.config.strategy|index_creation|${System.currentTimeMillis()-startTime}"
		}

	}
	
	private createCategListFromFile(int pivotsQty)
	{
		long startTime = System.currentTimeMillis()
		TextFileManager fm = new TextFileManager(ConfigurationHolder.config.categsBaseFileName, ConfigurationHolder.config.textDataSeparator);
		ArrayList<CategDto> list=new ArrayList<CategDto>()
		
		if(fm.openFile(0))
		{
			CategDto dto;
			while((dto = fm.nextCateg()))
			{
				if(dto != null && dto.pivoteQtys.contains(pivotsQty))
				{
					list.add(dto)
				}
			}
			fm.closeFile();
		}
		else
		{
			throw new Exception("Error al abrir el archivo")
		}
		log.info "$ConfigurationHolder.config.strategy|reading_categs_from_file|${System.currentTimeMillis()-startTime}|size:${list.size}"
		return list
	}
	
	private void createCategsHash(ArrayList<CategDto> list)
	{
		categs = new CategsHash(list.size(), 0.4)
		for(CategDto c:list)
		{
			categs.add(c)
		}
	}
	private void createRandomPivots(String pivotSelection, int pivotsQty)
	{
		pivots = [:]
		long startTime = System.currentTimeMillis()

		if("differentPivotes" == pivotSelection)
		{
			allPivotsByCateg.each{ k, v ->
				CategDto catForSearch = new CategDto(categName:k,itemQty:0,pivoteQtys:null,signatures:null)
				int pos = categs.search(catForSearch)
				if (categs.get(pos).equals(catForSearch))//La categoría debe considerarse para ese índice
				{
					while(v.size() > pivotsQty) {
						Random rand = new Random()
						v.remove(rand.nextInt(v.size()))
					}
					pivots.put(k,v)
				}
			}
		}
		else
		{
			List pivValues = allPivotsByCateg.values().flatten()
			int index = 0
			List pivs = []
			(1..pivotsQty).each
			{
				Random rand = new Random()
				(1..rand.nextInt(10)).each
				{
					index = index + 1
				}
				pivs.add(pivValues[index])
			}
			pivots.put("ALL",pivs)
		}
		log.info "$ConfigurationHolder.config.strategy|pivot_creation|${System.currentTimeMillis()-startTime}"
	}
	private void createIncrementalPivots(String pivotSelection, int pivotsQty){
		
		long startTime = System.currentTimeMillis()

		int aQty = ConfigurationHolder.config.elementsPairs
		ArrayList<ElementsPairs> elemPairs
		PivotDto pivote
		int max	
		ArrayList<PivotDto> pivs = []
		pivots = [:]
	
		if("differentPivotes" == pivotSelection){
			allPivotsByCateg.each{ c, pList ->
				CategDto catForSearch = new CategDto(categName:c,itemQty:0,pivoteQtys:null,signatures:null)
				int pos = categs.search(catForSearch)
				if (categs.get(pos).equals(catForSearch))//La categoría debe considerarse para ese índice
				{
					pivote = getRandomElement(pList)
					pivs.add(pivote)
					elemPairs = getElementsPairs(aQty,pivotsQty,pivote,pList)
					max = getMediaD(elemPairs,pivs, null)

					if(!pivots.get(c)){
						pivots.put(c, [])
					}

					PivotDto piv
					int indexByCateg = 1

					while(pivots.get(c).size() < pivotsQty)
					{	
						if(indexByCateg == pList.size()){
							max = 0
							indexByCateg = 1
						}
						
						if ((piv = getIncrementalPivot(pivs,max,elemPairs,pList))){						
							
							pivots.get(piv.categ).add(piv)
							
						}
						indexByCateg++
					}
				}
			}
			
		}else{
			List pivValues = allPivotsByCateg.values().flatten()
			pivote = getRandomElement(pivValues)
			pivs.add(pivote)
			elemPairs = getElementsPairs(aQty, pivotsQty, pivote, pivValues)
			max = getMediaD(elemPairs,pivs, null)

			while(pivs.size() < pivotsQty){
				pivote = getIncrementalPivot(pivs, max, elemPairs, pivValues)
				if(pivote){
					pivs.add(pivote)
				}
			}				
			pivots.put("ALL",pivs)
		}

		log.info "$ConfigurationHolder.config.strategy|pivot_creation|${System.currentTimeMillis()-startTime}"
	}
	private PivotDto getRandomElement(ArrayList<PivotDto> elements){
		Random rand = new Random()
		return elements[Math.abs(rand.nextInt()) % elements?.size()]
	}
	
	private ArrayList<ElementsPairs> getElementsPairs(int pairsQty, int pivotsQty, PivotDto pivot, ArrayList<PivotDto> pivotsList){
		ArrayList<ElementsPairs> pairs = new ArrayList<ElementsPairs>(pairsQty)
		ElementsPairs pair
		for (int i=0; i<pairsQty;i++){
			pair = new ElementsPairs()
			pair.a =  getRandomElement(pivotsList)
			pair.b =  getRandomElement(pivotsList)
			while (pair.a.itemId == pair.b.itemId){
				pair.b = getRandomElement(pivotsList)
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
	private int getMediaD(ArrayList<ElementsPairs> pairs, ArrayList<PivotDto> pivots, PivotDto pivotCandidate){
		int max
		int value
		int media = 0
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
	
	private PivotDto getIncrementalPivot(ArrayList<PivotDto> pivs, int max, ArrayList<ElementsPairs> elemPairs, ArrayList<PivotDto> pivotsList){
		PivotDto pCandidate = getRandomElement(pivotsList)
		while(pivs?.find{it.itemId == pCandidate.itemId}){
			pCandidate = getRandomElement(pivotsList)
		}
		int min = getMediaD(elemPairs, pivs, pCandidate)
		if(min >= max){
			max = min
			for (e in elemPairs){
				e.addDistance(pCandidate)
			}
			return pCandidate
		}
		return null
	}
	private void fillPivotsByCategFromFile(){
		//El file contiene pivotes (items) para las categorías con más de 50 items
		File filePv = new File(ConfigurationHolder.config.pivotsFileName.replaceAll("#strategy#","New"))
		filePv.withObjectInputStream(getClass().classLoader){ ois ->
			allPivotsByCateg = ois.readObject()
		}
	}
	private void createSignatures(boolean fillItemsData)
	{
		long startTime = System.currentTimeMillis()
		
		TextFileManager fm = new TextFileManager(ConfigurationHolder.config.itemsBaseFileName, ConfigurationHolder.config.textDataSeparator);
		if(fm.openFile(0))
		{
			ItemDto curItem = fm.nextItem()
			while(curItem != null)
			{
				ArrayList<PivotDto> pivsCateg = getPivotsForCateg(curItem.getCateg())
				if(pivsCateg != null)
				{
					ItemSignature sig = new ItemSignature(curItem.getItemId(), curItem.getSearchTitle(), pivsCateg)
					CategDto catForSearch = new CategDto(categName:curItem.categ,itemQty:0,pivoteQtys:null,signatures:null)
					int pos = categs.search(catForSearch)
					if (categs.get(pos).equals(catForSearch)){
						categs.get(pos).signatures.add(sig)
						if(fillItemsData)
						{
							items.put(curItem.getItemId(), curItem)
						}
					}
				}
				curItem = fm.nextItem()
			}
			fm.closeFile()
		}
		else
		{
			throw new Exception("Error al abrir el archivo ${ConfigurationHolder.config.itemsBaseFileName}")
		}
		
		log.info "$ConfigurationHolder.config.strategy|signature_creation|${System.currentTimeMillis()-startTime}"
	}
	
	private ArrayList<PivotDto> getPivotsForCateg(String categName)
	{
		ArrayList<PivotDto> ret = pivots.get(categName)
		if(ret == null)
		{
			ret = pivots.get("ALL")
		}
		return ret
	}
	
	private void createIndexFiles()
	{
		long startTime = System.currentTimeMillis()

		CategDto virgin = new CategDto(categName:ConfigurationHolder.config.VIRGIN_CELL,itemQty:0,pivoteQtys:null,signatures:null);
		CategDto used = new CategDto(categName:ConfigurationHolder.config.USED_CELL,itemQty:0,pivoteQtys:null,signatures:null);
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
		log.info "$ConfigurationHolder.config.strategy|categs_file_creation|${System.currentTimeMillis()-startTime}"
		
		startTime = System.currentTimeMillis()
		File file2 = new File(ConfigurationHolder.config.pivotsFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))
		file2.withObjectOutputStream { oos ->
			oos.writeObject(pivots)
		}
		log.info "$ConfigurationHolder.config.strategy|pivots_file_creation|${System.currentTimeMillis()-startTime}"
		
		startTime = System.currentTimeMillis()
		File file3 = new File(ConfigurationHolder.config.itemsDataFileName)
		
		if(!file3.exists())
		{
			file3.withObjectOutputStream { oos ->
				oos.writeObject(items)
			}
		}

		log.info "$ConfigurationHolder.config.strategy|items_file_creation|${System.currentTimeMillis()-startTime}"
	}
}

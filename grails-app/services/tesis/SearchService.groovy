package tesis

import tesis.data.CategDto
import tesis.data.ItemSignature
import tesis.file.manager.RandomAccessFileManager
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.web.json.JSONObject
import tesis.utils.Utils
import com.sun.xml.internal.bind.v2.util.EditDistance
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory


class SearchService {

	Log log1 = LogFactory.getLog("tesis.SearchService")

    static transactional = false

    def sequentialSearch(String itemTitle, String categ,int radio, IndexManager mgr)
	{
		long startTime = System.currentTimeMillis()
		int pos = mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))
		//Obtengo las firmas de los items para poder buscarlos en el archivo
		def signatures = mgr.categs.get(pos).signatures
		def items =  getItemsFromFile(signatures, itemTitle, null)
		log1.info "$ConfigurationHolder.config.strategy|secuential|$radio|${System.currentTimeMillis()-startTime}|$items.size|$signatures.size"
		return items
    }
	
	def knnSearch(String itemTitle, String categ,int kNeighbors, IndexManager mgr)
	{
		long startTime = System.currentTimeMillis()
		def signatures = getCandidatesByKNN(itemTitle,categ,kNeighbors,mgr)
		def items = getItemsFromFile(signatures, itemTitle, null)
		log1.info "$ConfigurationHolder.config.strategy|using_index_knn|${System.currentTimeMillis()-startTime}|$items.size"
		return items
	}

	def rankSearch(String itemTitle, String categ,int radio, IndexManager mgr)
	{
		long startTime = System.currentTimeMillis()
		def signatures = getCandidatesByRank(itemTitle,categ,radio,mgr)
		def items = getItemsFromFile(signatures, itemTitle, radio)
		log1.info "$ConfigurationHolder.config.strategy|using_index_rank|${System.currentTimeMillis()-startTime}|$items.size"
		return items
	}
	private getItemsFromFile(ArrayList<ItemSignature> signatures, String itemTitle, Integer radio) {
		ArrayList<JSONObject> itemsFound = new ArrayList<JSONObject>()

		RandomAccessFileManager rfm = new RandomAccessFileManager(ConfigurationHolder.config.itemsDataFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))

		if (rfm.openFile("rw"))
		{
			signatures.each
			{
				def item =  new JSONObject(rfm.getItem(it.itemPosition,it.itemSize))
				def dist = EditDistance.editDistance(itemTitle, item.searchTitle)
				if(!radio || dist < radio)
				{
					itemsFound.add(item)
				}
			}
			rfm.closeFile()
		}
		return itemsFound
	}
	
	def getAllItemsByCateg(IndexManager mgr, String categ)
	{
		int pos = mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))
		
		def signatures = mgr.categs.get(pos).signatures
		
		ArrayList<JSONObject> itemsFound = new ArrayList<JSONObject>()
		RandomAccessFileManager rfm = new RandomAccessFileManager(ConfigurationHolder.config.itemsDataFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))
		if (rfm.openFile("rw"))
		{
			signatures.each
			{
				def item =  new JSONObject(rfm.getItem(it.itemPosition,it.itemSize))
				itemsFound.add(item)
							
			}
			rfm.closeFile()
		}
		log1.info "$ConfigurationHolder.config.strategy|all_in_categ|${categ}|${itemsFound.size()}"
		return itemsFound
	}
	def getCandidatesByRank(String itemTitle, String categ,int radio, IndexManager mgr){
		//Calculo la firma para la query
		ItemSignature sig = new ItemSignature(itemTitle, mgr.getPivotsForCateg(categ))
		int value
		ItemSignature candidate
		ArrayList<ItemSignature> candidates = new ArrayList<ItemSignature>()

		if(!sig) return candidates

		//Obtengo todas las firmas para la categoria
		int pos = mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))
		
		def signatures = mgr.categs.get(pos).signatures

		//Comparo la firma de la query con las firmas de la categoria, si el valor es mayor que el radio, descarto el item
		signatures.each 
		{
			candidate = it
			for (int i = 0;i<it.dists.size();i++)
			{
				value = (sig.dists[i] - candidate.dists[i]).abs()
				if (value > radio)
				{
					i = candidate.dists.size()
					candidate=null
				}
			}
			if(candidate){
				candidates.add(candidate)
			}
		}
		return candidates 
	}
	def getCandidatesByKNN(String itemTitle, String categ,int knn, IndexManager mgr){
		
		ItemSignature sig = new ItemSignature(itemTitle, mgr.getPivotsForCateg(categ))
		int value
		ItemSignature candidate
		ArrayList<ItemSignature> candidates = new ArrayList<ItemSignature>()
		
		if(!sig) return candidates
		
		//Obtengo todas las firmas para la categoria
		int pos = mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))
		println  mgr.categs.get(pos)
		def signatures = mgr.categs.get(pos).signatures

		List candidatesList = []
		
		signatures.each 
		{
			Map obj = [candidate:it]
			def distance = 0
			for (int i = 0;i<it.dists.size();i++)
			{
				distance += (sig.dists[i] - it.dists[i]).abs()
				
			}
			
			obj.distance = distance
			candidatesList.add(obj)
		}
		candidatesList = candidatesList.sort { it.distance }
		
		(1..knn).each{
			if(candidatesList.isEmpty()){
				return
			}
			candidates.add(candidatesList.head().candidate)
			candidatesList.remove(candidatesList.head())
		}

		return candidates
	}
}

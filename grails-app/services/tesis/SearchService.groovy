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
		long timeSearch = System.currentTimeMillis()-startTime
		startTime = System.currentTimeMillis()
		def items =  getItemsFromFile(signatures, itemTitle, radio)
		long timeIO =  System.currentTimeMillis()-startTime
		log1.info "$ConfigurationHolder.config.strategy|secuential|$radio|$timeSearch|$signatures.size|$timeIO|$items.size|$signatures.size"
		return items
    }

	def rankSearch(String itemTitle, String categ,int radio, IndexManager mgr)
	{
		long startTime = System.currentTimeMillis()
		def results = getCandidatesByRank(itemTitle,categ,radio,mgr)
		long timeSearch = System.currentTimeMillis()-startTime
		startTime = System.currentTimeMillis()
		def items = getItemsFromFile(results.candidates, itemTitle, radio)
		long timeIO =  System.currentTimeMillis()-startTime
		log1.info "$ConfigurationHolder.config.strategy|using_index_rank|$radio|$timeSearch|$results.candidates.size|$timeIO|$items.size|$results.total"
		return items
	}

	def knnByRankSearch(String itemTitle, String categ,int radio, int kNeighbors , IndexManager mgr)
	{
		long startTime = System.currentTimeMillis()

		long millisSearch = 0L
		long millisFile = 0L
		int candidatesSize = 0
		//Calculo la firma para la query
		ItemSignature sig = new ItemSignature(itemTitle, mgr.getPivotsForCateg(categ))

		def (candidates, items, itemsPrev, itemsRandom) = [[],[],[],[]]
	
		def i = 0
		
		Double rank = Math.pow(radio,i)
		boolean isRefined = false
		def limit = rank
		//Obtengo todas las firmas para la categoria
		int pos = mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))
		
		def signatures = mgr.categs.get(pos).signatures
		
		while(items.size() != kNeighbors &&  rank <= limit) {
			
			if(items.size() > kNeighbors){
				if(!isRefined){
					limit = rank
					rank = Math.pow(radio, (i - 2))
					itemsRandom = itemsPrev
				}else{
					itemsPrev = itemsRandom
					itemsRandom = items - itemsRandom
					break
				}
				isRefined = true				
				rank++
			}else if(!isRefined){
				rank = Math.pow(radio,i++)
				limit = rank
			}else{
				rank++
			}
			
			itemsPrev = items

			candidates = getCandidates(signatures,candidates,sig,rank)
			millisSearch += System.currentTimeMillis()-startTime
			startTime = System.currentTimeMillis()
			items = getItemsFromFile(candidates, itemTitle, (rank).intValue())
			millisFile += System.currentTimeMillis()-startTime
			startTime = System.currentTimeMillis()
		}
		
		if(items.size() != kNeighbors){
			def item 
			while(itemsPrev?.size() < kNeighbors) {
				Random rand = new Random()
				item = itemsRandom[Math.abs(rand.nextInt()) % itemsRandom?.size()]
				itemsRandom.remove(item)
				itemsPrev.add(item)
			}
			items = itemsPrev
		}
		millisSearch += System.currentTimeMillis()-startTime
		log1.info "$ConfigurationHolder.config.strategy|using_index_knn_rank|$rank|$millisSearch|$candidates.size|$millisFile|$items.size|$signatures.size"
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
				if(!radio)
				{
					itemsFound.add(item)
				}
				else
				{
					def dist = EditDistance.editDistance(itemTitle, item.searchTitle)
					if(dist < radio)
					{
						itemsFound.add(item)
					}
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

	

	def getCandidates(def signatures, def candidatesSelected ,def sig, def radio){
		int value
		ItemSignature candidate
		def candidates = candidatesSelected
		
		//Comparo la firma de la query con las firmas de la categoria, si el valor es mayor que el radio, descarto el item
		signatures?.each 
		{	
			candidate = it
			
			if(!candidatesSelected?.find{it == candidate}){
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
		}
		return candidates 
	}

	def getCandidatesByRank(String itemTitle, String categ,int radio, IndexManager mgr){
		//Calculo la firma para la query
		ItemSignature sig = new ItemSignature(itemTitle, mgr.getPivotsForCateg(categ))
		int value
		ItemSignature candidate
		ArrayList<ItemSignature> candidates = new ArrayList<ItemSignature>()

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
		return ["candidates":candidates,"total":signatures.size]
	}
	def getCandidatesByKNN(String itemTitle, String categ,int knn, IndexManager mgr){
		
		ItemSignature sig = new ItemSignature(itemTitle, mgr.getPivotsForCateg(categ))
		int value
		ItemSignature candidate
		ArrayList<ItemSignature> candidates = new ArrayList<ItemSignature>()
				
		//Obtengo todas las firmas para la categoria
		int pos = mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))

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

		return ["candidates":candidates,"total":signatures.size]
	}
}

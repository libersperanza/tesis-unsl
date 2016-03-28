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

    def sequentialSearch(String itemTitle, String categ,Integer radio, IndexManager mgr)
	{
		long id = java.lang.Thread.currentThread().getId();
		long startTimeCPU = Utils.getCpuTime([id])
		long startTimeUser = Utils.getUserTime([id])
		
		int pos = mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))
		//Obtengo las firmas de los items para poder buscarlos en el archivo
		def signatures = mgr.categs.get(pos).signatures
		def items =  getItemsFromFile(signatures, itemTitle, radio)
		
		long elapsedCPUTime = Utils.getCpuTime([id]) - startTimeCPU;
		long elapsedUserTime = Utils.getUserTime([id]) - startTimeUser;
		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|TIEMPO_CPU|TIEMPO_USUARIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG|TITULO_BUSQUEDA
		log1.info "$ConfigurationHolder.config.strategy|secuential|$radio|$elapsedCPUTime|$elapsedUserTime|$signatures.size|$items.size|$signatures.size|$categ|$itemTitle"
		return items
    }

	def rankSearch(String itemTitle, String categ,Integer radio, IndexManager mgr)
	{
		long id = java.lang.Thread.currentThread().getId();
		long startTimeCPU = Utils.getCpuTime([id])
		long startTimeUser = Utils.getUserTime([id])


		def results = getCandidatesByRank(itemTitle,categ,radio,mgr)
		def items = getItemsFromFile(results.candidates, itemTitle, radio)
		
		long elapsedCPUTime = Utils.getCpuTime([id]) - startTimeCPU;
		long elapsedUserTime = Utils.getUserTime([id]) - startTimeUser;

		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|TIEMPO_CPU|TIEMPO_USUARIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG|TITULO_BUSQUEDA
		log1.info "$ConfigurationHolder.config.strategy|using_index_rank|$radio|$elapsedCPUTime|$elapsedUserTime|$results.candidates.size|$items.size|$results.total|$categ|$itemTitle"
		return items
	}

	def getCandidatesByRank(String itemTitle, String categ,Integer radio, IndexManager mgr)
	{
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

	def knnByRankSearch(String itemTitle, String categ,Integer radio, int kNeighbors , IndexManager mgr)
	{
		long id = java.lang.Thread.currentThread().getId();
		long startTimeCPU = Utils.getCpuTime([id])
		long startTimeUser = Utils.getUserTime([id])


		int candidatesSize = 0 //ES LA CANTIDAD DE VECES QUE CALCULAMOS LA FUNCION DE DISTANCIA
		//Calculo la firma para la query
		ItemSignature sig = new ItemSignature(itemTitle, mgr.getPivotsForCateg(categ))

		def (candidates, items, itemsPrev, itemsRandom) = [[],[],[],[]]
	
		def i = 0
		
		Double rank = Math.pow(radio,i)
		boolean isRefined = false
		def limit = rank
		//Obtengo todas las firmas para la categoria
		int pos = mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))
		
		def signatures = mgr.categs.get(pos).signatures.clone()
		
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
			items = getItemsFromFile(candidates, itemTitle, (rank).intValue())
			candidatesSize +=candidates.size
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

		long elapsedCPUTime = Utils.getCpuTime([id]) - startTimeCPU;
		long elapsedUserTime = Utils.getUserTime([id]) - startTimeUser;

		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|TIEMPO_CPU|TIEMPO_USUARIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG|TITULO_BUSQUEDA
		log1.info "$ConfigurationHolder.config.strategy|using_index_knn_rank|$rank|$elapsedCPUTime|$elapsedUserTime|$candidatesSize|$items.size|$signatures.size|$categ|$itemTitle"
		return items
	}

	def getCandidates(def signatures, def candidatesSelected ,def sig, def radio){
		int value
		ItemSignature candidate
		def candidates = candidatesSelected
		
		//Comparo la firma de la query con las firmas de la categoria, si el valor es mayor que el radio, descarto el item
		signatures.each 
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

	def knnByRankSearchV2(String itemTitle, String categ,Integer a, int kNeighbors , IndexManager mgr)
	{
		long id = java.lang.Thread.currentThread().getId();

		long startTimeCPU = Utils.getCpuTime([id])
		long startTimeUser = Utils.getUserTime([id])

		
		int radio = 0
		int i = 0
		def items = []
		def indexData = mgr.categs.get(mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))).signatures.collect{["signature":it]}
		//Calculo la firma para la query
		ItemSignature q = new ItemSignature(itemTitle, mgr.getPivotsForCateg(categ))

		while(items.size < kNeighbors && indexData.size > 0) {
			radio = Math.pow(a,i).intValue()
			def results = getItemsForRadio(itemTitle, q, radio, indexData)
			//log.info "PROBANDO RADIO $radio RESULTS $results.size"
			if(items.size + results.size > kNeighbors)
			{
				int li = Math.pow(a,i - 1).intValue()
				int ls = radio
				boolean done = false
				while(li <= ls)
				{
					radio = ((ls + li)/2).intValue()
					//log.info "BISECCION RADIO $radio"
					results = getItemsForRadio(itemTitle, q, radio, indexData)
					if(items.size() + results.size == kNeighbors)
					{
						//log.info "RESULTS $results.size - ITEMS $items.size"
						li = ls + 1
						items.addAll(results.collect{it.item})
						indexData.removeAll(results.collect{it.signature})
					}
					else
					{
						//log.info "RESULTS $results.size - ITEMS $items.size"
						if(items.size + results.size < kNeighbors)
						{
							li = radio + 1
							items.addAll(results.collect{it.item})
							indexData.removeAll(results.collect{it.signature})
						}
						else
						{
							ls = radio - 1
						}
						radio = radio + 1 //PARA DESPUES
					}
				}
				if(items.size != kNeighbors)
				{
					results = getItemsForRadio(itemTitle, q, radio, indexData)
					//log.info("ELIMINANDO ITEMS SOBRANTES DE $results.size")
					int j = 0;
					while(items.size < kNeighbors)
					{
						items.add(results[j].item)
						indexData.remove(results[j].signature)
						j++
					}
				}
			}
			else
			{
				//log.info("AGREGANDO $results.size ITEMS AL RESULTADO FINAL")
				items.addAll(results.collect{it.item})
				indexData.removeAll(results.collect{it.signature})
				i++
			}
		}
		//log.info("TERMINO")
		int evalDistQty = indexData.findAll{it.dist!=null}.size + items.size

		long elapsedCPUTime = Utils.getCpuTime([id]) - startTimeCPU;
		long elapsedUserTime = Utils.getUserTime([id]) - startTimeUser;

		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|TIEMPO_CPU|TIEMPO_USUARIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG|TITULO_BUSQUEDA
		log1.info "$ConfigurationHolder.config.strategy|using_index_knn_radio|$radio|$elapsedCPUTime|$elapsedUserTime|$evalDistQty|$items.size|${indexData.size + items.size}|$categ|$itemTitle"
		return items
	}

	def getItemsForRadio(String itemTitle, ItemSignature q, Integer radio, List indexData){
		
		//ArrayList<JSONObject> itemsFound = new ArrayList<JSONObject>()
		List itemsFound = []
		RandomAccessFileManager rfm = new RandomAccessFileManager(ConfigurationHolder.config.itemsDataFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))

		rfm.openFile("rw")
		
		int dyc = 0
		int dc = 0
		//Comparo la firma de la query con las firmas de la categoria, si el valor es mayor que el radio, descarto el item
		indexData.each 
		{ candidate -> 
			boolean add = true
			for (int i = 0;i<candidate.signature.dists.size();i++)
			{
				if ((q.dists[i] - candidate.signature.dists[i]).abs() > radio)
				{
					i = candidate.signature.dists.size()
					add = false
				}
			}
			if(add)
			{
				def item =  new JSONObject(rfm.getItem(candidate.signature.itemPosition,candidate.signature.itemSize))
				if(!candidate.dist)
				{
					candidate.dist = EditDistance.editDistance(itemTitle, item.searchTitle)
				}
				if(candidate.dist < radio)
				{
					itemsFound.add([item:item, signature: candidate])
				}
			}
		}
		rfm.closeFile()
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
				log.info item
							
			}
			rfm.closeFile()
		}
		log1.info "$ConfigurationHolder.config.strategy|all_in_categ|${categ}|${itemsFound.size()}|$categ| "
		return itemsFound
	}
}

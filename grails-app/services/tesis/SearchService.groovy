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
		//long id = java.lang.Thread.currentThread().getId();
		//long startTimeCPU = Utils.getCpuTime([id])
		//long startTimeUser = Utils.getUserTime([id])
		
		int pos = mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))
		//Obtengo las firmas de los items para poder buscarlos en el archivo
		def signatures = mgr.categs.get(pos).signatures
		def items =  getItemsFromFile(signatures, itemTitle, radio)
		
		//long elapsedCPUTime = Utils.getCpuTime([id]) - startTimeCPU;
		//long elapsedUserTime = Utils.getUserTime([id]) - startTimeUser;
		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG
		log1.info "$ConfigurationHolder.config.strategy|sequential|$radio|$elapsedCPUTime|$elapsedUserTime|$signatures.size|$items.size|$signatures.size|$categ"
		return items
    }

	def rankSearch(String itemTitle, String categ,Integer radio, IndexManager mgr)
	{
		//long id = java.lang.Thread.currentThread().getId();
		//long startTimeCPU = Utils.getCpuTime([id])
		//long startTimeUser = Utils.getUserTime([id])


		def results = getCandidatesByRank(itemTitle,categ,radio,mgr)
		def items = getItemsFromFile(results.candidates, itemTitle, radio)
		
		//long elapsedCPUTime = Utils.getCpuTime([id]) - startTimeCPU;
		//long elapsedUserTime = Utils.getUserTime([id]) - startTimeUser;

		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG
		log1.info "$ConfigurationHolder.config.strategy|using_index_rank|$radio|$results.candidates.size|$items.size|$results.total|$categ"
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

	//ESTA NO SE USA!!!!!!!!
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
			items = getItemsFromFile(candidates, itemTitle, (rank).intValue())
			candidatesSize +=candidates.size
		}
		
		if(items.size() != kNeighbors){
			def item 
			while(itemsPrev?.size() < kNeighbors) {
				Random rand = new Random()
				item = itemsRandom[Math.abs(rand.nextInt()) % itemsRandom.size()]
				itemsRandom.remove(item)
				itemsPrev.add(item)
			}
			items = itemsPrev
		}

		long elapsedCPUTime = Utils.getCpuTime([id]) - startTimeCPU;
		long elapsedUserTime = Utils.getUserTime([id]) - startTimeUser;

		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG|TITULO_BUSQUEDA
		log1.info "$ConfigurationHolder.config.strategy|using_index_knn_rank|$rank|$candidatesSize|$items.size|$signatures.size|$categ"
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
				JSONObject item = rfm.getItem(it.itemPosition,it.itemSize)
				if(!radio)
				{
					itemsFound.add(item)
				}
				else
				{
					int dist = EditDistance.editDistance(itemTitle, item.searchTitle)
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

	def knnByRankSearchV2(String query, String categ,Integer a, int kNeighbors , IndexManager mgr)
	{
		//long id = java.lang.Thread.currentThread().getId();

		//long startTimeCPU = Utils.getCpuTime([id])
		//long startTimeUser = Utils.getUserTime([id])

		int radio = 0
		int i = 1
		List finalResult
		List indexData = mgr.categs.get(mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))).signatures.collect{["signature":it]}
		//Calculo la firma para la query
		ItemSignature querySignature = new ItemSignature(query, mgr.getPivotsForCateg(categ))
		List items = []
		while(items.size() < kNeighbors && indexData.size > 0) {
			radio = Math.pow(a,i).intValue()
			//log.info "PROBANDO RADIO $radio"
			items = getItemsForRadio(query, querySignature, radio, indexData)
			//log.info "PROBANDO RADIO $radio RESULTS $items.size"
			if(items.size() > kNeighbors)
			{
				int li = Math.pow(a,i - 1).intValue()
				int ls = radio

				while(li <= ls)
				{
					radio = ((ls + li)/2).intValue()
					//log.info "BISECCION RADIO $radio"
					items = getItemsForRadio(query, querySignature, radio, indexData)
					//log.info "BISECCION RADIO $radio RESULTS $items.size"
					if(items.size() == kNeighbors)
					{
						//log.info "FIN BISECCION RADIO $radio"
						li = ls + 1
						finalResult = items
					}
					else
					{
						if(items.size < kNeighbors)
						{
							//log.info "K NO ALCANZADO CON RADIO $radio"
							li = radio + 1
							radio = radio + 1
						}
						else
						{
							ls = radio - 1
						}
					}
				}
				if(items.size != kNeighbors)
				{
					//log.info "FIN BISECCION RADIO $radio $items.size"
					if(items.size() < kNeighbors){
						items = getItemsForRadio(query, querySignature, radio, indexData)
					}
					//log.info "ORDENANDO ITEMS EN BASE A LA DISTANCIA DE Q"
					items.sort{it.dist}
					finalResult = items.subList(0,kNeighbors)
				}
			}
			else{
				finalResult = items
			}
			i++
		}

		//long elapsedCPUTime = Utils.getCpuTime([id]) - startTimeCPU;
		//long elapsedUserTime = Utils.getUserTime([id]) - startTimeUser;
		int evalDistQty = indexData.findAll{it.dist != null}.size()
		//log.info("TERMINO")
		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG
		log1.info "$ConfigurationHolder.config.strategy|using_index_knn_radio|$radio|$evalDistQty|${finalResult.size()}|${indexData.size()}|$categ"
		return finalResult.collect{it.item}
	}

	def getItemsForRadio(String query, ItemSignature q, Integer radio, List indexData){
		
		List itemsFound = []
		RandomAccessFileManager rfm = new RandomAccessFileManager(ConfigurationHolder.config.itemsDataFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))

		rfm.openFile("r")

		//long id = java.lang.Thread.currentThread().getId();
		//long startTimeCPU = 0
		//long timeCheckSignatures = 0
		//long timeGetItemAndEditDist = 0
		//int piv_comparisons = 0
		//int piv_discarded = 0
		//int ed_calc = 0
		
		//Comparo la firma de la query con las firmas de la categoria, si el valor es mayor que el radio, descarto el item
		for(int j = 0; j < indexData.size(); j++)
		{ 
			//Para no chequear los pivotes si ya se había calculado la distancia a la query
			if(indexData[j].dist != null){
				if(indexData[j].dist < radio){
					itemsFound.add(indexData[j])
				}
			}
			else{
				//startTimeCPU = Utils.getCpuTime([id])
				boolean check = true
				//piv_comparisons++
				for (int i = 0; i < indexData[j].signature.dists.size(); i++)
				{
					if ((q.dists[i] - indexData[j].signature.dists[i]).abs() > radio)
					{
						i = indexData[j].signature.dists.size()
						check = false
						//piv_discarded++
					}
				}
				//timeCheckSignatures += Utils.getCpuTime([id]) - startTimeCPU;
				//startTimeCPU = Utils.getCpuTime([id])
				if(check)
				{
					if(indexData[j].dist == null)
					{
						//ed_calc++
						indexData[j].item = rfm.getItem(indexData[j].signature.itemPosition,indexData[j].signature.itemSize)
						indexData[j].dist = EditDistance.editDistance(query, indexData[j].item.searchTitle)
					}
					if(indexData[j].dist < radio)
					{
						itemsFound.add(indexData[j])
					}
				}
				//timeGetItemAndEditDist += Utils.getCpuTime([id]) - startTimeCPU;
			}
		}
		rfm.closeFile()
		//log.info "[TPO_PIV_COMP: $timeCheckSignatures][#PIV_COMP:$piv_comparisons][#PIV_DISC:$piv_discarded][TPO_ED_CALC: $timeGetItemAndEditDist][#ED_CALC:$ed_calc]"
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
		log1.info "$ConfigurationHolder.config.strategy|all_in_categ|${categ}|${itemsFound.size()}|$categ| "
		return itemsFound
	}
}

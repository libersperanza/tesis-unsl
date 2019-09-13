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

    ArrayList<JSONObject> sequentialSearch(String itemTitle, String categ,Integer radio, IndexManager mgr)
	{
		long id = java.lang.Thread.currentThread().getId();
		long startTimeCPU = Utils.getCpuTime([id])
		long startTimeUser = Utils.getUserTime([id])
		
		int pos = mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))
		//Obtengo las firmas de los items para poder buscarlos en el archivo
		ArrayList<ItemSignature> signatures = mgr.categs.get(pos).signatures
		ArrayList<JSONObject> items =  getItemsFromFile(signatures, itemTitle, radio, mgr)
		
		long elapsedCPUTime = Utils.getCpuTime([id]) - startTimeCPU;
		long elapsedUserTime = Utils.getUserTime([id]) - startTimeUser;
		
		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG
		//log1.info "$ConfigurationHolder.config.strategy|sequential|$radio|$elapsedCPUTime|$elapsedUserTime|$signatures.size|$items.size|$signatures.size|$categ"
		
		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|TIEMPO_CPU|TIEMPO_USUARIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG|TITULO_BUSQUEDA
		log1.info "$ConfigurationHolder.config.strategy|secuential|$radio|$elapsedCPUTime|$elapsedUserTime|$signatures.size|$items.size|$signatures.size|$categ"
		
		return items
    }

	ArrayList<JSONObject> rankSearch(String itemTitle, String categ,Integer radio, IndexManager mgr)
	{
		long id = java.lang.Thread.currentThread().getId();
		long startTimeCPU = Utils.getCpuTime([id])
		long startTimeUser = Utils.getUserTime([id])


		Map results = getCandidatesByRank(itemTitle,categ,radio,mgr)

		/*log.info("TIEMPO DE FILTRO: ${Utils.getCpuTime([id]) - startTimeCPU} - ${Utils.getUserTime([id]) - startTimeUser}")

		long startTimeCPU2 = Utils.getCpuTime([id])
		long startTimeUser2 = Utils.getUserTime([id])*/
		
		ArrayList<JSONObject> items = getItemsFromFile(results.candidates, itemTitle, radio, mgr)

		//log.info("TIEMPO DE COMPARACION CONTRA ARCHIVO: ${Utils.getCpuTime([id]) - startTimeCPU2} - ${Utils.getUserTime([id]) - startTimeUser2}")
		
		long elapsedCPUTime = Utils.getCpuTime([id]) - startTimeCPU;
		long elapsedUserTime = Utils.getUserTime([id]) - startTimeUser;

		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG
		//log1.info "$ConfigurationHolder.config.strategy|using_index_rank|$radio|$results.candidates.size|$items.size|$results.total|$categ"
		
		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|TIEMPO_CPU|TIEMPO_USUARIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG
		log1.info "$ConfigurationHolder.config.strategy|using_index_rank|$radio|$elapsedCPUTime|$elapsedUserTime|$results.candidates.size|$items.size|$results.total|$categ"
		return items
	}

	Map getCandidatesByRank(String itemTitle, String categ,Integer radio, IndexManager mgr)
	{
		/*long id = java.lang.Thread.currentThread().getId();
		long startTimeCPU = Utils.getCpuTime([id])
		long startTimeUser = Utils.getUserTime([id])*/
		//Calculo la firma para la query
		ItemSignature sig = new ItemSignature(itemTitle, mgr.getPivotsForCateg(categ))
		/*long elapsedCPUTime = Utils.getCpuTime([id]) - startTimeCPU;
		long elapsedUserTime = Utils.getUserTime([id]) - startTimeUser;
		log.info("TIEMPO CALCULO FIRMA Q: ${elapsedCPUTime + elapsedUserTime}")*/
		ArrayList<ItemSignature> candidates = new ArrayList<ItemSignature>()

		//Obtengo todas las firmas para la categoria
		int pos = mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))
		ArrayList<ItemSignature> signatures = mgr.categs.get(pos).signatures

		
		
		//Comparo la firma de la query con las firmas de la categoria, si el valor es mayor que el radio, descarto el item
		for(int j = 0; j < signatures.size();j++)
		{
			int[] dists = signatures[j].dists
			boolean add = true
			for (int i = 0;i < dists.length;i++)
			{
				int value
				if(sig.dists[i] > dists[i])
				{
					value = sig.dists[i] - dists[i]
				}
				else
				{
					value = dists[i] - sig.dists[i]
				}
				//value = (sig.dists[i] - dists[i]).abs()
				if (value > radio)
				{
					i = dists.length
					add = false
				}
			}
			if(add){
				candidates.add(signatures[j])
			}
		}
		return ["candidates":candidates,"total":signatures.size]
	}

	private ArrayList<JSONObject> getItemsFromFile(ArrayList<ItemSignature> signatures, String itemTitle, Integer radio, IndexManager mgr) {
		
		ArrayList<JSONObject> itemsFound = new ArrayList<JSONObject>()
		
		/*long id = java.lang.Thread.currentThread().getId();
		long startTimeCPU = 0
		long startTimeUser = 0
		long timeReadFileCPU = 0
		long timeEvalDistCPU = 0
		long timeCheckRadioCPU = 0
		long timeReadFileUsr = 0
		long timeEvalDistUsr = 0
		long timeCheckRadioUsr = 0*/

		for(int i = 0; i < signatures.size(); i++)
		{
			/*startTimeCPU = Utils.getCpuTime([id])
			startTimeUser = Utils.getUserTime([id])*/

			JSONObject item = mgr.items.get(signatures[i].itemPosition)
			/*timeReadFileCPU += Utils.getCpuTime([id]) - startTimeCPU;
			timeReadFileUsr += Utils.getUserTime([id]) - startTimeUser;

			startTimeCPU = Utils.getCpuTime([id])
			startTimeUser = Utils.getUserTime([id])*/
			
			int dist = EditDistance.editDistance(itemTitle, item.searchTitle)
			
			/*timeEvalDistCPU += Utils.getCpuTime([id]) - startTimeCPU;
			timeEvalDistUsr += Utils.getUserTime([id]) - startTimeUser;
			
			startTimeCPU = Utils.getCpuTime([id]) 
			startTimeUser = Utils.getUserTime([id])*/
			if((radio - dist) > 0)
			{
				itemsFound.add(item)
			}
			/*timeCheckRadioCPU += Utils.getCpuTime([id]) - startTimeCPU;
			timeCheckRadioUsr += Utils.getUserTime([id]) - startTimeUser;*/
		}
		/*log.info("TPO LECTURA ARCHIVO: $timeReadFileCPU - $timeReadFileUsr")
		log.info("TPO EVAL DIST: $timeEvalDistCPU - $timeEvalDistUsr")
		log.info("TPO CHECK RADIO: $timeCheckRadioCPU - $timeCheckRadioUsr")*/
		return itemsFound
	}

	List<JSONObject> knnByRankSearchV2(String query, String categ,Integer a, int kNeighbors , IndexManager mgr)
	{
		long id = java.lang.Thread.currentThread().getId();

		long startTimeCPU = Utils.getCpuTime([id])
		long startTimeUser = Utils.getUserTime([id])

		int radio = 0
		int i = 1
		List finalResult
		ArrayList<HashMap<String,Object>> indexData = mgr.categs.get(mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))).signatures.collect{["signature":it]}
		//Calculo la firma para la query
		ItemSignature querySignature = new ItemSignature(query, mgr.getPivotsForCateg(categ))
		ArrayList<HashMap<String,Object>> items = new ArrayList<HashMap<String,Object>>()
		while(items.size() < kNeighbors && indexData.size > 0) {
			radio = Math.pow(a,i).intValue()
			//log.info "PROBANDO RADIO $radio"
			items = getItemsForRadio(query, querySignature, radio, indexData, mgr)
			//log.info "PROBANDO RADIO $radio RESULTS $items.size"
			if(items.size() > kNeighbors)
			{
				int li = Math.pow(a,i - 1).intValue()
				int ls = radio

				while(li <= ls)
				{
					radio = ((ls + li)/2).intValue()
					//log.info "BISECCION RADIO $radio"
					items = getItemsForRadio(query, querySignature, radio, indexData, mgr)
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
						items = getItemsForRadio(query, querySignature, radio, indexData, mgr)
					}
					//log.info "ORDENANDO ITEMS EN BASE A LA DISTANCIA DE Q"
					items.sort{it.dist}
					finalResult = new ArrayList<HashMap<String,Object>>(items.subList(0,kNeighbors))
				}
			}
			else{
				finalResult = items
			}
			i++
		}

		long elapsedCPUTime = Utils.getCpuTime([id]) - startTimeCPU;
		long elapsedUserTime = Utils.getUserTime([id]) - startTimeUser;
		int evalDistQty = indexData.findAll{it.dist != null}.size()
		
		//log.info("TERMINO")
		
		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG
		//log1.info "$ConfigurationHolder.config.strategy|using_index_knn_radio|$radio|$evalDistQty|${finalResult.size()}|${indexData.size()}|$categ"
		
		//ESTRATEGIA|TIPO_BUSQUEDA|RADIO|TIEMPO_CPU|TIEMPO_USUARIO|#EVAL_FUNCION_DISTANCIA==#CANDIDATOS|#RESULTADOS|#ITEMS_CATEG|CATEG
		log1.info "$ConfigurationHolder.config.strategy|using_index_knn_radio|$radio|$elapsedCPUTime|$elapsedUserTime|$evalDistQty|$finalResult.size|$indexData.size|$categ"
		return finalResult.collect{it.item}
	}

	ArrayList<HashMap<String,Object>> getItemsForRadio(String query, ItemSignature q, Integer radio, List indexData, IndexManager mgr){
		
		ArrayList<HashMap<String,Object>> itemsFound = new ArrayList<HashMap<String,Object>>()

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
						indexData[j].item = mgr.items.get(indexData[j].signature.itemPosition)
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
		//log.info "[TPO_PIV_COMP: $timeCheckSignatures][#PIV_COMP:$piv_comparisons][#PIV_DISC:$piv_discarded][TPO_ED_CALC: $timeGetItemAndEditDist][#ED_CALC:$ed_calc]"
		return itemsFound
	}
	
	ArrayList<JSONObject> getAllItemsByCateg(IndexManager mgr, String categ)
	{
		int pos = mgr.categs.search(new CategDto(categName:categ,itemQty:0,signatures:null))
		
		ArrayList<ItemSignature> signatures = mgr.categs.get(pos).signatures
		
		ArrayList<JSONObject> itemsFound = new ArrayList<JSONObject>()
		RandomAccessFileManager rfm = new RandomAccessFileManager(ConfigurationHolder.config.itemsDataFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))
		if (rfm.openFile("rw"))
		{	
			for(it in signatures)
			{
				JSONObject item =  new JSONObject(rfm.getItem(it.itemPosition,it.itemSize))
				itemsFound.add(item)
							
			}
			rfm.closeFile()
		}
		log1.info "$ConfigurationHolder.config.strategy|all_in_categ|${categ}|${itemsFound.size()}|$categ| "
		return itemsFound
	}

	def getHistogramByRadio(String categ,IndexManager mgr, Integer percentage = 0.1){

		log.info "sortCandidateByRadio "
		long startTime   = System.currentTimeMillis()
		Map<String, Integer> itemsByRadio = [:]
		Map pivotesMap = mgr.getAllpivotsByCateg()
	//	log.info "pivotes $pivotesMap"
		def itemsByCateg = pivotesMap.get(categ)
		def dist
		
		log.info "itemsByCateg ${itemsByCateg.size()}"
		def iPercentage = (itemsByCateg.size() * percentage) /100
		def i = 0
log.info "iPercentage ${iPercentage}"
		for(item in itemsByCateg){
			if(i < iPercentage){
				i +=1
			}else{
				log.info "end"
				log.info itemsByRadio
				return
			}
			for (itemAux in itemsByCateg){
				dist =  EditDistance.editDistance(item.searchTitle, itemAux.searchTitle)
				if(itemsByRadio["${dist}"]){
					itemsByRadio["${dist}"] += 1
				}else{
					itemsByRadio["${dist}"] = 1
				}
			}
		}
		
		log.info itemsByRadio
		// RandomAccessFileManager rfm = new RandomAccessFileManager(ConfigurationHolder.config.itemsDataFileName.replaceAll("#strategy#","${ConfigurationHolder.config.strategy}"))

		// rfm.openFile("r")	

		// for(candidate in indexData){

		// 	if(!candidate.dist)
		// 	{	
		// 		def item =  new JSONObject(rfm.getItem(candidate.signature.itemPosition,candidate.signature.itemSize))
		// 		candidate.dist = EditDistance.editDistance(itemTitle, item.searchTitle)
				
		// 	}

		// 	if(itemsByRadio["${candidate.dist}"]){
		// 		itemsByRadio["${candidate.dist}"].add(candidate)
		// 	}else{
		// 		itemsByRadio["${candidate.dist}"] = [candidate]
		// 	}
		// }
		// log.info "TIME after getAll items ${System.currentTimeMillis() - startTime}"
		// log.info "itemsByRadio: ${itemsByRadio.size()}"
		// def itemsSorted = itemsByRadio.sort {it.value.siz}
		// itemsSorted.each{k,v ->
		// 	log.info "radio $k - ITEMS: ${v.size()}"
		// }

	}

}

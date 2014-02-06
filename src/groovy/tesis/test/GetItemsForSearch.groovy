package tesis.test

import grails.converters.JSON;
import tesis.BasicRestService;
import tesis.data.CategDto;
import tesis.file.manager.TextFileManager;
import tesis.utils.Utils;
import tesis.data.ItemDto;
import com.ning.http.client.FluentStringsMap
import com.ning.http.client.Response;

class GetItemsForSearch
{

	static main(args)
	{
		//this.firstStep()
		//this.secondStep()
		this.thirdStep()
	}
	
	static thirdStep()
	{
		
		def itemsForSearch = [:] //[categ_code:[site,query,items]]
		TextFileManager fm = new TextFileManager("./test_data/categs_search_2.txt", ";");

		fm.openFile(0)
		String linea;
		while((linea =  fm.bf.readLine()) != null) {
			String[] arLinea = linea.split(";");
			itemsForSearch.put(arLinea[0], ["site":arLinea[1],"query":arLinea[2],"items":[]])
		}
		fm.closeFile();
		
		
		BasicRestService brs = new BasicRestService()
		FluentStringsMap map = new FluentStringsMap()
		
		itemsForSearch.each{ key, value ->
			map = new FluentStringsMap()
			map.add("q", value.query.replace("_"," "))
			map.add("limit", "4")
			
			Response res = brs.get("https://api.mercadolibre.com/sites/${value.site}/search",map)
			
			if(res?.getStatusCode()!= 200)
			{
				println "${res?.getStatusCode()} FAILED: $key;${value.site};${value.query}"

			}
			else
			{
				def sResponse = JSON.parse(res.getResponseBody())
				if(sResponse.paging.total > 0)
				{
					sResponse.results.each {
						value.items.add(it.title)
					}
					
				}
				else
				{
					println "NO RESULT: $key;${value.site};${value.query}"
				}
			}
		}
		
		
		new File("/Users/lsperanza/Desarrollo/workspace/tesis-unsl/test_data/items_for_search_2.txt").withWriter("UTF-8"){ out ->
			itemsForSearch.each {  key, value ->
				value.items.each{ tit ->
					out.println "$key;${value.site};$tit;"
				}
			  
			}
		 }
	}

	static secondStep()
	{
		
		def itemsForSearch = [:] //[categ_id:[site,categ_code,items]]
		TextFileManager fm = new TextFileManager("./test_data/categs_search.txt", ";");

		fm.openFile(0)
		String linea;
		while((linea =  fm.bf.readLine()) != null) {
			String[] arLinea = linea.split(";");
			itemsForSearch.put(arLinea[1], ["site":arLinea[1].substring(0,3),"categ_code":arLinea[0],"items":[]])
		}
		fm.closeFile();
		
		
		BasicRestService brs = new BasicRestService()
		FluentStringsMap map = new FluentStringsMap()
		
		itemsForSearch.each{ key, value ->
			String idForSearch = key
			while((value.items.size() == 0) && (idForSearch.length() > 5))
			{
				
				map = new FluentStringsMap()
				map.add("category", idForSearch)
				map.add("limit", "4")
				
				Response res = brs.get("https://api.mercadolibre.com/sites/${value.site}/search",map)
				
				if(res?.getStatusCode()!= 200)
				{
					println "${res?.getStatusCode()} FAILED: ${value.categ_code};$key"
					idForSearch = ''
				}
				else
				{
					def sResponse = JSON.parse(res.getResponseBody())
					if(sResponse.paging.total > 0)
					{
						sResponse.results.each {
							value.items.add(it.title)
						}
					}
					else
					{
						idForSearch = idForSearch.substring(0,idForSearch.length()-1)
					}
				}
			}
			if(value.items.size() == 0)
			{
				println "${value.categ_code};$key"
			}
		}
		
		
		new File("/Users/lsperanza/Desarrollo/workspace/tesis-unsl/test_data/items_for_search.txt").withWriter { out ->
			itemsForSearch.each {  key, value ->
				value.items.each{ tit ->
					out.println "$key;$value.categ_code;$tit;"
				}
			  
			}
		 }
	}

	static firstStep()
	{
		//Leo todas las categs
		def categs = getCategs();
		
		//Voy leyendo el archivo y actualizando el contador
		String res
		TextFileManager fm = new TextFileManager("./test_data/items.csv", ";");

		def categsForSearch = [] as Set
		
		if(fm.openFile(0))
		{
			def curItem
			while(curItem = fm.nextItem())
			{
				
				
				def categData = categs.get(curItem.categ)
				
				if(categData > 0)
				{
					String id = curItem.categ + ";"+curItem.itemId.substring(0,3) + curItem.categ
					categsForSearch.add(id)
				}
			}
		}
		else
		{
			throw new Exception("Error al abrir el archivo para lectura/escritura")
		}
		
		categsForSearch.each { println it}
	}
	static getCategs()
	{
		TextFileManager fm = new TextFileManager("./test_data/categs.csv", ";");

		def map = [:]

		if(fm.openFile(0))
		{
			CategDto dto;
			while((dto = fm.nextCateg()))
			{
				map.put(dto.getCategName(),1)
			}
			fm.closeFile();
			return map
		}
		else
		{
			throw new Exception("Error al abrir el archivo")
		}
	}
}

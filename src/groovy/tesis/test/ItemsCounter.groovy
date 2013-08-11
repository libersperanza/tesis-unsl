package tesis.test

import tesis.data.CategDto;
import tesis.file.manager.TextFileManager;
import tesis.utils.Utils;
import tesis.data.ItemDto;

class ItemsCounter
{

	static main(args)
	{

		//Leo todas las categs
		def categs = [:]//getCategs();
		//Voy leyendo el archivo y actualizando el contador
		String res
		TextFileManager fm = new TextFileManager("./test_data/items.csv", ";");
		
		if(fm.openFile(0))
		{
			def curItem
			while(curItem = fm.nextItem())
			{
				def categData = categs.get(curItem.categ)
				if(categData)
				{
					/*if(curItem.itemTitle.length() < categData.min)
					{
						categData.min = curItem.itemTitle.length()
					}
					if(curItem.itemTitle.length() > categData.max)
					{
						categData.max = curItem.itemTitle.length()
					}
					categData.total_size = categData.total_size + curItem.itemTitle.length()*/
					categData.total_elems = categData.total_elems + 1
					categs.put(curItem.categ,categData)
				}
				else
				{
					//categs.put(curItem.categ,["min":curItem.itemTitle.length(),"max":curItem.itemTitle.length(), "total_size":curItem.itemTitle.length(), "total_elems":1])
					categs.put(curItem.categ,["total_elems":1])
				}
			}
		}
		else
		{
			throw new Exception("Error al abrir el archivo para lectura/escritura")
		}
		//Imprimo el map
		categs.each { key, value -> 
			try
			{
				//println "$key;$value.min;$value.max;${value.total_size/value.total_elems}" 
				println "$key;$value.total_elems" 
			}
			catch(Exception e)
			{
				println "CATEG: $key;$value - EXCEPTION: $e"
			}
		}

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
				map.put(dto.getCategName(),0)
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

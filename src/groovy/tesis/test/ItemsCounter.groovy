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

			/*while(fm.nextItem())
			{
			}
			print "*"*/
			def curItem
			/*while(curItem = fm.nextItem())
			{
				if(categs.get(curItem.categ))
				{
					categs.put(curItem.categ,categs.get(curItem.categ)+1)
				}
				else
				{
					//println "NO ESTA EN EL ARCHIVO: $curItem.categ"
					categs.put(curItem.categ,1)
				}
			}*/
			
			for(int i=0;i < 100; i++)
			{
				curItem = fm.nextItem()
				println "ORIGINAL: $curItem.itemTitle"
				println "REEMPLAZO: $curItem.searchTitle"
			}
		}
		else
		{
			throw new Exception("Error al abrir el archivo para lectura/escritura")
		}
		//Imprimo el map
		categs.sort{it.value}.each { key, value -> println "$key;$value" }

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

package tesis.test

import tesis.data.CategDto;
import tesis.file.manager.SimpleFileManager;
import tesis.utils.Utils;
import tesis.data.ItemDto;

class ItemsCounter
{

	static main(args)
	{

		//Leo todas las categs
		def categs = getCategs();
		//Voy leyendo el archivo y actualizando el contador
		String res
		SimpleFileManager fm = new SimpleFileManager("/home/lsperanza/items.csv", ";");

		if(fm.openFile(0))
		{
			ItemDto curItem
			while(curItem = Utils.removeSpecialCharacters(fm.nextItem()))
			{
				if(categs.get(curItem.categ))
				{
					categs.put(curItem.categ,categs.get(curItem.categ)+1)
				}
				else
				{
					println "NO ESTA EN EL ARCHIVO: $curItem.categ"
					categs.put(curItem.categ,1)
				}
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
		SimpleFileManager fm = new SimpleFileManager("./test_data/categs.csv", ";");

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

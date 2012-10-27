import tesis.data.ItemDto;

package tesis.test

import tesis.data.CategDto;
import tesis.file.manager.SimpleFileManager;

class ItemsCounter {

	static main(args) {
		
		//Leo todas las categs
		def categs = getCategs();
		//Voy leyendo el archivo y actualizando el contador
		def noCateg=0
		String res
		SimpleFileManager fm = new SimpleFileManager("/home/lsperanza/items.csv", ";");
		
		if(fm.openFile(0))
		{
			ItemDto curItem
				while(curItem = fm.nextItem())
				{
					categs.get(curItem.categ).
					
					int pos = categs.search(new CategDto(categName:,signatures:null))
					if (!categs?.get(pos).equals(categs.virgin)|| categs?.get(pos).equals(categs.used)){
						sig.setItemPosition(rfm.insertItem(curItem))
						sig.setItemSize(curItem.toString().length())
						categs?.get(pos)?.getSignatures()?.add(sig)
					}else{
						noCateg++
					}
				}
				println "Items almacenados en el archivo ${rfm.f.getCanonicalPath()}"
				println "Items no almacenados por categoria invalida: " + noCateg
			}
			else
			{
				throw new Exception("Error al abrir el archivo para lectura/escritura")
			}
		}
		else
		{
			throw new Exception("Error al abrir el archivo $itemsSourceFilePath")
		}
		//Imprimo el map
		println categs
	
	}
	
	def getCategs()
	{
		SimpleFileManager fm = new SimpleFileManager("./test_data/categs.csv", ";");
		
		def map = [:]
		
		if(fm.openFile(0))
		{
			ArrayList<CategDto> list=new ArrayList<CategDto>()
			CategDto dto;
			while((dto = fm.nextCateg()))
			{
				map.put(["categ":dto.getCategName(),"count":0])
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

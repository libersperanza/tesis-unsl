package tesis.test

import tesis.data.CategDto;
import tesis.file.manager.SimpleFileManager;
import tesis.utils.Utils;
import tesis.data.ItemDto;

class ItemsCounter {

	static main(args) {

		//Leo todas las categs
		def categs = [:]//getCategs();
		//Voy leyendo el archivo y actualizando el contador
		String res
		File f = new File("/Users/lsperanza/Documents/Personal/Tesis/Implementacion/LotesDeDatos/items.clean.csv")
		FileReader fr = new FileReader(f);
		BufferedReader bf = new BufferedReader(fr);

		String linea
		String lineaPrev
		try
		{
			while(linea = bf.readLine())
			{

				String[] arLinea = linea.split(";");
				//categ = (arLinea[0]?.indexOf('"')!=-1)?arLinea[0].substring(1,arLinea[0]?.length()-1):arLinea[0]
				String categ = arLinea[0]
				if(categs.get(categ))
				{
					categs.put(categ,categs.get(categ)+1)
				}
				else
				{
					//println "NO ESTA EN EL ARCHIVO: $curItem.categ"
					categs.put(categ,1)
				}
				lineaPrev = linea
			}

			//Imprimo el map
			categs.sort{it.value}.each { key, value -> println "$key;$value" }
		}
		catch(Exception e)
		{
			println lineaPrev
			println linea
			e.printStackTrace()
		}

	}

	/*static getCategs()
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
	 }*/
}

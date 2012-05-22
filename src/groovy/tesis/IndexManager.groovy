/**
 * 
 */
package tesis

import tesis.data.CategDto;
import tesis.data.ItemDto;
import tesis.data.ItemSignature;
import tesis.file.manager.RandomAccessFileManager;
import tesis.file.manager.SimpleFileManager;
import tesis.structure.CategsHash;

/**
 * @author lsperanza
 *
 */
class IndexManager
{
	CategsHash categs = null;
	String categsFilePath = null;
	String itemsSourceFilePath = null;
	String itemsDataFilePath = null;
	String pivotsSourceFilePath = null;
	String dataSeparator = null;
	//Contiene la categoria y la lista de pivotes asociada.
	//Si se utiliza el mismo conjunto de pivotes p/todas
	//las categorias, se carga el par "ALL",[lista_pivotes]
	Map pivots = [:]

	/**
	 * Inicializa la estructura de acuerdo
	 * a los parámetros configurados por defecto
	 * */
	public IndexManager()
	{
		//TODO: Implementar la config
	}

	public IndexManager(String categsFilePath, String itemsSourceFilePath,
						String itemsDataFilePath, String pivotsSourceFilePath, String dataSeparator)
	{
		this.categsFilePath = categsFilePath;
		this.itemsSourceFilePath = itemsSourceFilePath;
		this.itemsDataFilePath = itemsDataFilePath;
		this.pivotsSourceFilePath = pivotsSourceFilePath;
		this.dataSeparator = dataSeparator;
	}
	
	public String initIndex(int cantPivots)
	{
		fillCategsHash();
		fillPivots("ALL",cantPivots);
		createSignatures();
		
	}
	
	public void initIndex(Map cantPivotsByCateg)
	{
		//TODO: Implem metodo
	}
	
	private String fillCategsHash()
	{
		SimpleFileManager fm = new SimpleFileManager(categsFilePath, dataSeparator);
		StringBuilder errors = new StringBuilder()
		
		if(fm.openFile(1))
		{
			ArrayList<CategDto> list=new ArrayList<CategDto>()
			CategDto dto;
			while((dto = fm.nextCateg()))
			{
				list.add(dto)
			}
			fm.closeFile();
			categs = new CategsHash(list.size(), 0.4)
			for(CategDto c:list)
			{
				if(categs.add(c)==-1)
				{
					errors.append ("No se pudo agregar la categoria: ")
					errors.append (c.getCategName())
					errors.append ("<br>")
				}
			}
			return errors
			
		}
		else
		{
			throw new Exception("Error al abrir el archivo")
		}
	}
	private void fillPivots(String categ, int cant)
	{
		SimpleFileManager fm = new SimpleFileManager(pivotsSourceFilePath, dataSeparator);
		String res;
		if(cant <= 50)
		{
			if(fm.openFile(1))
			{
				def pivs = []
				(1..cant).each
				{
					pivs.add(fm.nextItem())
					Random rand = new Random()
					(1..rand.nextInt(5)).each
					{
						fm.nextItem()
					}
				}
				fm.closeFile()
				pivots.put(categ,pivs)
				println "${cant} pivotes cargados con exito"
			}
			else
			{
				throw new Exception("Error al abrir el archivo")
			}
		}
		else
		{
			throw new Exception("Cantidad de pivotes mayor a la permitida (50)")
		}
		
	}
	private createSignatures()
	{
		String res
		SimpleFileManager fm = new SimpleFileManager(itemsSourceFilePath, dataSeparator);
		RandomAccessFileManager rfm = new RandomAccessFileManager(itemsDataFilePath)
		
		if(fm.openFile(1))
		{
			if(rfm.openFile("rw"))
			{
				ItemDto curItem
				while(curItem = fm.nextItem())
				{
					ItemSignature sig = new ItemSignature(curItem.getItemTitle(), getPivotsForCateg(curItem.getCateg()))
					sig.setItemPosition(rfm.insertItem(curItem))
					sig.setItemSize(curItem.toString().length())
					int pos = categs.search(new CategDto(categName:curItem.getCateg()))
					categs.get(pos).getSignatures().add(sig)
				}
				
				println "Items almacenados en el archivo ${rfm.f.getCanonicalPath()}"
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
	}
	
	def getPivotsForCateg(String categName)
	{
		def ret = pivots.get(categName)
		if(!ret)
		{
			ret = pivots.get("ALL")
		}
		return ret
	}
	
	def searchItemsByCateg(){
		}
}

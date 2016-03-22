/**
 * 
 */
package tesis.file.manager

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.codehaus.groovy.grails.web.json.JSONObject

import tesis.data.CategDto;
import tesis.data.ItemDto;
import tesis.data.ItemSignature;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lsperanza
 *
 */
class RandomAccessFileManager
{

	RandomAccessFile objFile;
	File f;
	Map pages;//Aca vamos acumulando las paginas que tenemos
	int pageQty;//tam archivo/4096 (tam pag)
	static long PAGE_SIZE = 4096
	
	public RandomAccessFileManager(String filePath)
	{
		f = new File(filePath);
	}
	
	public boolean openFile(String mode)
	{
		try
		{
			objFile = new RandomAccessFile(f,mode);
			pageQty = objFile.length()/PAGE_SIZE
			println "CANT PAGINAS:$pageQty"
			pages = [:]
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public long insertItem(ItemDto dto)
	{
		long pos=-1;
		try
		{
			pos = objFile.length()
			int curPage = pos/PAGE_SIZE
			int nextPage = (pos + 372)/PAGE_SIZE
			if(nextPage>curPage)
			{
				//Escribo caracteres de relleno
				objFile.writeBytes("    ")
			}
			pos = objFile.length()
			objFile.seek(pos)
			objFile.writeBytes(StringUtils.rightPad(dto.toJSON().toString(),372))
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return pos;
	}
	
	public String getItem(long pos,itemSize)
	{
		//Aca chequeo si tengo la pagina cargada o si la tengo que buscar
		//Nro pag = pos modulo cant pag
		int pagNbr = pos%pageQty
		byte[] data

		if(!pages.get(pagNbr))
		{
			//Seek hasta nro pag*tam pag
			//Busco la pagina en el archivo y la cargo en mem
			long pagePos = pagNbr * PAGE_SIZE
			objFile.seek(pagePos)
			data = new byte[PAGE_SIZE]
			objFile.read(data)
			pages.put(pagNbr,data)
		}
		else
		{
			data = pages.get(pagNbr)
		}
		//pos item = pos mod tam pag
		int posItem = pos % PAGE_SIZE
		String json = new String(Arrays.copyOfRange(data,posItem,posItem+372))
		println json
		return new JSONObject(json.trim())

	}
	
	public boolean closeFile()
	{
		try
		{
			objFile.close();
			pages = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	def seek(pos){
		objFile.seek(pos)
	}
	def resetFile(){
		objFile.setLength(0)
	}
}

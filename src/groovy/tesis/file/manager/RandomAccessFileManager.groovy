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

/**
 * @author lsperanza
 *
 */
class RandomAccessFileManager
{

	RandomAccessFile objFile;
	File f;
	
	public RandomAccessFileManager(String filePath)
	{
		f = new File(filePath);
	}
	
	public boolean openFile(String mode)
	{
		try
		{
			objFile = new RandomAccessFile(f,mode);
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
			objFile.seek(pos)
			objFile.writeBytes(dto.toJSON().toString())
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return pos;
	}
	
	public JSONObject getItem(long pos,itemSize) 
	{
		objFile.seek(pos)
		byte[] data = new byte[itemSize]
		objFile.read(data)
		String json = new String(data)
		return new JSONObject(json)

	}
	
	public boolean closeFile()
	{
		try
		{
			objFile.close();
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

/**
 * 
 */
package tesis.file.manager

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import tesis.data.ItemDto;

/**
 * @author lsperanza
 *
 */
class RandomAccessFileManager
{

	RandomAccessFile itemFile;
	File f;
	
	public RandomAccessFileManager(String filePath)
	{
		f = new File(filePath);
	}
	
	public boolean openFile(String mode)
	{
		try
		{
			itemFile = new RandomAccessFile(f,mode);
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
			pos = itemFile.length() //itemFile.getFilePointer();
			itemFile.seek (pos)
			itemFile.writeBytes(dto.toString())
//			itemFile.writeLong(dto.getItemId());
//			itemFile.writeChars(dto.getCateg());
//			itemFile.writeChars(dto.getItemTitle());
//			itemFile.writeChars(dto.getMainDescription());
//			itemFile.writeChars(dto.getSecDescription());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return pos;
	}
	
	public  String getItem(long pos,itemSize) //ItemDto
	{
		itemFile.seek(pos)
		byte[] data = new byte[itemSize]
		itemFile.read(data)
		return new String(data)

	}
	
	public boolean closeFile()
	{
		try
		{
			itemFile.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	def seek(pos){
		itemFile.seek(pos)
	}
}

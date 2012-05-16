/**
 * 
 */
package tesis.file.manager

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import tesis.data.CategDto;
import tesis.data.ItemDto;
import tesis.data.ItemSignature;

/**
 * @author lsperanza
 *
 */
class SimpleFileManager
{
	File f;
	String lineSeparator;
	FileReader fr;
	BufferedReader bf;
	
	public SimpleFileManager(String filePath, String separator)
	{
		f = new File(filePath);
		lineSeparator = separator;
	}
	
	public boolean openFile(int skipLines)
	{
		try
		{
			fr = new FileReader(f);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		bf = new BufferedReader(fr);
		for(int i=0; i< skipLines; i++)
		{
			try
			{
				bf.readLine();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public boolean closeFile()
	{
		try
		{
			fr.close();
			bf.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public CategDto nextCateg()
	{
		CategDto dto = null;
		try
		{
			String linea;
			if((linea = bf.readLine()) != null)
			{
				String[] arLinea = linea.split(lineSeparator);
				dto = new CategDto(categName:arLinea[0],signatures:new ArrayList<ItemSignature>());
			}
			else
			{
				return null;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return dto;
	}

	public ItemDto nextItem()
	{
		ItemDto dto = null;
		try
		{
			String linea;
			if((linea = bf.readLine()) != null)
			{
				String[] arLinea = linea.split(lineSeparator);
				dto = new ItemDto(itemId:Long.parseLong(arLinea[0]),categ:arLinea[1],itemTitle:arLinea[2],mainDescription:arLinea[3],secDescription:arLinea[4]);
			}
			else
			{
				return null;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return dto;
	}

}

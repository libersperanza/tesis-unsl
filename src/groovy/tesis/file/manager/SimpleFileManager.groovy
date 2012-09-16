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
import java.util.regex.Matcher
import java.util.regex.Pattern;

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
	private static Pattern pattern = Pattern.compile("^([0-9])*\$")
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
		def categ
		def itemId
		String[] arLinea
		try
		{
			String linea;
			if((linea = bf.readLine()) != null)
			{
				arLinea = linea.split(lineSeparator);
				categ = (arLinea[0]?.indexOf('"')!=-1)?arLinea[0].substring(1,arLinea[0]?.length()-1):arLinea[0]
				try{
					def md = arLinea[3]!=null&&arLinea[3]!=""&&arLinea[3]!=" "?arLinea[3]:"No description"
					def sd = arLinea[4]!=null&&arLinea[4]!=""&&arLinea[4]!=" "?arLinea[4]:"No description"
					
					dto = new ItemDto(itemId:Long.parseLong(arLinea[1]),categ:categ,itemTitle:arLinea[2],mainDescription:md,secDescription:"No description");
				}catch(Exception e){
					 println arLinea
					 def md = arLinea[3]!=null&&arLinea[3]!=""&&arLinea[3]!=" "?arLinea[3]:"No description"
					 def sd = arLinea[4]!=null&&arLinea[4]!=""&&arLinea[4]!=" "?arLinea[4]:"No description"
					 categ = (arLinea[1]?.indexOf('"')!=-1)?arLinea[1].substring(1,arLinea[1]?.length()-1):arLinea[1]
					 dto = new ItemDto(itemId:Long.parseLong(arLinea[0]),categ:categ,itemTitle:arLinea[2],mainDescription:md,secDescription:"No description");
				}
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
		catch (Exception e)
		{
			e.printStackTrace();
			println arLinea
			return null;
		}
		return dto;
	}

}

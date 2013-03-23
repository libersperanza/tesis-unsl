/**
 * 
 */
package tesis.structure

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher
import java.util.regex.Pattern

import tesis.data.CategDto;
import tesis.data.ItemSignature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

/**
 * @author lsperanza
 *
 */
class CategsHash
{

	Log log = LogFactory.getLog(CategsHash.class.getName())
	private int size;
	private int elemCount;
	private ArrayList<CategDto> hash;
	public final CategDto virgin = new CategDto(categName:ConfigurationHolder.config.VIRGIN_CELL,signatures:null);
	public final CategDto used = new CategDto(categName:ConfigurationHolder.config.USED_CELL,signatures:null);
	
	public CategsHash(int n, double loadFactor)
	{
		size = (int)((n/ loadFactor)-1);
		hash = new CategDto[size];
		initHash();
	}

	private void initHash()
	{
		for(int i=0; i < hash.size; i++)
		{
			hash[i] = new CategDto(categName:ConfigurationHolder.config.VIRGIN_CELL,signatures:new ArrayList<ItemSignature>());
		}
	}
	
	/**
	 * Funcion de Paso - Determina el tipo de rebalse
	 * @param curPos posición actual
	 * @return siguiente posicion en el rebalse
	 */
	private int nextPosition(int curPos)
	{
		//TODO: Modificar para cambiar el tipo de rebalse
		return (curPos + 1)%size;
	}
	
	public int search(CategDto dto)
	{
		
		int pos = Integer.valueOf(hashCode(dto.categName)).abs()%size;
		def h = hash[pos]
		if(hash[pos].equals(dto))
		{
			return pos;
		}
		
		boolean found = false;
		int firstFree=-1;
		
		while(!found)
		{
			if(!hash[pos].equals(dto))
			{
				if(!hash[pos].equals(virgin))
				{
					if(hash[pos].equals(used))
					{
						if(firstFree == -1)
						{
							firstFree = pos;
						}
						
					}
					pos = nextPosition(pos);
				}
				else
				{
					found =true;
				}
			}
			else
			{
				found = true;
			}
		}
		
		return firstFree!=-1?firstFree:pos;
	}
	
	public int add(CategDto dto)
	{
		int pos = search(dto);
		if(hash[pos].equals(dto))
		{
			return -1;
		}
		else
		{
			hash[pos] = dto;
			elemCount++;
			return pos;
		}
		
	}
	
	public int remove(CategDto dto)
	{
		int pos = search(dto);
		if(hash[pos].equals(dto))
		{
			hash[pos] = new CategDto(categName:ConfigurationHolder.config.USED_CELL);
			elemCount--;
		}
		else
		{
			return -1;
		}
		return pos;
	}

	public int elemCount()
	{
		return elemCount;
	}
	
	public CategDto get(int pos)
	{
		return hash[pos];
	}
	
	public List<CategDto> getValues()
	{
		/*ArrayList<CategDto> list = new ArrayList<CategDto>();
		for(int i=0;i < size; i++)
		{
			if((!hash[i].equals(virgin))&&(!hash[i].equals(used)))
			{
				list.add(hash[i]);
			}
		}
		return list;*/
		return hash;
	}
	
	public void printValues()
	{
		//ArrayList<CategDto> list = new ArrayList<CategDto>();
		for(int i=0;i < size; i++)
		{
			if((!hash[i].equals(virgin))&&(!hash[i].equals(used)))
			{
				log.info hash[i].toString()
				//list.add(hash[i]);
			}
		}
		//return list;
	}
	public int hashCode(String str) {
		int b = 378551;
		int a = 63689;
		int hash = 0;
  
		for(int i = 0; i < str.length(); i++)
		{
		   hash = hash * a + str.charAt(i);
		   a    = a * b;
		}
  
		return hash;
	  }
}

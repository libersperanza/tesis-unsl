/**
 * 
 */
package tesis.structure

import java.util.ArrayList;
import java.util.List;

import tesis.data.CategDto;
import tesis.data.ItemSignature;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;

/**
 * @author lsperanza
 *
 */
class CategsHash
{

	private int size;
	private int elemCount;
	private CategDto []hash;
	private final CategDto virgin = new CategDto(categName:ConfigurationHolder.config.VIRGIN_CELL,signatures:new ArrayList<ItemSignature>());
	private final CategDto used = new CategDto(categName:ConfigurationHolder.config.USED_CELL,signatures:new ArrayList<ItemSignature>());
	
	public CategsHash(int n, double loadFactor)
	{
		size = (int)((loadFactor * n)-1);
		hash = new CategDto[size];
		initHash();
	}

	private void initHash()
	{
		for(int i=0; i < hash.length; i++)
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
		
		int pos = dto.hashCode()%size;
		
		if(hash[pos].equals(dto))
		{
			return pos;
		}
		
		boolean found = true;
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
		ArrayList<CategDto> list = new ArrayList<CategDto>();
		for(int i=0;i < size; i++)
		{
			if((!hash[i].equals(virgin))&&(!hash[i].equals(virgin)))
			{
				list.add(hash[i]);
			}
		}
		return list;
	}
}

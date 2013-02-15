/**
 * 
 */
package tesis.data

import com.sun.xml.internal.bind.v2.util.EditDistance
import org.codehaus.groovy.grails.web.json.JSONObject
import org.codehaus.groovy.grails.web.json.JSONArray

/**
 * @author lsperanza
 *
 */
class ItemSignature
{
	List dists//Array de distancias
	long itemPosition
	long itemSize
	

	public ItemSignature(List dists,long itemPosition, long itemSize){
		this.dists = dists
		this.itemPosition = itemPosition
		if(itemSize){
			this.itemSize = itemSize
		}
	}
	public ItemSignature(String itemTitle, List pivotes)
	{
		dists = new int[pivotes.size()]
		
		for(int i=0; i< pivotes.size(); i++)
		{
			dists[i] = EditDistance.editDistance(itemTitle, pivotes[i].searchTitle)
		}
	}
	@Override
	public boolean equals(ItemSignature obj)
	{
		return (this.dists.equals(obj.dists) && this.itemPosition.equals(obj.itemPosition) && this.itemSize.equals(obj.itemSize))
	}
	
	@Override
	public String toString()
	{
		return "ItemSignature [dists=" + dists + ", itemPosition=" + itemPosition + ", itemSize=" + itemSize + "]";
	}
	
	public JSONObject toJSON()
	{
		JSONObject json = new JSONObject()
		json.put("dists", new JSONArray(dists))
		json.put("itemPosition",itemPosition)
		json.put("itemSize",itemSize)
		return json
	}	
}

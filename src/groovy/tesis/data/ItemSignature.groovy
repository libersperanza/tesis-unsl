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
class ItemSignature implements Serializable
{
	private static final long serialVersionUID = 7208542815446741112L
	/**
	 * @serial
	 */
	int[] dists//Array de distancias
	/**
	 * @serial
	 */
	String itemId
	

	public ItemSignature(List dists, String itemId){
		this.dists = dists
		this.itemId = itemId
	}

	public ItemSignature(String itemId, String itemTitle, List pivotes)
	{	
		this.itemId = itemId
		dists = new int[pivotes.size()]

		
		for(int i=0; i< pivotes.size(); i++)
		{
			dists[i] = EditDistance.editDistance(itemTitle, pivotes[i].searchTitle)
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
		return (this.dists.equals(obj.dists) && this.itemId.equals(obj.itemId))
	}
	
	@Override
	public String toString()
	{
		return "ItemSignature [dists=" + dists + ", itemId=" + itemId + "]";
	}
	
	public JSONObject toJSON()
	{
		JSONObject json = new JSONObject()
		json.put("dists", new JSONArray(dists))
		json.put("itemId",itemId)
		return json
	}	
}

/**
 * 
 */
package tesis.data

import java.io.Serializable

import org.codehaus.groovy.grails.web.json.JSONObject;

/**
 * @author lsperanza
 *
 */
class ItemDto extends PivotDto implements Serializable
{
	String itemTitle
	String mainDescription
	String secDescription
	
	@Override
	public String toString()
	{
		return "ItemDto [itemId=" + itemId + ", categ=" + categ + ", itemTitle=" + itemTitle + ", searchTitle=" + searchTitle + ", mainDescription="+ mainDescription + ", secDescription=" + secDescription + "]";
	}
	public toJSON()
	{
		JSONObject json = new JSONObject()
		json.put("itemId", itemId)
		json.put("categ", categ)
		json.put("itemTitle", itemTitle)
		json.put("searchTitle", searchTitle)
		if(mainDescription?.length()>100)
		{
			json.put("mainDescription", mainDescription?.substring(0,100))
		}
		else
		{
			json.put("mainDescription", mainDescription)
		}
		//json.put("secDescription", secDescription)
		return json
	}
}

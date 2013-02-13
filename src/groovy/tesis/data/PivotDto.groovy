package tesis.data

import org.codehaus.groovy.grails.web.json.JSONObject;

class PivotDto
{
	String itemId
	String categ
	String searchTitle

	@Override
	public String toString()
	{
		return "PivotDto [itemId=" + itemId + ", categ=" + categ + ", searchTitle=" + searchTitle + "]";
	}
	public toJSON()
	{
		JSONObject json = new JSONObject()
		json.put("itemId", itemId)
		json.put("categ", categ)
		json.put("searchTitle", searchTitle)
		return json
	}
}

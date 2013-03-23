package tesis.data

import org.codehaus.groovy.grails.web.json.JSONObject;

class PivotDto implements Serializable
{
	private static final long serialVersionUID = -4811553982963135234L
	/**
	 * @serial
	 */
	String itemId
	/**
	 * @serial
	 */
	String categ
	/**
	 * @serial
	 */
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

/**
 * 
 */
package tesis.data

import java.io.Serializable
import java.util.ArrayList
import org.apache.commons.lang.builder.HashCodeBuilder
import org.codehaus.groovy.grails.web.json.JSONObject;

/**
 * @author lsperanza
 *
 */
class CategDto implements Serializable
{
	private static final long serialVersionUID = 4264549437270341254L
	/**
	 * @serial
	 */
	String categName
	/**
	 * @serial
	 */
	int itemQty
	/**
	 * @serial
	 */
	ArrayList<ItemSignature> signatures
	

	public CategDto()
	{
	}
	
	public CategDto(String categName, int itemQty, ArrayList<ItemSignature> signatures)
	{
		this.categName = categName;
		this.itemQty = itemQty;
		this.signatures = signatures;
	}



	public toJSON()
	{
		JSONObject json = new JSONObject()
		json.put("categName", categName)
		json.put("itemQty", itemQty)
		json.put("signatures", signatures.collect{it.toJSON()})
		return json
	}
	
	@Override
	public String toString()
	{
		//return "CategDto [categName=" + categName + ", itemQty=" + itemQty + "]";
		return "CategDto [categName=" + categName + ", itemQty=" + itemQty + ", signatures=" + signatures + "]";
	}



	@Override
	public boolean equals(CategDto obj){
		return (this.categName.equals(obj.categName))
	}
	
}

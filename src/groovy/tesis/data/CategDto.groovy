/**
 * 
 */
package tesis.data

import java.io.Serializable
import java.util.ArrayList
import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * @author lsperanza
 *
 */
class CategDto implements Serializable
{
	String categName
	ArrayList<ItemSignature> signatures

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "[categName=" + categName + ", signatures=" + signatures + "]";
	}
	@Override
	public boolean equals(CategDto obj){
		return (this.categName.equals(obj.categName))
	}
	
}

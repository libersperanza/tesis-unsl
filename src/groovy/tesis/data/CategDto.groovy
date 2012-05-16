/**
 * 
 */
package tesis.data

import java.io.Serializable
import java.util.ArrayList

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
		return "CategDto [categName=" + categName + ", signatures=" + signatures + "]";
	}	
}

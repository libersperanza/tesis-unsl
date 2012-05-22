/**
 * 
 */
package tesis.data

import java.io.Serializable

/**
 * @author lsperanza
 *
 */
class ItemDto implements Serializable
{
	long itemId
	String categ
	String itemTitle
	String mainDescription
	String secDescription


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "[itemId:" + itemId + ", categ:" + categ + ", itemTitle:" + itemTitle+ ", mainDescription:" + mainDescription + ", secDescription:"+ secDescription +"]";
	}
}

/**
 * 
 */
package tesis.data

import com.sun.xml.internal.bind.v2.util.EditDistance

/**
 * @author lsperanza
 *
 */
class ItemSignature
{
	def dists//Array de distancias
	long itemPosition
	long itemSize
	

	public ItemSignature(def dists,def itemPosition, def itemSize){
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
			dists[i] = EditDistance.editDistance(itemTitle, pivotes[i].getItemTitle())
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return '{"dists":' + dists + ', "itemPosition":' + itemPosition + ', "itemSize":' + itemSize + '}';	
	}
	@Override
	public boolean equals(ItemSignature obj){
		return (this.dists.equals(obj.dists) && this.itemPosition.equals(obj.itemPosition) && this.itemSize.equals(obj.itemSize))
	}	
}

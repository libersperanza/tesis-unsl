package tesis.data

import java.util.List;

import tesis.utils.Utils;

import com.sun.xml.internal.bind.v2.util.EditDistance;

class ElementsPairs {
	PivotDto a
	List aDists
	PivotDto b
	List bDists
	public void initDist(pivotsQty,pivot){
		aDists = new int[pivotsQty]
		bDists = new int[pivotsQty]
		for (int i=0; i< pivotsQty;i++){
			aDists[i]=-1
			bDists[i]=-1
		}
		addDistance (pivot)
	}
	public void addDistance(pivot){			
		aDists[Utils.firtsFree(aDists)]= EditDistance.editDistance(a.searchTitle, pivot.searchTitle)
		bDists[Utils.firtsFree(bDists)]= EditDistance.editDistance(b.searchTitle, pivot.searchTitle)
	}
}

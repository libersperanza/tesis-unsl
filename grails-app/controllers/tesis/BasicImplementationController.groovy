package tesis

import org.codehaus.groovy.grails.web.json.JSONObject
import tesis.file.manager.RandomAccessFileManager

class BasicImplementationController
{
	def index =
	{
	} 

	def initIndex =
	{
		IndexManager mgr = new IndexManager(params.file_name_cat, params.file_name_it,
				"./test_data/Items.dat", params.file_name_piv, params.separator);
		try
		{
			String result = mgr.initIndex(Integer.parseInt(params.cant))
			session.categs = mgr.categs
			session.pivots = mgr.pivots
			if(!result)
			{
				result = "INICIALIZACION CORRECTA"
			}
			render(view:"fillFile", model:[result:result])
		}
		catch(Exception e)
		{
			e.printStackTrace()
			render(view:"fillFile", model:[result:e.message])
		}
	}
	def listCategs =
	{
		render(view:"list", model:[tit:"Categorias",lista:session.categs.getValues()])
	}

	def listPivotes =
	{
		render(view:"list", model:[tit:"Pivotes", lista:session.pivots.values()])
	}
	def searchItems =
	{ render(view:"searchItems") }
	def listItems =
	{ render(view:"sequentialSearch") }
	def searchItemsCateg =
	{
		IndexManager mgr = new IndexManager(session.categs,session.pivots)
		int radio = Integer.valueOf(params.radio)
		def signatures = mgr.searchItemsByCateg(params.itemTitle, params.categ, radio)
		def itemsFound = null
		RandomAccessFileManager rfm = new RandomAccessFileManager("./test_data/Items.dat")
		if (signatures){
			if (rfm.openFile("rw"))
			{
				itemsFound = new ArrayList()
				signatures.each
				{
					def item =  new JSONObject(rfm.getItem(it.itemPosition,it.itemSize))
					def dist = EditDistance.editDistance(params.itemTitle, item.itemTitle)
					if(dist < radio)
					{
						itemsFound.add(item)
					}
					println item
				}
				rfm.closeFile()
			}
		}
		render(view:"searchItems", model:[tit:"Items",itemsFound:itemsFound])
	}

	def sequentialSearch=
	{
		
		IndexManager mgr = new IndexManager(session.categs,session.pivots)
		int radio = Integer.valueOf(params.radio)
		def signatures =  mgr.searchItemsByCateg(params.itemTitle, params.categ, radio)
		def itemsFound = null
		RandomAccessFileManager rfm = new RandomAccessFileManager("./test_data/Items.dat")
		if (signatures){
			if (rfm.openFile("rw"))
			{
				itemsFound = new ArrayList()
				signatures.each
				{
					def item =  new JSONObject(rfm.getItem(it.itemPosition,it.itemSize))
					itemsFound.add(item)
					println item
				}
				rfm.closeFile()
			}
		}
		render(view:"sequentialSearch", model:[tit:"Items",itemsFound:itemsFound])
//		StringBuilder stats = new StringBuilder();
//		def itemsFound = new ArrayList()
//		int radio
//		if(params.categ)
//		{
//			radio = Integer.valueOf(params.radio)
//			int pos = session.categs.search(new CategDto(categName:params.categ,signatures:null))
//			RandomAccessFileManager rfm = new RandomAccessFileManager("./test_data/Items.dat")
//			if (rfm.openFile("rw"))
//			{
//				stats.append("Cantidad de items en categ: ${session.categs.get(pos)?.signatures?.size()} ")
//				def signatures = session.categs.get(pos)?.signatures
//				signatures.each
//				{
//					def item =  new JSONObject(rfm.getItem(it.itemPosition,it.itemSize))
//					def dist = EditDistance.editDistance(params.itemTitle, item.itemTitle)
//					if( dist < radio)
//					{
//						itemsFound.add(item)
//					}
//					println item
//				}
//				rfm.closeFile()
//				stats.append("Cantidad de items que matchean la busqueda: ${itemsFound.size()}")
//			}
//		}
//		render(view:"sequentialSearch", model:[tit:"Items", info:stats.toString(),itemsFound:itemsFound])
	}
}

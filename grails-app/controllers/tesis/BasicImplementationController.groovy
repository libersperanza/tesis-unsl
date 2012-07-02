package tesis
import org.codehaus.groovy.grails.web.json.JSONObject;
import tesis.data.ItemSignature;
import java.util.ArrayList;
import tesis.data.ItemSignature;
import java.util.ArrayList;

import org.apache.commons.validator.routines.LongValidator;

import com.sun.xml.internal.bind.v2.util.EditDistance;

import tesis.data.CategDto;
import tesis.data.ItemDto;
import tesis.file.manager.RandomAccessFileManager;
import tesis.file.manager.SimpleFileManager;
import tesis.structure.CategsHash;
import tesis.data.ItemSignature;

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
	def listItems = {
		render(view:"searchItems")
	}
	def listItmesCateg =
	{	
//		CategsHash categs = session.categs
//		int pos = categs.search(new CategDto(categName:String.valueOf(params.categ),signatures:new ArrayList<ItemSignature>()))
//		def signatures = categs.hash[pos]
//		def itemsFound = null
//		RandomAccessFileManager rfm = new RandomAccessFileManager("./test_data/Items.dat")
//		if (signatures?.signatures){
//			if (rfm.openFile("rw")){
//				itemsFound = new ArrayList()
//				signatures?.signatures?.each{
//					def item =  new JSONObject(rfm.getItem(it.itemPosition,it.itemSize))
//					itemsFound.add(rfm.getItem(it.itemPosition,it.itemSize))
//				}
//				rfm.closeFile()
//			}
//		}
		//render(view:"searchItems", model:[tit:"Items", lista:session.firmas,itemsFound:itemsFound])
		IndexManager mgr = new IndexManager(session.categs,session.pivots)
		mgr.searchItemsByCateg(params,session)
		def signatures = session?.candidatos
		def itemsFound = null
		RandomAccessFileManager rfm = new RandomAccessFileManager("./test_data/Items.dat")
		if (signatures){
			if (rfm.openFile("rw")){
				itemsFound = new ArrayList()
				signatures.each{
					def item =  new JSONObject(rfm.getItem(it.itemPosition,it.itemSize))
					def g = EditDistance.editDistance(params?.itemTitle, item?.itemTitle)
					if(EditDistance.editDistance(params?.itemTitle, item?.itemTitle)< Integer.valueOf(params?.radio)){
						itemsFound.add(item)
					}
				}
				rfm.closeFile()
			}			
		}
		render(view:"searchItems", model:[tit:"Items", lista:session.firmas,itemsFound:itemsFound])
	}
}

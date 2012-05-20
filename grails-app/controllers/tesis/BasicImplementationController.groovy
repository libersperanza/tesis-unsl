package tesis

import java.util.ArrayList;

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
			session.mgr = mgr
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
		render(view:"list", model:[tit:"Categorias",lista:session.mgr.categs.getValues()])
	}
	
	def listPivotes =
	{
		render(view:"list", model:[tit:"Pivotes", lista:session.mgr.pivots.values()])
	}
}

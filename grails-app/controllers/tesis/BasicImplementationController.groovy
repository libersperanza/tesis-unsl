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
    def index = { }
	
	def fillCategs =
	{
		SimpleFileManager fm = new SimpleFileManager(params.file_name, params.separator);
		StringBuilder errors = new StringBuilder()
		
		if(fm.openFile(1))
		{
			ArrayList<CategDto> list=new ArrayList<CategDto>()
			CategDto dto;
			while((dto = fm.nextCateg()))
			{
				list.add(dto)
			}
			fm.closeFile();
			session.hash = new CategsHash(list.size(), 0.4)
			for(CategDto c:list)
			{
				if(session.hash.add(c)==-1)
				{
					errors.append ("No se pudo agregar la categoria: ")
					errors.append (c.getCategName())
					errors.append ("<br>")
				}
			}
			render(view:"fillFile", model:[file:"Categorias",result : "Se termino de crear el hash de categorias.<br/> Información extra:"+errors.toString()])
			
		}
		else
		{
			render(view:"fillFile", model:[file:"Categorias",result : "Error al abrir el archivo"])
		}
	}
	
	def fillPivotes =
	{
		SimpleFileManager fm = new SimpleFileManager(params.file_name, params.separator);
		int cant = Integer.parseInt(params.cant)
		String res;
		if(cant <= 50)
		{
			if(fm.openFile(1))
			{
				session.pivotes = []
				(1..cant).each
				{
					session.pivotes.add(fm.nextItem())
					Random rand = new Random()
					(1..rand.nextInt(5)).each
					{
						fm.nextItem()
					}
				}
				fm.closeFile()
				res="${params.cant} pivotes cargados con exito"
			}
			else
			{
				res= "Error al abrir el archivo"
			}
		}
		else
		{
			res = "Cantidad de pivotes mayor a la permitida (50)"
		}
		
		render(view:"fillFile", model:[file:"Pivotes", result:res])
	}
	
	def listCategs =
	{
		render(view:"list", model:[tit:"Categorias",lista:session.hash.getValues()])
	}
	
	def listPivotes =
	{
		render(view:"list", model:[tit:"Pivotes", lista:session.pivotes])
	}
	
	def createSignatures=
	{
		String res
		CategsHash categs = session.hash
		ItemDto []pivots = session.pivotes
		SimpleFileManager fm = new SimpleFileManager(params.file_name, params.separator);
		RandomAccessFileManager rfm = new RandomAccessFileManager("/home/lsperanza/Documents/workspace-sts-2.5.1.RELEASE/TesisFullGroovy/test_data/Items.dat")
		
		if(fm.openFile(1))
		{
			if(rfm.openFile("rw"))
			{
				ItemDto curItem
				while(curItem = fm.nextItem())
				{
					ItemSignature sig = new ItemSignature(curItem.getItemTitle(), pivots)
					sig.setItemPosition(rfm.insertItem(curItem))
					int pos = categs.search(new CategDto(categName:curItem.getCateg()))
					categs.get(pos).getSignatures().add(sig)
				}
				res="Items almacenados en el archivo /home/lsperanza/Items.dat"
			}
			else
			{
				res = "Error al abrir el archivo para lectura/escritura"
			}
		}
		else
		{
			res = "Error al abrir el archivo $params.file_name"
		}
		
		render(view:"fillFile", model:[file:"Items", result:res])
	}
}

package tesis.test

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject;

import com.javamex.classmexer.MemoryUtil;
import com.javamex.classmexer.MemoryUtil.VisibilityFilter;
import tesis.data.CategDto
import tesis.data.ItemSignature
import tesis.file.manager.TextFileManager

class TestObjectSize {

	static main(args) {
		
		CategDto dto = getCategDto()
		
		JSONObject categJson = dto.toJSON()
		String jsonString  = categJson.toString()
		
		long dtoDeepSize = MemoryUtil.deepMemoryUsageOf(dto, VisibilityFilter.ALL);
		long dtoSize = MemoryUtil.memoryUsageOf(dto);
		long jsonSize = MemoryUtil.deepMemoryUsageOf(categJson, VisibilityFilter.ALL);
		long stringJsonSize = MemoryUtil.deepMemoryUsageOf(jsonString, VisibilityFilter.ALL);
		
		//long total = ((dtoSize + jsonSize + stringJsonSize)/1024) * 13071
		long total = (jsonSize/1024) * 13071
		
		println "Categoria: $dto.categName Cant Items: $dto.signatures.size"
		println "TAMAÑO DEL DTO ${dtoSize/1024}KB"
		println "TAMAÑO DEL JSON ${jsonSize/1024}KB" 
		println "TAMAÑO DEL JSON STRING  ${stringJsonSize/1024}KB" 
		println "TAMAÑO TOTAL ${(total/1024)/1024} GB"
	
	}

	static CategDto getCategDto()
	{
		TextFileManager fm = new TextFileManager("./test_data/categs.dat","\n")
		
		if(fm.openFile(0))
		{
			String categLine = fm.nextLine()
			JSONObject jsonCateg = new JSONObject(categLine)
			JSONArray jsonSigs = new JSONArray(jsonCateg.signatures)
			ArrayList signatures = new ArrayList<ItemSignature>(jsonSigs.size())
			for(JSONObject s in jsonSigs){
					signatures.add(new ItemSignature(s.dists, s.itemPosition, s.itemSize))
			}
			fm.closeFile()
			return new CategDto(jsonCateg.categName,signatures)
		}
	}
}

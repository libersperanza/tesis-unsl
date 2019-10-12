/**
 * 
 */
package tesis.file.manager

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher
import java.util.regex.Pattern;

import tesis.data.CategDto;
import tesis.data.ItemDto;
import tesis.data.ItemSignature;
import tesis.data.PivotDto
import tesis.utils.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory

/**
 * @author lsperanza
 *
 */
class TextFileManager {
	File f;
	String lineSeparator;
	FileReader fr;
	BufferedReader bf;
	FileInputStream fis;

	Log log = LogFactory.getLog(TextFileManager.class.getName())
	
	public TextFileManager(String filePath, String separator) {
		f = new File(filePath);
		lineSeparator = separator;
	}

	public boolean openFile(int skipLines) {
		try {
			//fr = new FileReader(f);
			fis = new FileInputStream(f)
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		bf = new BufferedReader(new InputStreamReader(fis, "UTF8"));
		for(int i=0; i< skipLines; i++) {
			try {
				bf.readLine();
			}
			catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	public boolean closeFile() {
		try {
			//fr.close();
			fis.close();
			bf.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public CategDto nextCateg() {
		CategDto dto = null;
		try {
			String linea;
			if((linea =  bf.readLine()) != null) {
				String[] arLinea = linea.split(lineSeparator);
				dto = new CategDto()
				dto.categName = arLinea[0]
				dto.itemQty = Integer.parseInt(arLinea[2])
				dto.pivoteQtys = arLinea[1].split(",").collect{Integer.parseInt(it.toString())}
				dto.signatures = new ArrayList<ItemSignature>();
			}
			else {
				return null;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return dto;
	}

	public ItemDto nextItem() {
		ItemDto dto = null;
		def categ
		String linea;
		String[] arLinea;
		try {

			if((linea = bf.readLine()) != null) {
				linea = linea
				arLinea = linea.split(lineSeparator);
				dto = new ItemDto()
				def map = getCommonData(arLinea)
				dto.categ = map.categ
				dto.itemId = map.itemId
				dto.searchTitle = map.searchTitle
				
				dto.itemTitle = arLinea[2]
				if (arLinea.size()==5) {
					dto.mainDescription = arLinea[3]
					dto.secDescription = arLinea[4]
				}
				if (arLinea.size()==4) {
					dto.mainDescription = arLinea[3]
				}
			}else {
				return null;
			}
		}
		catch (Exception e) {
			log.info linea
			log.info arLinea
			e.printStackTrace();
			return null;
		}
		return dto;
	}
	
	
	public PivotDto nextPivot() {
		PivotDto dto = null;
		String linea;
		String[] arLinea;
		try {

			if((linea = bf.readLine()) != null) {
				arLinea = linea.split(lineSeparator);
				
				dto = new PivotDto();
				def map = getCommonData(arLinea)
				dto.categ = map.categ
				dto.itemId = map.itemId
				dto.searchTitle = map.searchTitle
			}else {
				return null;
			}
		}
		catch (Exception e) {
			log.info linea
			log.info arLinea
			e.printStackTrace();
			return null;
		}
		return dto;
	}

	private getCommonData(String[] arLinea) {
		return [itemId:arLinea[1],categ:arLinea[0],searchTitle:Utils.removeSpecialCharacters(arLinea[2])]
	}
}

/**
 * 
 */
package tesis.file.manager

import java.io.File;

/**
 * @author lsperanza
 *
 */
class ObjectFileManager
{
	File f;
	FileInputStream fis;
	ObjectInputStream ois;
	FileOutputStream fos;
	ObjectOutputStream oos;
	
	public ObjectFileManager(String filePath)
	{
		f = new File(filePath);
	}
	
	public boolean openFile(String mode)
	{
		try
		{
			if("W".equals(mode))
			{
				fos = new FileOutputStream(f);
				oos = new ObjectOutputStream(fos);
				return true
			}
			if("R".equals(mode))
			{
				fis = new FileInputStream(f);
				ois = new ObjectInputStream(fis);
				return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false
	}
	
	public closeFile()
	{
		oos?.close()
		fos?.close()
		ois?.close()
		fis?.close()
	}
	
	public void writeObject(Object dto)
	{
		oos.writeObject(dto)
	}
	
	public Object readObject()
	{
		return ois.readObject()
	}
}

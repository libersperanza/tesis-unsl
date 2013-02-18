package tesis.utils

class Utils {
	public static def removeSpecialCharacters(str){
		if(str == null){
			return null;
		}
		String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ!¡¿?ªº@#¢∞¬÷“”≠´‚][}{-_;:¨^*().&%\$,+";
		String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC                                     ";
		String output = str;
		for (int i=0; i<original.length(); i++) {
			output = output.replace(original.charAt(i), ascii.charAt(i));
		}
		StringBuilder ret = new StringBuilder()
		def stringList = output.split(" ")
		stringList.each
		{
			s ->
			if(s.trim().length() > 0)
			{
				ret.append(s)
				ret.append(" ")
			}
		}
		return ret.toString().trim()
	}
	public static def firtsFree(listD){
		int i = 0
		for(i=0;i<listD?.size();i++){
			if(listD[i]==-1){
				return i
			}
		}
		return i
	}
}

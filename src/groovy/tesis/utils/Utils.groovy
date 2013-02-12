package tesis.utils

class Utils {
	public static String removeSpecialCharacters(str){
		if(str == null){
			return null;
		}
		String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
		String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
		String output = str;
		for (int i=0; i<original.length(); i++) {
			output = output.replace(original.charAt(i), ascii.charAt(i));
		}
		return output.replaceAll("\\s+", " ")
	}
	public static String parseString(str){
		str?.replaceAll("\\s+", " ")?.replaceAll("\"","'")
	}	
}

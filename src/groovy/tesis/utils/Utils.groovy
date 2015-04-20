package tesis.utils

import java.lang.management.*;


class Utils {
	public static String removeSpecialCharacters(str){
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
	/**
	 * retorna el primer elemento libre
	 * @param listD
	 * @return
	 */
	public static def firtsFree(listD){
		int i = 0
		for(i=0;i<listD?.size();i++){
			if(listD[i]==-1){
				return i
			}
		}
		return i
	}

	/** Get CPU time in nanoseconds. */
	public static long getCpuTime(def ids ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    if ( ! bean.isThreadCpuTimeSupported( ) )
	        return 0L;
	    long time = 0L;
	    for (def id : ids ) {
	    	long t = bean.getThreadCpuTime(id);
	        if ( t != -1 )
	            time += t;
	    }
	    return time;
	}
 
	/** Get user time in nanoseconds. */
	public static long getUserTime(def ids ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    if ( ! bean.isThreadCpuTimeSupported( ) )
	        return 0L;
	    long time = 0L;
	    for (def id : ids ) {
	        long t = bean.getThreadUserTime(id);
	        if ( t != -1 )
	            time += t;
	    }

	    return time;
	}
}

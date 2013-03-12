package tesis.test

import tesis.data.CategDto;
import tesis.data.ItemSignature;
import tesis.data.PivotDto;

import com.sun.xml.internal.bind.v2.util.EditDistance;

class EditDistanceTest {
	

	static main(args) {
		/*int ed = EditDistance.editDistance (args[0],args[1])
		println("La distancia entre ${args[0]} y ${args[1]} es: $ed");*/
		
		/**
		 * Get serialVersionUID of a class that implements Serializable. Especially usefull for client server applications with different
		 * versions of client and server but without changes of the serializable objects they exchange. Insert the resulting value as
		 * <code>static final long serialVersionUID = <result of this method>L;</code> in your serializable class.
		 *
		 * @param javaClass Class to get serialVersionUID for.
		 * @return The serialVersionUID for <i>javaClass</i> or 0 if failed.
		 * @throws NotSerializableException if <i>javaClass</i> does not implement Serializable.
		 */
		Class javaClass = PivotDto.class
			long result = 0;
			
			if (javaClass != null) {
				//check if class implements Serailizable:
				Class[] classes = javaClass.getInterfaces();
				if (classes != null) {
					for (Class c : classes) {
						if (c == Serializable.class) {
							result = ObjectStreamClass.lookup(javaClass).getSerialVersionUID();
							break;
						}
					}//next interface
					if (result == 0) {
						throw new NotSerializableException("Class '"+javaClass.getName()+"' does not implement "+Serializable.class);
					}
				}//else: interfaces unavailable
			}//else: input unavailable
			
			println result;
		}//getSerialVersionUID()

}

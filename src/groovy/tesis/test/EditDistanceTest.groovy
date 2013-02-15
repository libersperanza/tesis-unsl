package tesis.test

import com.sun.xml.internal.bind.v2.util.EditDistance;

class EditDistanceTest {
	

	static main(args) {
		int ed = EditDistance.editDistance (args[0],args[1])
		println("La distancia entre ${args[0]} y ${args[1]} es: $ed");
	
	}

}

package tesis.test

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject;

import com.javamex.classmexer.MemoryUtil;
import com.javamex.classmexer.MemoryUtil.VisibilityFilter;
import tesis.data.CategDto
import tesis.data.ItemSignature
import tesis.file.manager.TextFileManager
import java.lang.reflect.Field;
import sun.misc.Unsafe;


class TestNewVersion {

	static main(args) {
		
		Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe)f.get(null);

        int pageSize = unsafe.pageSize();
        System.out.println("Page size: ${pageSize/1024}KB");
	}
}

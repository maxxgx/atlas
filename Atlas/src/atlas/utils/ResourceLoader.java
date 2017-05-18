package atlas.utils;

import java.io.InputStream;
import java.net.URL;

public class ResourceLoader {

	public static InputStream loadAsStream(String path) {
		InputStream input = ResourceLoader.class.getResourceAsStream(path);
		if(input == null) {
			input = ResourceLoader.class.getResourceAsStream("/" + path);
		}
		return input;
	}
	
	public static URL loadAsURL(String path) {
		return ResourceLoader.class.getResource("/"+path);
	}
}

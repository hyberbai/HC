package hylib.toolkits;

public class type {

	public static <T> T as(Object o, Class<T> tClass) {
		return tClass.isInstance(o) ? (T) o : null;
	}
	
}

package hylib.util;

import java.security.PublicKey;
import java.util.HashMap;

public class IdMap extends HashMap<String, Integer> {
	
	public static class Entry {
		public String Key;
		public int Value;
		
		public Entry(String key, int value){
			Key = key;
			Value = value;
		}
	}

	@Override
	public Integer put(String key, Integer value) {
		return super.put(key.toUpperCase(), value);
	}

	@Override
	public Integer get(Object key) {
		return super.get(((String)key).toUpperCase());
	}

	public void addRange(Entry... values) {
		for (Entry v : values) {
			put(v.Key, v.Value);
		}
	}
}

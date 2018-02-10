package com.dev;

import java.util.ArrayList;

public class DataList extends ArrayList<byte[]> {
	
	public byte[] toBytes(){
		int len = 0;
		for (byte[] item: this) len += item.length;
		byte[] result = new byte[len];
		int pos = 0;
		for (byte[] item: this) { 
			System.arraycopy(item, 0, result, pos, item.length);
			pos += item.length;
		}
		return result;
	}

}

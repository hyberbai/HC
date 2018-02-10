package hylib.toolkits;

import java.util.ArrayList;

public class BytesBuffer {
	private ArrayList<byte[]> list;
	
	public BytesBuffer() {
		list = new ArrayList<byte[]>();
	}
	
	public void append(byte[] bytes) {
		list.add(bytes);
	}

	public void append(byte[] bytes, int pos, int len) {
		byte[] bs = new byte[len];
		System.arraycopy(bytes, 0, bs, pos, len);
		list.add(bs);
	}
	
	public byte[] toBytes() {
		int count = 0;
		for(int i = 0; i < list.size(); i++)
			count += list.get(i).length;
		byte[] bytes = new byte[count];
		
		int pos = 0;
		for(int i = 0; i < list.size(); i++) {
			int len = list.get(i).length;
			System.arraycopy(list.get(i), 0, bytes, pos, len);
			pos += len;
		}
		return bytes;
	}
}

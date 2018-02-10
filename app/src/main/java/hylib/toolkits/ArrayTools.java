package hylib.toolkits;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.R.integer;

public class ArrayTools {

	
	@SuppressWarnings("unchecked")
	public static <T> T[] newGenicArray(T[] A, int len) {
		return (T[]) Array.newInstance(A.getClass().getComponentType(), len);
	}
	
	public static <T> T[] newGenicArray(T[] A, T... values) {
		T[] array = newGenicArray(A, values.length);
		for (int i = 0; i < values.length; i++)
			array[i] = values[i];
		return array;
	}

    public static <T> T[] Insert(T[] A, int index, T item)
    {
        if (index > A.length) index = A.length;
        if (item == null) return A;
        if (A == null || A.length == 0) return newGenicArray(A, item);
        T[] result = newGenicArray(A, A.length + 1);
        System.arraycopy(A, 0, result, 0, index);
        result[index] = item;
        System.arraycopy(A, index, result, index + 1, A.length - index);
        return result;
    }


    public static <T> T[] Copy(T[] A, int index, int len)
    {
    	if(A.length < index + len) len = A.length - index;
        T[] result = newGenicArray(A, len);
        System.arraycopy(A, index, result, 0, len);
        return result;
    }

    public static byte[] Copy(byte[] A, int index, int len)
    {
    	if(A.length < index + len) len = A.length - index;
    	byte[] result = new byte[len];
        System.arraycopy(A, index, result, 0, len);
        return result;
    }

    public static int[] Delete(int[] A, int index)
    {
        if (A == null) return null;
        if (index > A.length) return A;
        int[] result = new int[A.length - 1];
        for(int i = 0, j = 0; i < result.length; i++, j++)
        {
        	if(j == index) j++;
        	result[i] = A[j];
        }
        return result;
    }
    
    public static HashMap<String, Object> toHashMap(Object[] array, gi.IFunc1<Object, String> hashFunc) {
    	HashMap<String, Object> map = new HashMap<String, Object>();
		return toHashMap(map, array, hashFunc);
	}
    
    public static HashMap<String, Object> toHashMap(HashMap<String, Object> map, Object[] array, gi.IFunc1<Object, String> hashFunc) {
		if(array != null)
			for (Object o : array) map.put(hashFunc.Call(o), o);
		return map;
	}

    /*
     * 取数组差集
     */
    public static Object[] Except(Object[] A, Object[] B, gi.IFunc1<Object, String> hashFunc)
    {
    	HashMap<String, Object> mapA = toHashMap(A, hashFunc);
    	HashMap<String, Object> mapB = toHashMap(B, hashFunc);

    	List<Object> list = new ArrayList<Object>();
    	for(Entry<String, Object> e : mapA.entrySet())   
    		if (!mapB.containsKey(e.getKey())) list.add(e.getValue());
    	return list.toArray();
    }
    
    /*
     * 取数组交集
     */
    public static Object[] Intersect(Object[] A, Object[] B, gi.IFunc1<Object, String> hashFunc)
    {
    	HashMap<String, Object> mapA = toHashMap(A, hashFunc);
    	HashMap<String, Object> mapB = toHashMap(B, hashFunc);

    	List<Object> list = new ArrayList<Object>();
    	for(Entry<String, Object> e : mapA.entrySet())   
    		if (mapB.containsKey(e)) list.add(e.getValue());
    	return list.toArray();
    }

    /*
     * 取数组并集
     */
    public static Object[] Union(Object[] A, Object[] B, gi.IFunc1<Object, String> hashFunc)
    {
    	HashMap<String, Object> map = toHashMap(A, hashFunc);
    	toHashMap(map, B, hashFunc);

    	List<Object> list = new ArrayList<Object>();
    	for(Entry<String, Object> e : map.entrySet())   
    		list.add(e.getValue());
    	return list.toArray();
    }
}

package hylib.toolkits;

import java.security.MessageDigest;

public class Security {
	
    public static String getMD5(String message) {  
	    try {  
	        // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象  
	        MessageDigest md = MessageDigest.getInstance("MD5");  
	  
	        // 2 将消息变成byte数组  
	        byte[] input = message.getBytes();  
	  
	        // 3 计算后获得字节数组,这就是那128位了  
	        byte[] buff = md.digest(input);  
	  
	        // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串  
	        return gv.BytesToHex(buff);  
	  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return "";  
    }  
}

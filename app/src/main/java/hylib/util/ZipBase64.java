package hylib.util;

import hylib.toolkits._D;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import sun.misc.BASE64.BASE64Decoder;
import sun.misc.BASE64.BASE64Encoder;

public class ZipBase64 {
	public static final String ZB64 = "#ZB64:";
	
	public static boolean IsZB64Code(String code)
	{
		if(code.length() < ZB64.length()) return false;
		return code.substring(0, ZB64.length()).equals(ZB64);
	}

	public static String RemoveZB64Header(String code)
	{
		return code.substring(ZB64.length(), code.length());
	}
	
    /// <summary>
    /// 编码，先压缩后编码为Base64
    /// </summary>
    public static String Encode(byte[] data) throws IOException 
    {
    	ByteArrayOutputStream out = new ByteArrayOutputStream ();

    	GZIPOutputStream zs = new GZIPOutputStream(out);
        try
        {
            zs.write(data);
            zs.flush();
        }
        finally
        {
            zs.close(); 
        }
        data = out.toByteArray();
        return new BASE64Encoder().encode(data);
    }

    public static String Encode(String TextData) throws Exception 
    {
        return Encode(TextData.getBytes("UTF-8"));
    }

    public static String EncodeZB64(String TextData) throws Exception
    {
        return ZB64 + Encode(TextData);
    }

    /// <summary>
    /// 解码，先解码Base64数据，后解缩还原数据
    /// </summary>
    public static byte[] Decode(String code) throws IOException
    {
    	if(code == null || code.isEmpty()) return null;
    	
    	ByteArrayInputStream in = new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(code));
    	ByteArrayOutputStream out = new ByteArrayOutputStream();

        GZIPInputStream zs = new GZIPInputStream(in);

        try { 
            byte[] buffer = new byte[1024 * 16]; 
            int n; 
            while ((n = zs.read(buffer)) >= 0) 
                out.write(buffer, 0, n); 
        } finally { 
            zs.close();
        }
        in.close();
        out.close();
        return out.toByteArray();
    }

    public static String DecodeText(String code) throws IOException
    {
        return new String(Decode(code), "UTF-8");
    }

    public static String decodeZB64Code(String code) throws IOException
    {
    	if(!IsZB64Code(code)) return code;
    	code = RemoveZB64Header(code);
        return DecodeText(code);
    }
    
    public static void Test()
    {
    	try
    	{
            String code = Encode("public static void Test()");
            String text = DecodeText(code);
    	}
    	catch(Exception e){
    		
    	}
    }
}

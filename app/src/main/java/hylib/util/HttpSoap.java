package hylib.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hylib.data.DataTable;
import hylib.toolkits.BytesBuffer;
import hylib.toolkits.ExProc;
import hylib.toolkits.MsgException;
import hylib.toolkits._D;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;


public class HttpSoap {
	public static final String ERR_TAG = "<Error>";		// Soap 返回错误标签
	
	private static boolean mConnected;
	private static int RETRY_CONNECT_SEC = 0;//5 * 60;	// 尝试重启连接的时间秒数
	private static long mT0 = 0;
	private static int MAX_CONNECT_TIMEOUT = 3000;		// 设置最大连接超时时间
	private static String mServerAddr = "";
	public static boolean silence = false;
	public static boolean AllowZB64 = false;		// 压缩编码
	
	public static boolean IsConected(){
		return mConnected;
	}
	
	public static boolean CheckConnected() {
		if(mConnected) return true;
		if(new Date().getTime()/1000 - mT0 > RETRY_CONNECT_SEC) return true;
		return false;
	}
	
	public static void SetConnected(boolean connected) {
		if(mConnected == connected) return;
		//if(!connected) mT0 =;
		mT0 = new Date().getTime()/1000;
		mConnected = connected;
	}

	public static void SetServerAddr(String addr) {
		mServerAddr = addr;
	}
	
	public static String GetServerAddr() {
		return mServerAddr;
	}
	
	// 连接测试
	public static boolean Connect() throws Exception {
		SetConnected(true);
		try {
			boolean IsConnected = (Boolean)InvokeWebService("Connected", null, false);
			if(!IsConnected) SetConnected(false); 
			return IsConnected;
		} catch (Exception e) {
			SetConnected(false); 
			throw e;
		}
	}
	
	private static String CDATA(String data) {
		return "<![CDATA[" + data + "]]>";
	}
	
	public static Object InvokeWebService(String methodName, Param[] Params, boolean IsEntryMode) throws Exception {
		if(!CheckConnected()) return null;
		if(mServerAddr.equals("")) ExProc.ThrowMsgEx("服务器地址未设置！");

		//mServerAddr = "192.168.0.10";
		String ServerUrl = "http://" + mServerAddr + "/HC.asmx";

		/* SOAP头的参数格式
			<methodName xmlns="http://tempuri.org/">
				<Param1>ParValue1</Param1>"
				<Param2>ParValue2</Param2>"
			</methodName>"
		*/
		String methodParams = "";
		if(IsEntryMode)
		{
			methodParams = methodName;
			if(Params != null)
			{
				List<String> ls = new ArrayList<String>();
				ParamList pl = new ParamList(Params);
				//for (Param pm : Params) ls.add(pm.Name + ":" + pm.Value);
				methodParams += ": " + pl;
			}
			if(AllowZB64) methodParams = ZipBase64.EncodeZB64(methodParams);
			methodParams = "<ParamsText>" + methodParams + "</ParamsText>";
			methodName = "MainEntry";
		}
		else
		{
			if(Params != null)
				for (Param pm : Params)
					methodParams += "<" + pm.Name + ">" + pm.Value + "</" + pm.Name + ">";
		}

		String soapAction = "http://tempuri.org/" + methodName;
		
		String soapBody = "<" + methodName + " xmlns='http://tempuri.org'>" +
						  methodParams +
						  "</" + methodName + ">";

		String sRequest = "<?xml version='1.0' encoding='utf-8'?>" +
						  "<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>" +
						  "<soap:Body/> " + soapBody + " </soap:Envelope>";

		_D.Out("->> [" + mServerAddr + "] " + sRequest);
		try {
			URL url = new URL(ServerUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			byte[] req_data = sRequest.getBytes("utf-8");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(MAX_CONNECT_TIMEOUT);	
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
			con.setRequestProperty("SOAPAction", soapAction);
			con.setRequestProperty("Content-Length", "" + req_data.length);
			con.connect();   

			OutputStream osm = con.getOutputStream();
			osm.write(req_data);
			osm.flush();
			osm.close();
						
			InputStream ism = con.getInputStream();

			return GetResults(ism, methodName);
			
		} 
		catch(WSException e) {
			throw new MsgException(e.getMessage());
		}
		catch (Exception e) {
			if(e instanceof java.net.ConnectException )
			{
				SetConnected(false);
				//if(!silence) g.Hint("无法连接服务器！");
			}
			_D.Out(String.format("<<-GetWebService %s %s", methodName, e.toString()));
			throw e;
		}
	}

    public static int min(int a,int b){
        if(a>b){
            return a-b;
        }
        return b-a;
    }
	
	public static Object GetResults(InputStream in, String methodName) throws Exception {
		BytesBuffer out = new BytesBuffer();

		byte[] b = new byte[64 * 1024];

		ArrayList<String> Values = new ArrayList<String>();
		Values.clear();
		
		// 读取接收数据
		for (int n; (n = in.read(b)) >= 0;) 
			out.append(b, 0, n);
		byte[] data = out.toBytes();

		String xmlStr = new String(data, 0, data.length);

		// 解析XML数据
		_D.Out("RECV: " + xmlStr);
		Document doc = XmlUtils.GetXMLDoc(xmlStr);
		Element ele = XmlUtils.SelectDocElement(doc, "soap:Envelope/soap:Body/#Response/#Result".replace("#", methodName));
		String sData = ZipBase64.decodeZB64Code(ele.getTextContent());
		
		SetConnected(true);
		return ParseTextResult(sData);
	}

	public static Object ParseTextResult(String result) throws Exception {
		boolean HasError = gs.Sub(result, 0, ERR_TAG.length()).equals(ERR_TAG);
		if(HasError) // 解析Web服务返回的出错信息，产生一个异常
			WSException.Throw(result);
		int i = result.indexOf(":");
		if(i < 0) return null;
		String retTypeName = result.substring(0, i);
		String retValue = result.substring(i + 1);
		if(retTypeName.equals("Table")) return DataTable.Create(retValue);
		if(retTypeName.equals("Int32")) return gv.IntVal(retValue);
		if(retTypeName.equals("Single") || retTypeName.equals("Double")) return gv.FVal(retValue);
		if(retTypeName.equals("Boolean")) return gv.BoolVal(retValue);
		if(retTypeName.equals("ParamList")) return type.as(Hson.Parse(retValue), ParamList.class);
		return retValue;
	}
}

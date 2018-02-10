package hylib.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class WSException extends Exception {
    private static final long serialVersionUID = -338751699344441809L;
    public String ExceptionType;
    public String ErrorCode;
    
    public WSException(String msg, String type, String code)
    {
    	super(msg);
    	ErrorCode = code;
    	ExceptionType = type;
    }

	// 解析Web服务返回的出错信息，产生一个异常
	public static void Throw(String XmlError) throws Exception {
		Document doc = XmlUtils.GetXMLDoc(XmlError);
		Element xe = XmlUtils.SelectDocElement(doc, "Error");
		throw new WSException(
			XmlUtils.GetSubElementValue(xe, "ErrorMessage"),
			XmlUtils.GetSubElementValue(xe, "ExceptionType"),
			XmlUtils.GetSubElementValue(xe, "ErrorCode")
		);
	}
}

package com.hc.report;

import java.text.DecimalFormat;
import java.util.Date;

import hylib.toolkits.gv;
import hylib.util.TextAlign;

public class RptMini {
	public static PrtFieldList mPrtFields;
	static {
		mPrtFields = new PrtFieldList();
		mPrtFields.AddRange(new PrtField[] {
				new PrtField("FName", "产品名称", 0),
				new PrtField("FModel", "规格型号", 0),
				new PrtField("Unit", "单位", 3),
				new PrtField("FUnit", "单位", 3),
				new PrtField("Qty", "数量", 5, TextAlign.RIGHT),
				new PrtField("FQty", "数量", 5, TextAlign.RIGHT),
				new PrtField("Price", "单价", 5, TextAlign.RIGHT),
				new PrtField("FPrice", "单价", 5, TextAlign.RIGHT),
				new PrtField("Amount", "金额", 8, TextAlign.RIGHT),
		});
	}
	
	public static PrtField find(String name) {
		return mPrtFields.Find(name);
	}

    public static String StrVal(Object o)
    {
        if (o == null) return "";
        if (o instanceof String) return (String)o;
        if (o instanceof Date) return gv.SDateTime((Date)o);
        if (o instanceof byte[]) return gv.BytesToHex((byte[])o, 0, "");

        if (o instanceof Float) return new DecimalFormat("0.##").format(o);  
        if (o instanceof Double) return new DecimalFormat("0.##").format(o);  
        return o.toString();
    }
}

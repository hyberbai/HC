package com.hc;

import hylib.toolkits.ArrayTools;
import hylib.toolkits.ExProc;
import hylib.toolkits._D;
import hylib.toolkits.gv;
import hylib.util.Param;
import hylib.util.ParamList;

public class ptest {

	public static void testFirst() {
		try {
			T();
			//T1();
			//T2();
			//testArray();
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public static void testLoad() {
		try {
			 T1();
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

	public static void T() throws Exception {
//		ParamList pl = new ParamList("{ t, a:2, b, c:'aaa' }");
//		pl.RemoveEmpties();
		_D.Dumb();
	}

//	private static void testArray() {
//		try {
//			Param[] A = new ParamList("{ a:2, b:'aaa' }").toArray();
//			Param[] B = new ParamList("{ a:1, c:'aaa' }").toArray();
//			Param[] C = Param.convertParamArray(ArrayTools.Except(A, B, gv.hashFunc));
//			_D.Out(C);
//		} catch (Exception e) {
//			_D.Dumb();
//		}
//	}
	
	public static void T1() throws Exception {
//    	int fitemid = DBLocal.FindProductItemID("PTCA扩张导管（Maverick2）", "2.5*12");
//    	_D.Out(fitemid);
	}
	
//	public static void T1() {
//		Object[] a = { 1, 2, 3 };
//		a = ArrayTools.Insert(a, 0, "A");
//		
//		ptest t = new ptest();
//		try {
//			Object v = gu.ExecuteMethod(t, "Act" + "Test#1", 123);
//			_D.Out(v);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void T2() {
//		long t = FileUtil.getIdTime(12345001);
//
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
//		String a = formatter.format(t);
//		try {
//			String fn = "/sdcard/1.txt";
//			FileUtil.SaveToFile(fn, new byte[] { 49, 50, 51, 52 });
//
//			String s1 = formatter.format(new File(fn).lastModified());
//			FileUtil.SetFileLastModified(fn, t);
//			String s2 = formatter.format(new File(fn).lastModified());
//			int i = FileUtil.getFileTimeID(fn);
//			_D.Out(i);
//		} catch (Exception e) {
//			ExProc.Show(e);
//		}
//	}
	
	public static void T3() {
//		String pattText = gs.TextEscape("[23]*.pdf", ".+$^[](){}|\\/");
//
//        pattText = pattText.replace("*", "([\\s\\S]*)").replace("?", "([\\s\\S]+)");
//        Pattern patt = pattText.isEmpty() ? null : Pattern.compile(pattText);
//        boolean ok = patt.matcher("[123]sdfsa.pdf").find();
//        
//        try {
//    		String[] files = FileUtil.searchFiles("/sdcard", "*解*.pdf", new ParamList("includeSub", false));
//    		_D.Out(files);
//		} catch (Exception e) {
//			ExProc.Show(e);
//		}
	}
	

	public static void L1() {
		//SysOpen.GetInstalledPackages(MyApp.CurrentActivity(), "/sdcard/[189].鞍山市双山医院-一次性使用无菌高值医用耗材记录.pdf");
		//SysOpen.OpenFile(MyApp.CurrentActivity(), "/sdcard/[189].鞍山市双山医院-一次性使用无菌高值医用耗材记录.pdf");
	}
	public String ActTest(String key, int v) {
		return key + " : " + v;
	}
}

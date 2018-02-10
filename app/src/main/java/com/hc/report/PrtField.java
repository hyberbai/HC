package com.hc.report;

import hylib.data.SelectColInfo;
import hylib.data.SqlUtils;
import hylib.util.TextAlign;

public class PrtField {
	public String Name;
	public String Disp;
	public int Len;
	public int Align;
	public SelectColInfo ColInfo;

	public PrtField(String name, String disp, int len, int align) {
		Name = name;
		Disp = disp;
		Len = len;
		Align = align;
	}

	public PrtField(String name, String disp, int len) {
		this(name, disp, len, TextAlign.LEFT);
	}

	@Override
	public String toString() {
		return Name + " " + Disp;
	}
}

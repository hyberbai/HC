package com.dev.prt;

import hylib.toolkits.gs;

public class DeviceInfo {
	public String Name;
	public String Addr;
	public int Type;
	
	public DeviceInfo(String name, String addr, int type) {
		Name = name;
		Addr = addr;
		Type = type;
	}

	public String getFullName() {
		return gs.Connect(Name, Addr, "@");
	}
}

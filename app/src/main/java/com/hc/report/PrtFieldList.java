package com.hc.report;

import hylib.toolkits.gs;

import java.util.ArrayList;
import java.util.List;

public class PrtFieldList extends ArrayList<PrtField> {
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		List<String> list = new ArrayList<String>();
		for (PrtField item : this)
			list.add(item.toString());
		return gs.JoinList(list, ", ");
	}
	
	public void AddRange(PrtField... items) {
		for (PrtField item : items)
			add(item);
	}
	
	public PrtField Find(String name) {
		for (PrtField item : this)
			if(item.Name.equalsIgnoreCase(name)) return item;
		return null;
	}
}

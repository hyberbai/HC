package hylib.data;

import hylib.toolkits.gs;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;

public class SelectColList extends ArrayList<SelectColInfo> {
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		List<String> list = new ArrayList<String>();
		for (SelectColInfo sc : this)
			list.add(sc.toString());
		return gs.JoinList(list, ", ");
	}

	public String getAliasText() {
		List<String> list = new ArrayList<String>();
		for (SelectColInfo sc : this)
			list.add(sc.Alias);
		return gs.JoinList(list, ", ");
	}
	
	/**
	 * 取非分组计算列
	 */
	public SelectColList getNormalCols() {
		SelectColList list = new SelectColList();
		for (SelectColInfo sc : this)
			if(sc.isNormal()) list.add(sc);
		return list;
	}

	/**
	 * 取分组计算列
	 */
	public SelectColList getComputeCols() {
		SelectColList list = new SelectColList();
		for (SelectColInfo sc : this)
			if(!sc.isNormal() && !sc.isItems()) list.add(sc);
		return list;
	}

	/**
	 * 是否包含分组明细
	 */
	public boolean containItems() {
		for (SelectColInfo sc : this)
			if(!sc.isItems()) return true;
		return false;
	}
	
	public String getNormalColsText() {
		return getNormalCols().getAliasText();
	}

	/**
	 * 根据选择列的配置生成一个数据表
	 */
	public DataTable createTable() {
		DataTable dt = new DataTable();
		for (SelectColInfo sc : this)
			dt.addColumn(sc.Alias, sc.DataType);
		return dt;
	}
}

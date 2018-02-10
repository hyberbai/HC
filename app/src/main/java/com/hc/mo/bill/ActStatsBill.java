package com.hc.mo.bill;

import com.hc.R;
import com.hc.SysData;
import com.hc.dal.Bill;
import com.hc.db.DBLocal;

import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataTable;
import hylib.data.SelectColList;
import hylib.data.SqlUtils;
import hylib.db.SqlHelper;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.HyColor;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits.gs;
import hylib.toolkits.type;
import hylib.ui.dialog.UCCreator;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.widget.HyEvent;
import hylib.widget.HyListAdapter;
import hylib.widget.HyListView;
import hylib.widget.HyEvent.LvItemClickEventParams;
import hylib.widget.HyEvent.LvItemLongClickEventParams;
import android.R.string;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class ActStatsBill extends ActStats {

    public static int STAT_USE = 1;
    public static int STAT_TS = 2;
    public static String[] statNames = new String[] { "使用统计", "台数统计" };
    
	@Override
    public void Stats() {
		if(CID == STAT_USE) StatsUsed();
		setTitle(statNames[CID - 1]);
		super.Stats();
    }

    @Override
    protected Object getListItemViewConfig() {
        // 列表项视图配置
        return "items: [" + 
	    	"[tv: { id:2031, fs: 14.2dp, text: info,w:5w,bg1:r,color:text, bgc1:r, padding: 3dp,h:wrap }, "+ 
        	"tv: { id:2032, fs: 14.2dp, text: tot,w:100dp,bg1:g, padding: 3dp, h:wrap, bgc:#33CCAAFF, grv:r }] "+ 
        "], padding: '5dp, 0dp, 5dp, 0dp', margin: 0dp, w:match, h:wrap, bgc1:#33CCAAFF";
    }
    
    @Override
    protected void setListItemViewData(ParamList arg) {
		View convertView = arg.Get("view", View.class);
		DataRow dr = arg.Get("item", DataRow.class);

		TextView tvInfo = type.as(convertView.findViewById(2031), TextView.class);
		TextView tvTot = type.as(convertView.findViewById(2032), TextView.class);
		
		String info = gs.JoinArray(new Object[] {
    			dr.getStrVal("FName"),
    			dr.getStrVal("FModel")
		}, "\n");
		
		boolean readed = dr.getIntVal("Flag") == Bill.FLAG_READED;
		
		if(info.isEmpty()){
    		tvInfo.setText("无描述信息");
    		tvInfo.setTextColor(Color.GRAY);
		} else {
			tvInfo.setText(info);
    		tvInfo.setTextColor(readed ? Color.GRAY : HyColor.Text);
		}
		
		tvInfo.setLineSpacing(0, 1.2f);

		SpannableStringHelper sh = new SpannableStringHelper();
		gs.getFmtQty(sh, dr.getIntVal("Qty"));
		sh.appendObj("\n");
		gs.getFmtMoney(sh, dr.getFVal("Amount"));
		tvTot.setLineSpacing(0, 1.2f);
		tvTot.setText(sh);
    }
    
    public void StatsUsed() {
    	Object[] IDs = Params.$("IDs");
    	if(IDs != null) {
    		String sql = 
				"select d.IID, i.SName FName, i.FModel, d.Qty, d.Price, d.Ext from Bill m\n" +
				"join BillDetail d on m.BID=d.BID\n" +
				"left join Item i on i.FItemID=d.IID\n" +
				"where m.BID " + SqlHelper.SqlIn(IDs);
    		dtStats = DBLocal.OpenTable("IID/i|Qty/i|Price/m", sql);
    		for (DataRow dr : dtStats.getRows()) {
				if(dr.getIntVal("IID") == 0) {
					ParamList pl = new ParamList(dr.getStrVal("Ext"));
					dr.setValue("FName", pl.SValue("FName"));
					dr.setValue("FModel", pl.SValue("FModel"));
				}
			}

    		SelectColList selects = SqlUtils.ParserSqlSelect("FName, FModel, Sum(Qty), Sum(Price) Amount");
    		dtStats = dtStats.GroupBy(selects);

			mListAdapter.setListItems(dtStats.getRows());
			mListAdapter.notifyDataSetChanged();
    	}
    }
}

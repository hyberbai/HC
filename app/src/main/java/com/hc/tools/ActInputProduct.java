package com.hc.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.hc.ID;
import com.hc.R;
import com.hc.pu;
import com.hc.R.id;
import com.hc.R.layout;
import com.hc.dal.Bill;
import com.hc.db.DBHelper;
import com.hc.db.DBLocal;

import hylib.data.DataRow;
import hylib.edit.EditField;
import hylib.edit.ValidType;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.gc;
import hylib.toolkits.gu;
import hylib.toolkits.gv;
import hylib.ui.dialog.UCParamList;
import hylib.ui.dialog.UIUtils;
import hylib.util.ParamList;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract.Root;
import android.util.SparseArray;
import android.view.View;

// 输入产品信息
public class ActInputProduct extends ActInput {
	private int SID;
	private int FItemID;
	private int FCustID;
	public final static int TYPE_SALE = 1; 
	
	public final static int GID_ITEM = 1; 
	public final static int GID_SN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FCustID = Params.IntValue("CustID");
    }
    
    @Override
    protected void LoadEditFields() {
        try {
            mEFields.addRange(
        		new EditField(ID.SNo, "FSerialNo", "流水号", ""),

    			//new EditField(ID.FNumber, "FNumber", "产品编号", "need, uniq"),
    		//	new EditField(ID.FName, "FName", "产品名称", "need"),
    			new EditField(ID.SName, "SName", "产品名称", "need"),
    			new EditField(ID.FModel, "FModel", "规格型号", "need"),
    			new EditField(ID.Price, "Price", "单 价", "need, vt: money"),
    			new EditField(ID.Unit, "FUnit", "单 位", "need"),
    			
    			new EditField(ID.FBatchNo, "FBatchNo", "产品批号", "need"),

    			new EditField(ID.Cls, "Cls", "产品类别", ""),
    			
    			new EditField(ID.MfgDate, "FKFDate", "生产日期", "vt: date"),
    			new EditField(ID.ExpDate, "FPeriodDate", "有效期至", "vt: date"),
    			new EditField(ID.ExpDays, "FKFPeriod", "保质期"),
    			
    			new EditField(ID.Manuf, "Manuf", "生产厂家"),
    			new EditField(ID.Reg, "Reg", "注册证号"),
    			new EditField(ID.SP, "SP", "供货单位"),
    			
    			new EditField(ID.FNote, "FNote", "备  注")
    		);
            mEFields.setGroup(GID_ITEM, ID.SName, ID.FNumber, ID.FModel, ID.Cls, ID.Manuf, ID.Reg, ID.Unit);
            mEFields.setGroup(GID_SN, ID.SNo, ID.FBatchNo, ID.MfgDate, ID.ExpDays, ID.ExpDate);
		} catch (Exception e) {
			ExProc.Show(e);
		}
    }

    @Override
    protected void CreateInputPanel() {
		setContentView(R.layout.activity_empty);
        tvTitle = $(R.id.tv_title);
        $(R.id.ib_options).setVisibility(View.INVISIBLE);
        
		// 手工录入产品信息
        String config = "items: [" + 
        		(mInputType == TYPE_SALE ? "et: { id:" + ID.SNo + ", vt: nt }, " : "" ) +
        	//	"et: { id:" + ID.FNumber + ", text: '产品编号', " + Search.getProductSrchCfg() + " }, " + 
        		"et: { id:" + ID.SName + "," + Search.getProductSrchCfg() + " }, " + 
        		"et: { id:" + ID.FModel + ", " + Search.getProductSrchCfg() + " }, " + 
        		"[et: { id:" + ID.Price + ", w: 3w }, " +
        		 "et: { id:" + ID.Unit + ", lw: match, w: 2w } ], " + 
        		"et: { id:" + ID.FBatchNo + ", vt: nt }, " + 

        		//"et: { id:" + ID.Cls + ", " + Search.getProductSrchCfg() + " }, " + 
        		"et: { id:" + ID.Cls + ", auto-comp: { dsn: ItemCls, dm: IName } }, " + 
        		"et: { id:" + ID.MfgDate + " }, " + 
        		"et: { id:" + ID.ExpDate + " }, " + 
        		"et: { id:" + ID.Manuf + ", auto-comp: { dsn: Manuf, dm: FName }  }, " + 
        		"et: { id:" + ID.Reg + ",  }, " +
        		
        		(mInputType == TYPE_SALE ? "et: { id:" + ID.SP + ",  }, " : "" ) +
        		
        		"et: { id:" + ID.FNote + ",  }, " +
        "], width: 60dp, margin: 10dp";

        //items[*.id]==
        
        try {
        	UCParamList pl = new UCParamList(config);
            pl.SetValue("EFields", mEFields);
         //   findPanelItemPM(pl, ID.FNumber).set("onSearch", searchListener);
            pl.findViewParams(ID.SName).set("searchListener", searchListener);
            pl.findViewParams(ID.FModel).set("searchListener", searchListener);
            pl.findViewParams(ID.Cls).set("searchListener", searchListener);
            // 创建输入面板
            CreateInputView(pl);

		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

    @Override
    protected void AdjustLoadParamValues(ParamList pl) throws Exception {
    	Bill.CoverExtParams(pl);
    	pl.RenKeys("SNo FSerialNo, BatchNo FBatchNo, FName SName");
	}
    
    public void Clear() {
    	//UIUtils.clearViews(this, );
    	mEFields.clearValues(ID.SName, ID.FModel, ID.FBatchNo, ID.Price, ID.Cls, ID.MfgDate, ID.ExpDate, ID.Manuf, ID.Reg, ID.Unit);
		SID = 0;
		FItemID = 0;
	}
    
    @Override
	protected void onSearch(int tid, DataRow dr) throws Exception {
    	if(tid == Search.PRODCUT)
    	{
			FItemID = dr.getIntVal("FItemID");
			DataRow drItem = DBLocal.OpenDataRow(DBHelper.Cfg_Item, "select * from Item where FItemID=?", FItemID);
			SetItemCtrls(drItem);
    	}
    	else if (tid == Search.CLS) {
    		mEFields.setValue(ID.Cls, dr.getValue("IName"));
    	}
	}
    
    public void SetCtrlValues(int gid, DataRow dr) {
		for (EditField ef : mEFields) 
			if(ef.GroupId == gid) 
				ef.setValue(dr.getValue(ef.Name));
	}
    
    public void SetItemCtrls(DataRow drItem) {
		mLockCount++;
		FItemID = drItem.getIntVal("FItemID");
		SetCtrlValues(GID_ITEM, drItem);
		Float price = Bill.GetPlanPrice(FCustID, FItemID);
		mEFields.setValue(ID.Price, price);
		mLockCount--;
	}
    		
    @Override
    public void onEditEnd(int id) throws Exception {
    	String val = $T(id);
    	if(id == ID.SNo) {
    		val = pu.getSNo(val);
    		DataRow dr = Bill.GetSNInfo(val);
    		if(dr != null){
    			mLockCount++;
    			SID = dr.getIntVal("FID");
    			SetCtrlValues(GID_SN, dr);
    			SetItemCtrls(dr);
    			mLockCount--;
    		}
    		else if(SID > 0)
    			Clear();
    		mEFields.setValue(ID.SNo, val);
    	} else {
    		if(gv.In(id, ID.SName, ID.FNumber, ID.FModel)) FItemID = 0;
    		EditField ef = mEFields.get(id);
    		ef.updateValue(val);
    	}
	}
    
    private EventHandleListener searchListener = new EventHandleListener() {
		
		@Override
		public void Handle(Object sender, ParamList arg) throws Exception {
			int id = ((View)sender).getId();

			int tid =
					gv.In(id, ID.FNumber, ID.SName, ID.FModel) ? Search.PRODCUT:
					id == ID.Cls ? Search.CLS :
					id == ID.Manuf ? Search.MANUF :
					id == ID.SP ? Search.SP :
					//id == ID.st ? Search.STOCK :
					0;

			ParamList pl = new ParamList("hint", "搜索" + Search.nameOf(tid));
			Search.Execute(context, tid, $T(id), pl);
		}
		
	};

    @Override
    protected void setResultValues(ParamList plValues) {
    	mEFields.writeToParamList(plValues);
    	
    	plValues.SetValue("IID", FItemID);
		plValues.SetValue("Qty", 1);	 		// 数量
		plValues.RenKeys("FBatchNo BatchNo, FSerialNo SNo, FItemID IID, SName FName");
	}
    
}

package com.hc;

import hylib.data.DataTable;
import hylib.toolkits.ExProc;
import hylib.toolkits.TempV;
import hylib.toolkits.gc;
import hylib.toolkits.gi;
import hylib.toolkits.gi.CallBack;
import hylib.ui.dialog.LoadingDialog;
import hylib.util.ActionInfo;
import hylib.util.ParamList;
import hylib.view.ActivityEx;
import hylib.widget.VGWrap;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import com.hc.dal.WS;
import com.hc.db.DBLocal;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ActMore extends ActivityEx {
	private VGWrap vwItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState); 
        //TableView table = new TableView(this, 8, 8);        setContentView(table);
        
		setContentView(R.layout.activity_more);
		
		vwItems = (VGWrap) this.findViewById(R.id.vw_items);
		
		FillList();
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MyApp.Exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 更新仓库信息
    List<String[]> list;
	HashMap<String, Integer> FI;
	String[] items;
	
	void PutMapValue(HashMap<String, String> map, String key) {
		map.put(key, items[FI.get(key)]);
	}
	
	public ActionInfo[] ACL = new ActionInfo[] {
			new ActionInfo("SynData", "数据同步", R.drawable.m_syn), 
			new ActionInfo("Setting", "系统设置", R.drawable.m_setting), 
	};
	
	// 同步数据，同步内容：流水号数据、产品信息、用户信息、仓库、价格方案
	protected void ActSynData() {
		MainActions.SynAllData(true);
	}
	
	protected void ActSetting() {
		ActSettings.Load(this);
	}
	
    private void FillList() {
		for (ActionInfo act : ACL) {
	        LinearLayout lay = (LinearLayout)View.inflate(this, R.layout.list_image_item, null);
			
			// 图标
			ImageView imgView = (ImageView)lay.findViewById(R.id.image);
			imgView.setImageDrawable(getResources().getDrawable(act.ResID));
			
			// 文字
			TextView tvTitle = (TextView)lay.findViewById(R.id.title);
			tvTitle.setText(act.Title);

	        lay.setTag(act);
	        
	        final Context context = this;
	        
		    lay.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Class<?> cls = context.getClass();
					ActionInfo act = (ActionInfo)v.getTag();
					try {
						Method md = cls.getDeclaredMethod("Act" + act.Name);
						md.invoke(context);
					} catch (Exception e) {
						ExProc.ShowMsgbox(context, e);
					}
				}
			});
			vwItems.addView(lay);

		}
    }
    
}

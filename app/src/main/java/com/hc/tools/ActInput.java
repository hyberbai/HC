package com.hc.tools;

import java.util.HashMap;
import java.util.Map;

import com.hc.ActBase;
import com.hc.R;
import com.hc.R.id;

import hylib.data.DataRow;
import hylib.edit.EditField;
import hylib.edit.EditFieldList;
import hylib.toolkits.DelayTask;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.ui.dialog.Msgbox;
import hylib.ui.dialog.UICreator;
import hylib.util.Param;
import hylib.util.ParamList;
import android.R.bool;
import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActInput extends ActBase {
	private View mEditCtrl;
	protected int mID;
	public TextView tvTitle;
	public EditFieldList mEFields;
	public int mInputType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	mInputType = Params.IntValue("InputType");
        mEFields = new EditFieldList();
        try {
        	mID = Params.IntValue("ID");
        	
        	// 加载编辑字段
        	LoadEditFields();
        	
        	// 创建输入面板
            CreateInputPanel();
            
            // 绑定编辑控件
            mEFields.BindingViews(this);

            $Set(R.id.tv_title, Params.getValue("title", "产品录入"));

            DisableViews(Params.SValue("disables"));
            
            // 加载初值
            LoadValues(Params.getArray("values"));
		} catch (Exception e) {
			ExProc.Show(e);
		}
    }

    
    /*
     * 创建输入面板
     */
    protected void LoadEditFields() {
	}
    
    /*
     * 创建输入面板
     */
    protected void CreateInputPanel() {
	}

    /*
     * 加载初始输入值
     */
    protected void LoadValues(Object[] values) throws Exception {
    	if(values == null) return;
    	LockChange = true;
    	ParamList pl = new ParamList(values);
    	AdjustLoadParamValues(pl);
    	mEFields.readParamList(pl);
    	LockChange = false;
	}

    protected void DisableViews(String items) throws Exception {
    	for (Object item : gs.Split(items)) {
    		String name = (String)item;
    		EditField ef = mEFields.findItem(name);
    		if(ef == null) continue;
			ef.view.setEnabled(false);
    	}
	}
    
    protected void AdjustLoadParamValues(ParamList pl) throws Exception {
	}

    @Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.ib_ok) InputDone();
		if(id == R.id.ib_back && SaveChangesCancel()) return;
		super.onClick(v);
	}
    
    private boolean SaveChangesCancel() {
		if(!IsChanged) return false;
		if(Msgbox.Ask(this, "数据已修改，是否保存？") == false) return false;
		return !InputDone();
	}
    
    protected void setResultIntent(Intent intent) {
    	intent.putExtra("ID", mID);
    	intent.putExtra("InputType", mInputType);
    	ParamList plValues = new ParamList();
    	setResultValues(plValues);
    	intent.putExtra("values", plValues.toString());
	}

    protected void setResultValues(ParamList plValues) {
	}

	protected void ValidateItem(EditField ef) throws Exception {
		ef.Validate();
	}

    protected void Validate() throws Exception {
    	for (EditField ef : mEFields) ValidateItem(ef);
	}

	protected boolean InputDone() {
		EndEdit();
		try {
			Validate();
			Intent intent = new Intent();
			setResultIntent(intent);
	        setResult(RESULT_OK, intent);
			finish();
			return true;
		} catch (Exception e) {
			ExProc.Show(e);
			return false;
		}
	}
	
	protected void EndEdit() {
		if(mEditCtrl == null) return;
		try {
			onEditEnd(mEditCtrl.getId());
			mEditCtrl = null;
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK) this.finish();
        return super.onKeyDown(keyCode, event);
    }

    public void onEditEnd(int id) throws Exception {
	}

	// 手工录入产品信息
	public void CreateInputView(ParamList pl) {
        try {
            pl.set("ValueChanged", new EventHandleListener() {
				
				@Override
				public void Handle(Object sender, ParamList arg) throws Exception {
					if(isLockChange()) return;
					mEditCtrl = type.as(sender, View.class);
					mEFields.setItemChange(mEditCtrl.getId(), true);
					setChanged(true);
				}
			});

            pl.set("onFocusChange", new EventHandleListener() {
				
				@Override
				public void Handle(Object sender, ParamList arg) throws Exception {
					int id = type.as(sender, View.class).getId();
					boolean hasFocus = arg.BValue("hasFocus");
					if(!hasFocus && mEFields.getItemChange(id))
					{
						mEFields.setValue(id, $V(id));
						$Set(id, mEFields.getValue(id));
						//$Set(id,mEFields.get(id).val);
						EndEdit();
					}
				}
			});
            
            ViewGroup vg = UICreator.CreateScrollPanel(this, pl);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)vg.getLayoutParams();
            if(lp == null) lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.weight = 1.0f;
            lp.gravity = Gravity.CENTER;
            vg.setLayoutParams(lp);
            
           this.getRootLayout().addView(vg, 1);
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	protected void onSearch(int tid, DataRow dr) throws Exception {
	}
	
	public ParamList getChangeItems(){
    	ParamList pl = new ParamList();
		for (EditField ef : mEFields)
			pl.SetValue(ef.Name, ef.Value);
		return pl;
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	try {
            if(requestCode == ActBase.REQ_CODE_SEARCH) {
                if(resultCode == RESULT_OK) {
                	int tid = data.getIntExtra("tid", 0);
                	onSearch(tid, ActSearch.drSelected);
                }
            }
		} catch (Exception e) {
			ExProc.Show(e);
		}
        super.onActivityResult(requestCode, resultCode, data);
    } 
    
}

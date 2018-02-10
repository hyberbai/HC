package com.hc.mo.bill;


import com.hc.MyApp;
import com.hc.R;
import com.hc.R.id;
import com.hc.R.layout;
import com.hc.R.style;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import hylib.data.DataRow;
import hylib.toolkits.ExProc;
import hylib.toolkits._D;
import hylib.toolkits.gc;
import hylib.toolkits.gcon;
import hylib.toolkits.gi;
import hylib.toolkits.gv;
import hylib.ui.dialog.HyDialog;

public class DlgSale extends HyDialog {
	public DataRow drItem;
	private TextView tvInfo;
	private EditText etNote;
	private EditText etPrice;

	public DlgSale(Context context) {
        super(context, R.style.hy_dialog);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dlg_sale);
        tvInfo = (TextView)findViewById(R.id.tv_info);
        etNote = (EditText)findViewById(R.id.et_note);
        etPrice = (EditText)findViewById(R.id.et_price);

        BindingOnClick(gc.GetViewGroup(this));
        Notify(gi.NF_INIT);
    }
    
    @Override
	public void Click(View v) {
		int id = v.getId(); 
		try {
			if(id == R.id.btn_ok) {
		        Notify(gi.NF_OK);
		        dismiss();
			}
			else if (id == R.id.btn_exit)
				dismiss();
		} catch (Exception e) {
			ExProc.Show(MyApp.CurrentActivity(), e);
		}
	}
    
	public void SetInfo(String info) {
		tvInfo.setText(info);
	}

	public String GetNote() {
		return etNote.getText().toString();
	}
	
	public void SetNote(String note) {
		etNote.setText(note);
	}

	public float GetPrice() {
		return gv.FVal(etPrice.getText().toString());
	}
	
	public void SetPrice(Object value) {
		etPrice.setText(gv.StrVal(value));
	}
}

package com.hc.mo.pd;


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

public class DlgPdNote extends HyDialog {
	public DataRow drItem;
	private TextView tvInfo;
	private EditText etNote;

	public DlgPdNote(Context context) {
        super(context, R.style.hy_dialog);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dlg_pd_note);
        tvInfo = (TextView)findViewById(R.id.tv_info);
        etNote = (EditText)findViewById(R.id.et_note);
        etNote.addTextChangedListener(new TextWatcher() {
        	
    		   public void afterTextChanged(Editable s) {
    		   }

    		   public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    		   }

    		   public void onTextChanged(CharSequence s, int start, int before, int count) {
    			   if(GetState() == 0) SetState(gcon.S_WRONG);
    		   }
        });
        
        BindingOnClick(gc.GetViewGroup(this));
        Notify(gi.NF_INIT);
        int state = gv.IntVal(drItem.getValue("State"));

		((RadioButton)findViewById(R.id.rb_ok)).setEnabled(state != gcon.S_EXTRA);
		((RadioButton)findViewById(R.id.rb_wrong)).setEnabled(state != gcon.S_EXTRA);
		((RadioButton)findViewById(R.id.rb_extra)).setEnabled(state == gcon.S_EXTRA);
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
	
	public int GetState() {
		int rid = ((RadioGroup)findViewById(R.id.rg_status)).getCheckedRadioButtonId();
		return rid == R.id.rb_ok ? gcon.S_OK :
			   rid == R.id.rb_wrong ? gcon.S_WRONG :
			   rid == R.id.rb_extra ? gcon.S_EXTRA :
			   0;
	}
	
	public String GetNote() {
		return etNote.getText().toString();
	}
	
	public void SetNote(String note) {
		etNote.setText(note);
	}
	
    public void SetState(int state) {
    	((RadioGroup)findViewById(R.id.rg_status)).clearCheck();
		int rid = state == gcon.S_OK ? R.id.rb_ok : 
				  state == gcon.S_WRONG ? R.id.rb_wrong : 
				  state == gcon.S_EXTRA ? R.id.rb_extra : 
			     0;
		if(rid == 0) return;
		((RadioButton)findViewById(rid)).setChecked(true);
	}

}

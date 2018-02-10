package com.hc.mo.bill;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.dev.HyScanner;
import com.hc.ActBase;
import com.hc.ID;
import com.hc.MyApp;
import com.hc.R;
import com.hc.SysData;
import com.hc.g;
import com.hc.pu;
import com.hc.MyApp.WorkState;
import com.hc.R.id;
import com.hc.dal.Bill;
import com.hc.dal.SCID;
import com.hc.dal.Setting;
import com.hc.dal.WS;
import com.hc.dal.d;
import com.hc.db.DBHelper;
import com.hc.db.DBLocal;
import com.hc.mo.sy.ActSy;
import com.hc.tools.ActInputProduct;
import com.hc.tools.ActSearchItem;
import com.hc.tools.Search;

import hylib.data.DataColumn;
import hylib.data.DataColumnCollection;
import hylib.data.DataRow;
import hylib.data.DataRowCollection;
import hylib.data.DataRowState;
import hylib.data.DataSort;
import hylib.data.DataTable;
import hylib.db.SqlDataAdapter;
import hylib.edit.EditField;
import hylib.edit.EditFieldList;
import hylib.io.FileUtil;
import hylib.toolkits.DelayTask;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.SpannableStringHelper;
import hylib.toolkits.Speech;
import hylib.toolkits.gc;
import hylib.toolkits.gi;
import hylib.toolkits.gs;
import hylib.toolkits.gv;
import hylib.toolkits.type;
import hylib.toolkits.gi.CallBack;
import hylib.ui.dialog.HyDialog;
import hylib.ui.dialog.Msgbox;
import hylib.ui.dialog.PopupWindowEx;
import hylib.ui.dialog.UCCreator;
import hylib.ui.dialog.UICreator;
import hylib.ui.dialog.UIUtils;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.widget.HyListAdapter;
import hylib.widget.HyListView;
import hylib.widget.HyEvent.LvItemClickEventParams;
import android.R.bool;
import android.R.integer;
import android.R.plurals;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ActTest extends ActBase {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_t);
    }


}

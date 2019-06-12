package hylib.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hc.ID;
import com.hc.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import hylib.data.DataRowCollection;
import hylib.data.DataTable;
import hylib.sys.HyApp;
import hylib.sys.LoopMsg;
import hylib.toolkits.DelayTask;
import hylib.toolkits.EventHandleListener;
import hylib.toolkits.ExProc;
import hylib.toolkits.TempV;
import hylib.toolkits.gs;
import hylib.toolkits.gu;
import hylib.toolkits.type;
import hylib.util.ActionInfo;
import hylib.util.ActionList;
import hylib.util.Param;
import hylib.util.ParamList;
import hylib.widget.HyEvent.LvItemClickEventParams;
import hylib.widget.HyListAdapter;
import hylib.widget.HyListView;

public class Msgbox {
	// Msgbox Button
	public static final int MB_Ok		= 1 << 0;
	public static final int MB_Def1		= 1 << 1;
	public static final int MB_Def2		= 1 << 2;	
	public static final int MB_Def3		= 1 << 3;	
	
	// Msgbox Icon
	public static final int MI_Info		= 1 << 11;
	public static final int MI_Ask		= 1 << 12;
	public static final int MI_Alert	= 1 << 13;
	public static final int MI_Error	= 1 << 14;
	
	public static final int MB_Input	= 1 << 20;

	// Msgbox Result
	public static final int MR_1		= 1;
	public static final int MR_2		= 2;
	public static final int MR_3		= 3;
	public static final int MR_OK		= MR_1;
	public static final int MR_Yes		= MR_1;
	public static final int MR_No		= MR_2;
	public static final int MR_Cancel	= MR_2;
	
	private static OnInputVerifyListener InputVerifyListener;

    public interface OnInputVerifyListener {
        boolean onInputVerify(String input);
    }

	public static int Show(Context context, String title, String msg, int Flag, HashMap<String, Object> map) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if(title.equals("")) title = (Flag & MI_Error) != 0 ? "出错" :
									 (Flag & MI_Alert) != 0 ? "警告" :
									 (Flag & MB_Input) != 0 ? "" :
									 "提示";

		final TempV ret = new TempV();
		int iconId = (Flag & MI_Error) != 0 ? android.R.drawable.ic_dialog_alert :
					 (Flag & MI_Alert) != 0 ? android.R.drawable.ic_dialog_alert :
					 (Flag & MI_Ask) != 0 ? android.R.drawable.ic_dialog_info :
				     0;//android.R.drawable.ic_dialog_info;
		
		String sb1 = (Flag & MI_Ask) != 0 ? "是" :
			// (Flag & MB_Alert) != 0 ? "" :
		     "确定";
		
		String sb2 = (Flag & MI_Ask) != 0 ? "否" :
			 (Flag & MB_Input) != 0 ? "取消" :
		     "";
		
		final EditText etInput = (Flag & MB_Input) != 0 ? new EditText(context) : null;

		final LoopMsg lm = new LoopMsg();

		builder.setTitle(title).setMessage(msg).setIcon(iconId);
	  	if(etInput != null) {
	  		builder.setView(etInput);
	  		if(map != null) {
	  			String input_type = map.get("input_type").toString();
	  			if(input_type.equals("pwd")) 
	  				etInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	  			Object o = map.get("input");
	  			if(o != null) etInput.setText(o.toString());
	  			InputVerifyListener = (OnInputVerifyListener)(map.get("input_vertify"));
	  		}
	  	}
	  	
		builder.setPositiveButton(sb1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					  	if(etInput != null) {
					  		ret.S = etInput.getText().toString();
					  		if(InputVerifyListener != null) 
					  			if(!InputVerifyListener.onInputVerify(ret.S)) {
					  				AllowDialogClose(dialog, false);
					  				return;
					  			}
					  	}

		  				AllowDialogClose(dialog, true);
						lm.StopLoop(MR_1);
						dialog.dismiss();
					}
				});
		if(!sb2.equals(""))
			builder.setNegativeButton(sb2, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
	  				AllowDialogClose(dialog, true);
					lm.StopLoop(MR_2);
					dialog.dismiss();
				}
			});

		builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				lm.StopLoop();
			}
		});

		builder.show();
		lm.Loop();

		int mr = lm.Result();
		if(etInput != null && mr == MR_OK) map.put("input", ret.S);
		return mr;
	}

	private static void AllowDialogClose(DialogInterface dialog, boolean Allow) {
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, Allow);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void AllowCloseDlg(DialogInterface dlg, boolean allowClose) {
		try {
			Field field = dlg.getClass().getSuperclass().getDeclaredField("mShowing");
	        field.setAccessible(true);
	        field.set(dlg,allowClose);
		} catch (Exception e) {
            e.printStackTrace();
		}
	}

	public static Object Input(final Context context, String msg, final ParamList pl) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);

		final TempV ret = new TempV();
		final TempV tdlg = new TempV();

		final EditText etInput = new EditText(context);

		final LoopMsg lm = new LoopMsg();
		builder.setMessage(msg);//.setIcon(iconId);
  		builder.setView(etInput);
  		if(pl != null) {
  			if(pl.containsKey("title")) builder.setTitle(pl.SValue("title"));
  			String input_type = pl.SValue("input_type");
  			etInput.setInputType(UIUtils.valueOfInputTypeName(input_type));
  			etInput.setSelectAllOnFocus(true);
  			
  			Object o = pl.get("value");
  			if(o != null) etInput.setText(o.toString());
  			
  			UIUtils.BindingValueChangeEvent(new EventHandleListener() {
				
				@Override
				public void Handle(Object sender, ParamList arg) throws Exception {
					pl.Call("onChanged", sender, arg);
					if(arg.BValue("dismiss")) ((Dialog)tdlg.get()).dismiss();
				}
			}, etInput);
  		}

		builder.setPositiveButton("确定", null);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
  				AllowDialogClose(dialog, true);
				lm.StopLoop(MR_Cancel);
				dialog.dismiss();
			}
		});

		builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				lm.StopLoop();
			}
		});
		
		final AlertDialog dialog = builder.create();
		tdlg.set(dialog);
		dialog.show();
		
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					String val = etInput.getText().toString();
			  		if(val.isEmpty()) ExProc.ThrowMsgEx("输入不能为空！");
			  		ParamList plInput = new ParamList();
			  		plInput.SetValue("value", val, false);
			  		pl.Call("input", context, plInput);
			  		ret.O = plInput.get("value");
	  				AllowDialogClose(dialog, true);
					lm.StopLoop(MR_OK);
					dialog.dismiss();
				} catch (Exception e) {
					ExProc.Show(e);
					AllowCloseDlg(dialog, false);
				}
			}
		});
		
		DelayTask tsk = new DelayTask();
		tsk.TaskHandleListener = new EventHandleListener() {
			
			@Override
			public void Handle(Object sender, ParamList arg) throws Exception {
				UIUtils.toggleSoftInput(dialog.getContext());
			}
		};
		tsk.Start(100);
        
		if(!pl.BValue("loop")) return true;
		lm.Loop();

		int mr = lm.Result();
		if(mr != MR_OK) return null;
		return ret.O;
	}
	

	public static HyDialog CreateInputDialog(final Context context, String title, final ParamList pl) throws Exception {
        String config = "items: [" + 
            	"tv: { id:100, text: '标题', w: match, color: lb, style: dlg_title }, " +  
            	"-: { h: 2dp }," +  
            	"et: { id:Input, hint: '', no-measure, bgc:#FFFFFFFF }, " +  
                "-," +  
                "[btn: { id:Cancel, text: '取消', style: btn }, " +   
                  "-," +  
                  "btn: { id:OK, text: '确定', style: btn }], " + 
        "], padding: 0dp, margin: 0dp, bgc: lb, w:300dp";
    	UCParamList plPanel = new UCParamList(config);
    	plPanel.AddRange(pl);

    	plPanel.SetValue("styles", new Param[] {
    		plPanel.newStyle("dlg_title", "fs: 18sp, margin: 0dp, padding: 8dp, bgc:lgray", new Param("bg1", R.drawable.cab_bg_holo)),
    		plPanel.newStyle("btn", "fs: 16sp, margin: 0dp,h:40dp, padding: 0dp, color: #FF000000,", new Param("bg", R.drawable.button_bg_flat)),
        });
        ParamList plTitle = plPanel.findViewParams(100);
        if(plTitle != null) plTitle.SetValidValue("text", title);

        final TempV temp = new TempV();
        
        pl.set("onChange", new EventHandleListener() {
			
			@Override
			public void Handle(Object sender, ParamList arg) throws Exception {
				
			}
		});

        // 创建选择面板
         HyDialog dialog = CreateDialog(plPanel);
         temp.set(dialog);
		
         return dialog;
	}

	public static Object Input2(final Context context, String title, final ParamList pl) {
		try {
			HyDialog dialog = CreateInputDialog(context, title, pl);
			dialog.showDialog();
			return dialog.Result;
		} catch (Exception e) {
			ExProc.Show(e);
			return null;
		}
	}
	
	public static int Hint(Context context, String msg) {
		return Show(context, "", msg, MI_Info | MB_Ok, null);
	}
	
	public static int Hint(String msg) {
		return Show(HyApp.CurrentActivity(), "", msg, MI_Info | MB_Ok, null);
	}
	
	public static boolean Ask(Context context, String msg) {
		return Show(context, "", msg, MI_Ask, null) == MR_OK;
	}
	
	public static boolean Ask(String msg) {
		return Show(HyApp.CurrentActivity(), "", msg, MI_Ask, null) == MR_OK;
	}
	
	private static AlertDialog.Builder CreateChooseDialogBuilder(String title, final String[] items, final ParamList pl) {
		final Context context = HyApp.CurrentActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);  
		//builder.setIcon(R.drawable.ic_launcher);  
		builder.setTitle(title);
		//    设置一个下拉的列表选择项  
		//builder.setItems(items, clickListener);  

		LinearLayout lay= new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	    lay.setLayoutParams(lp);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.setBackgroundColor(Color.WHITE);
        
        HyListView lv = UCCreator.CreateListView(context, true);
		lay.addView(lv);

		HyListAdapter apt = new HyListAdapter(context, Arrays.asList(items), R.layout.list_sale_item);
		
		lv.setAdapter(apt);
		apt.getViewListener = new HyListAdapter.OnGetViewListener() {

			@Override
			public View onGet(int position, View convertView, ViewGroup parent) {
				String value = items[position];
				if (convertView == null) {
					//convertView = LayoutInflater.from(context).inflate(R.layout.list_sale_item, null);
					
					LinearLayout ll = new LinearLayout(context);
			        int padding = gu.dp2px(context, 10);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT);
//					lp.gravity = Gravity.CENTER_VERTICAL;
				    ll.setLayoutParams(lp);
			        ll.setOrientation(LinearLayout.HORIZONTAL);
			        ll.setPadding(padding, padding, padding, padding);
			        
					TextView tv = new TextView(context);
					tv.setInputType(EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
					tv.setSingleLine(false);  
					int w = gu.getPx(context, pl.SValue("width"));
					//tv.setWidth(w == 0 ? LayoutParams.WRAP_CONTENT : w);
					
					tv.setTextSize(gu.sp2px(context, 8));
					tv.setTextColor(Color.BLACK);
					tv.setGravity(Gravity.CENTER_VERTICAL);
					tv.setId(100);
					
					ll.addView(tv);
					convertView = ll;
				}
				View v = convertView.findViewById(100);
				UIUtils.setViewValue(v, value);
				v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

				return convertView;
			}
		};

		builder.setView(lay);
		
		//    设置一个下拉的列表选择项  
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					pl.Call("onItemClick", context, new ParamList("which", position));
				} catch (Exception e) {
					ExProc.Show(e);
				}
			}
		});
		return builder;
	}
	
	public static HyDialog CreateDialog(ParamList pl) {
		Context context = HyApp.CurrentActivity();
    	final HyDialog dlg = new HyDialog(context, R.style.hy_dialog);
    	dlg.setCancelable(pl.getValue("Cancelable", true));
    	try {
          //  dlg.setContentView(R.layout.dlg);
    		Window window = dlg.getWindow();
        	WindowManager.LayoutParams params = window.getAttributes();
        	
          //  params.width = 240*2; 
          //  params.height = 240*2;
            
        //	dlg.getWindow().setBackgroundDrawableResource(R.drawable.dlg_bg_normal);

            //window.findViewById(R.id.ll_bg).setBackgroundResource(R.drawable.abc_menu_dropdown_panel_holo_light);
            
            //ViewGroup vg = (ViewGroup)window.findViewById(R.id.ll_content);
                ViewGroup vg =  (ViewGroup)window.getDecorView();

            // 对话框参数配置
        	UICreator uc = new UICreator(dlg.getContext());

            // 创建面板
            ViewGroup pnl = uc.CreatePanel(pl);

            pnl.setBackgroundResource(R.drawable.abc_menu_dropdown_panel_holo_light);

            if(pl.containsKey("onClick")) UIUtils.BindingClickEvent(pl.getEvent("onClick"), pnl);
            
            vg.addView(pnl);
            //dlg.addContentView(pnl, params);
         //   dlg.getWindow().setAttributes(params);
		} catch (Exception e) {
			ExProc.Show(e);
		}
    	return dlg;
	}

	public static HyDialog CreateChooseDialog(String title, final DataRowCollection rows, final ParamList pl, final DialogInterface.OnClickListener clickListener) throws Exception {
		String w = pl.getValue("width", "300dp");
		String ih = pl.getValue("item-height", "wrap");

		if(!pl.containsKey("item-height") && pl.BValue("summary")) ih = "36dp";
		
		Object[] buttons = pl.getArray("buttons");
		List<Object> lsButtons = new ArrayList<Object>();
		if(pl.BValue("stay")) pl.SetValue("Cancelable", false, false);
		if(buttons == null) {
			if(!pl.getValue("Cancelable", true))
				lsButtons.add("btn: { id:Cancel, text: '取消', style: btn }, ");
		} else
			for (Object o : buttons) 
				if(o instanceof String) {
					ActionInfo act = ActionList.getAction((String)o);
					if(act != null) {
						lsButtons.add("btn: { id:" + act.Name + ", text: '" + act.Title + "', style: btn }, ");
					}
				}
		
        String config = "items: [" + 
            	"tv: { id:100, text: '标题', w: match, color: lb, style: dlg_title }, " + 
            	"-: { h: 2dp, bgc: holo }," +   
            	"lv: { id:101, hint: '选择列表', no-measure, bgc:white }, " +  
//                	"-," +  
//                	"[btn: { id:OK, text: '确定', style: btn }, " +   
//                	"-," +  
				(lsButtons.size() == 0 ? "" :  
                	"-: { h: 1dp, bgc: holo }," +  
					gs.JoinList(lsButtons, ",")
                ) + 
        "], padding: 0dp, margin: 0dp, bgc: lb, w:" + w;
    	UCParamList plPanel = new UCParamList(config);
    	plPanel.AddRange(pl);

    	plPanel.SetValue("styles", new Param[] {
    		plPanel.newStyle("dlg_title", "fs: 18sp, margin: 0dp, padding: 8dp, bgc:lgray", new Param("bg1", R.drawable.cab_bg_holo)),
    		plPanel.newStyle("btn", "fs: 16sp, margin: 0dp,h:40dp, padding: 0dp, color: #333,", new Param("bg", R.drawable.button_bg_flat)),
        });
        ParamList plTitle = plPanel.findViewParams(100);
        if(plTitle != null) plTitle.SetValidValue("text", title);
        ParamList plLv = plPanel.findViewParams(101);
        
        plLv.SetValue("items", rows);
        plLv.SetValue("dm", pl.get("dm"));
        if(pl.BValue("summary")) plLv.SetValue("summary", 1);
        pl.CopyTo("min-ih", plLv);

        // 列表项视图配置
        String cfgListItem = "items: [" + 
        	"tv: { id:Info, fs: 16dp, text: info,color:black,bgc1:r, marginLeft:2dp, grv:cv,padding: 0dp, w:match,h:" + ih + " }, "+ 
        "], grv:r, padding:10dp, margin: 0dp, w:match, h:wrap, bgc1:lb";
//	        	
        plLv.SetValue("itemViewConfig", cfgListItem);
     //   plLv.SetValue("dm", "SName");// 显示成员
        
        final TempV temp = new TempV();
        
        plLv.set("onEvent", new EventHandleListener() {
			
			@Override
			public void Handle(Object sender, ParamList arg) throws Exception {
				HyDialog dialog = temp.get();
				if(arg instanceof LvItemClickEventParams) {
					if(clickListener != null)
						clickListener.onClick(dialog, ((LvItemClickEventParams)arg).getPosition());
					dialog.Result = ((LvItemClickEventParams)arg).getPosition();
					if(!pl.BValue("stay")) dialog.dismiss();
				}
			}
		});

        plPanel.set("onClick", new EventHandleListener() {
			
			@Override
			public void Handle(Object sender, ParamList arg) throws Exception {
				HyDialog dialog = temp.get();
				View v = type.as(sender, View.class);

		        pl.Call("onClick", sender, arg);
				if(v.getId() == ID.Cancel) dialog.dismiss();
				if(v.getId() == ID.Close) dialog.dismiss();
			}
		});

        // 创建选择面板
         HyDialog dialog = CreateDialog(plPanel);
         temp.set(dialog);
		
         return dialog;
	}

	public static int Choose(String title, DataRowCollection rows, final ParamList pl) {
		try {
			HyDialog dialog = CreateChooseDialog(title, rows, pl, null);

			dialog.showDialog();
			Object result = dialog.Result;
			return result == null ? -1 : (Integer)result;
		} catch (Exception e) {
			ExProc.Show(e);
			return -1;
		}
	}

	public static void Choose(String title, DataRowCollection rows, final ParamList pl, final DialogInterface.OnClickListener clickListener) {
		try {
			HyDialog dialog = CreateChooseDialog(title, rows, pl, clickListener);
			dialog.show();
		} catch (Exception e) {
			ExProc.Show(e);
		}
	}
	
	public static int Choose(String title, String[] items, final ParamList pl) {
		DataTable dt = new DataTable("t", "c", items);
		return Choose(title, dt.getRows(), pl);
	}

	public static void Choose(String title, final String[] items, final ParamList pl, final DialogInterface.OnClickListener clickListener) {
		DataTable dt = new DataTable("t", "c", items);
		Choose(title, dt.getRows(), pl, clickListener);
	}
}

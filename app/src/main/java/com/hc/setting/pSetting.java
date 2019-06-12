package com.hc.setting;

import hylib.data.TableInfo;
import hylib.data.TableList;
import hylib.toolkits.ExProc;
import hylib.toolkits._D;

public class pSetting {
	public static TableList Tables;
	
	public static void Init() {
		LoadTabsConfigs();
	}

    public static void LoadTabsConfigs()
    {
        try
        {
            if(Tables != null) return;
        	Tables = new TableList();
            for(String s : Configs.Tables)
            	Tables.add(new TableInfo(s));
            _D.Dumb();
        }
        catch (Exception ex)
        {
            ExProc.Show(ex);
        }
    }
}

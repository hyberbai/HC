package com.hc.dal;

import java.util.Date;

import com.hc.SysData;
import com.hc.db.DBLocal;

import hylib.db.SqlHelper;
import hylib.toolkits.ExProc;
import hylib.toolkits.gv;
import hylib.util.ParamList;

public class Setting {
	public final static int SYS_UID = 999999999;

    public static Object GetSetting(int opid, int SCID)
    {
        ParamList pl = GetSettingParams(opid, SCID);
        return pl.get("v");
    }
	
    public static <T> T GetSetting(int opid, int SCID, T defaultValue) {
    	Object v = GetSetting(opid, SCID);
    	return gv.IsEmpty(v) ? defaultValue : (T)gv.ConvertType(v, defaultValue); 
	}

    public static ParamList GetSettingParams(int UID, int SCID)
    {
        String v = DBLocal.ExecuteScalar("select v from Settings where UID=" + UID + " and SCID=" + SCID);
        try {
        	return new ParamList(v);
		} catch (Exception e) {
			ExProc.Show(e);
			return null;
		}
    }

    public static void SetSetting(int UID, int SCID, Object value)
    {
    	ParamList pms = value instanceof ParamList ? (ParamList)value : new ParamList("v", value);
        String v = SqlHelper.SqlVal(pms.toString());
        int ID = DBLocal.ExecuteIntScalar("select ID from Settings where UID=" + UID + " and SCID=" + SCID);
        if (ID < 0)
        	DBLocal.ExecSQL("insert into Settings(UID, SCID, V) values(" + UID + "," + SCID + "," + v + ")");
        else
        	DBLocal.ExecSQL("update Settings set V=" + v + " where SCID=" + SCID + " and UID=" + UID);
    }

    /*
     * 获取系统参数
     */
    public static Object GetSysSetting(int SCID) {
    	return GetSetting(SYS_UID, SCID);
	}

    public static <T> T GetSysSetting(int SCID, T defaultValue) {
    	return GetSetting(SYS_UID, SCID, defaultValue); 
	}
    
    public static ParamList GetSysSettingParams(int SCID) {
    	return GetSettingParams(SYS_UID, SCID);
	}

    public static void SetSysSetting(int SCID, Object value)
    {
    	SetSetting(SYS_UID, SCID, value);
    }

    /*
     * 获取用户参数
     */
    public static Object GetUserSetting(int SCID) {
    	return GetSetting(SysData.op_id, SCID);
	}
	
    public static <T> T GetUserSetting(int SCID, T defaultValue) {
    	return GetSetting(SysData.op_id, SCID, defaultValue); 
	}
    
    public static ParamList GetUserSettingParams(int SCID) {
    	return GetSettingParams(SysData.op_id, SCID);
	}

    public static void SetUserSetting(int SCID, Object value)
    {
    	SetSetting(SysData.op_id, SCID, value);
    }
}

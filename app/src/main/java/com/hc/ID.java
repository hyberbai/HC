package com.hc;

import java.lang.reflect.Field;

import hylib.toolkits._D;
import hylib.util.ParamList;


public class ID {
	public static int START = 100000;		//

	public static int OK = 	   100;
	public static int Back   = 102;
	public static int Cancel = 103;
	public static int Close  = 104;
	public static int Options = 109;
	
	public static int Add =    201;
	public static int Modify = 202;
	public static int Delete = 203;

	public static int Title = 300;
	public static int Stats = 301;
	public static int Total = 302;

	public static int Info = 1000;
	public static int Input = 1001;
	public static int Summary = 1009;


	public static int Icon = 1201;

	public static int User = newID();		// 用户
	public static int Pwd = newID();		// 密码
	public static int Stock = newID();		// 仓库
	public static int SynData = newID();	// 同步数据
	public static int Cust = newID();		// 客户
	public static int RemPwd = newID();		// 记住密码
	public static int Login = newID();		// 登录
	public static int Exit = newID();		// 退出
	
	                                               
	public static int HZNo = newID();		// 患者编号
	public static int HZ = newID();			// 患者姓名
	public static int Doc = newID();		// 医生
	public static int Sex = newID();		// 年龄
	public static int Age = newID();		// 床位号
	public static int KS = newID();			// 科室 
	public static int SSLX = newID();		// 手术类型
	public static int CWNo = newID();		// 床位号
	public static int ZJ = newID();			// 外请专家

	public static int SNo = newID();		// 流水号
	
	public static int FNumber = newID();	// 产品编号
	public static int FName1 = newID();		// 产品名称
	public static int SName = newID();		// 产品简称
	public static int FModel = newID();		// 规格型号
	public static int Cls = newID();		// 产品类别
	public static int Manuf = newID();		// 生产厂家
	public static int Reg = newID();		// 注册证号
	public static int Price = newID();		// 单价
	public static int Unit = newID();		// 单位
	public static int Qty = newID();		// 数量
	public static int FBatchNo = newID();	// 产品批号
	public static int MfgDate = newID();	// 生产日期
	public static int ExpDays = newID();	// 保质期
	public static int ExpDate = newID();	// 有效期至
	public static int SP = newID();			// 供货单位
	public static int FNote = newID();		// 备注
	

	public static int BillNo = newID();		// 单据编号
	public static int SCStock = newID();	// 调出库
	public static int DCStock = newID();	// 调入库

	public static int Tot = newID();		// 合计
	
	private static int tid;
	public static int newID() {
		return START + ++tid;
	}
	
	public static ParamList map = new ParamList();
	
	static {
		Class<ID> clazz = ID.class;
        for( Field field : clazz.getFields())
			try {
				if(field.getType() != int.class) continue;
		        map.set(field.getName(), field.getInt(clazz));
			} catch (Exception e) {
				_D.Out(e);
			}
	}

	public static int valueOf(String name)
	{
		return map.containsKey(name) ? map.IntValue(name) : -1;
	}
}
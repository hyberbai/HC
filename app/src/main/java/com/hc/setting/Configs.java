package com.hc.setting;

public class Configs {
	
    public static String[] Tables = new String[] {

        // 生产商
        "table: { Name: Manuf, TCnName: 生产商 }," +
        "fields: [" +
        "  @FItemID, " + // 主键
        "	FNumber:  { cn: 编号, config: { w: 10ec } }," +
        "	FName:    { cn: 名称, config: { w: 6cc } }," +
        "]",
        
        // 产品类别
        "table: { Name: ItemCls, TCnName: 生产商 }," +
        "fields: [" +
        "  @IID, " + // 主键
        "	INo:  	{ cn: 编号, config: { w: 10ec } }," +
        "	IName:  { cn: 名称, config: { w: 6cc } }," +
        "]",

        // 医院信息：医生
        "table: { Name: Doc, TID: 9451, TCID:451, TName: Dict, TCnName: 医生 }," +
        "fields: [" +
        "  @IID, " + // 主键
        "	No:       { cn: 编号, config: { w: 10ec } }," +
        "	FName:    { cn: 姓名, config: { w: 6cc } }," +
        "	T1:       { cn: 联系电话, alias: tel, config: { w: 20ec } }," +
        "	T2:       { cn: 科室, alias: ks, config: { w: 10cc } }," +
        "]",

        // 医院信息：科室
        "table: { Name: KS, TID: 9452, TCID:452, TName: Dict, TCnName: 科室 }," +
        "fields: [" +
        "  @IID, " + // 主键
        "	No:       { cn: 编号, config: { w: 10ec } }," +
        "	FName:    { cn: 名称, config: { w: 10cc } }," +
        "]",

        // 医院信息：手术类型
        "table: { Name: SSLX, TID: 9453, TCID:453, TName: Dict, TCnName: 手术类型 }," +
        "fields: [" +
        "  @IID, " + // 主键
        "	No:       { cn: 编号, config: { w: 10ec } }," +
        "	FName:    { cn: 名称, config: { w: 15cc } }," +
        "]",

    };
}

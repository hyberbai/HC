package hylib.db;

public enum SqlDbType {
    BigInt,

    // System.Byte 类型的 System.Array。二进制数据的固定长度流，范围在 1 到 8,000 个字节之间。
    Binary,

    // System.Boolean.无符号数值，可以是 0、1 或 null。
    Bit,

    // System.String.非 Unicode 字符的固定长度流，范围在 1 到 8,000 个字符之间。
    Char,

    // System.DateTime.日期和时间数据，值范围从 1753 年 1 月 1 日到 9999 年 12 月 31 日，精度为 3.33 毫秒。
    DateTime,

    // System.Decimal.固定精度和小数位数数值，在 -10 38 -1 和 10 38 -1 之间。
    Decimal,

    // System.Double.-1.79E +308 到 1.79E +308 范围内的浮点数。
    Float,

    // System.Byte 类型的 System.Array。二进制数据的可变长度流，范围在 0 到 2 31 -1（即 2,147,483,647）字节之间。
    Image,

    // System.Int32.32 位带符号整数。
    Int,

    // System.Decimal.货币值，范围在 -2 63（即 -922,337,203,685,477.5808）到 2 63 -1（即 +922,337,203,685,477.5807）之间，精度为千分之十个货币单位。
    Money,

    // System.String.Unicode 字符的固定长度流，范围在 1 到 4,000 个字符之间。
    NChar,

    // System.String.Unicode 数据的可变长度流，最大长度为 2 30 - 1（即 1,073,741,823）个字符。
    NText,

    // System.String.Unicode 字符的可变长度流，范围在 1 到 4,000 个字符之间。如果字符串大于 4,000 个字符，隐式转换会失败。在使用比
    // 4,000 个字符更长的字符串时，请显式设置对象。
    NVarChar,

    // System.Single.-3.40E +38 到 3.40E +38 范围内的浮点数。
    Real,

    // System.Guid.全局唯一标识符（或 GUID）。
    UniqueIdentifier,

    // System.DateTime.日期和时间数据，值范围从 1900 年 1 月 1 日到 2079 年 6 月 6 日，精度为 1 分钟。
    SmallDateTime,

    // System.Int16.16 位的带符号整数。
    SmallInt,

    // System.Decimal.货币值，范围在 -214,748.3648 到 +214,748.3647 之间，精度为千分之十个货币单位。
    SmallMoney,

    // System.String.非 Unicode 数据的可变长度流，最大长度为 2 31 -1（即 2,147,483,647）个字符。
    Text,

    // System.Byte 类型的 System.Array。自动生成的二进制数字，它们保证在数据库中是唯一的。timestamp 通常用作为表行添加版本戳的机制。存储大小为
    // 8 字节。
    Timestamp,

    // System.Byte.8 位无符号整数。
    TinyInt,

    // System.Byte 类型的 System.Array。二进制数据的可变长度流，范围在 1 到 8,000 个字节之间。如果字节数组大于 8,000
    // 个字节，隐式转换会失败。在使用比 8,000 个字节大的字节数组时，请显式设置对象。
    VarBinary,

    // System.String.非 Unicode 字符的可变长度流，范围在 1 到 8,000 个字符之间。
    VarChar,

    // System.Object.特殊数据类型，可以包含数值、字符串、二进制或日期数据，以及 SQL Server 值 Empty 和 Null，后两个值在未声明其他类型的情况下采用。
    Variant,

    // XML 值。使用 System.Data.SqlClient.SqlDataReader.GetValue(System.Int32) 方法或 System.Data.SqlTypes.SqlXml.Value
    // 属性获取字符串形式的 XML，或通过调用 System.Data.SqlTypes.SqlXml.CreateReader() 方法获取 System.Xml.XmlReader
    // 形式的 XML。
    Xml,

    // SQL Server 2005 用户定义的类型 (UDT)。
    Udt,

    // 指定表值参数中包含的构造数据的特殊数据类型。
    Structured,

    // 日期数据，值范围从公元 1 年 1 月 1 日到公元 9999 年 12 月 31 日。
    Date,

    // 基于 24 小时制的时间数据。时间值范围从 00:00:00 到 23:59:59.9999999，精度为 100 毫微秒。对应于 SQL Server
    // time 值。
    Time,

    // 日期和时间数据。日期值范围从公元 1 年 1 月 1 日到公元 9999 年 12 月 31 日。时间值范围从 00:00:00 到 23:59:59.9999999，精度为
    // 100 毫微秒。
    DateTime2,

    // 显示时区的日期和时间数据。日期值范围从公元 1 年 1 月 1 日到公元 9999 年 12 月 31 日。时间值范围从 00:00:00 到 23:59:59.9999999，精度为
    // 100 毫微秒。时区值范围从 -14:00 到 +14:00。
    DateTimeOffset,
}
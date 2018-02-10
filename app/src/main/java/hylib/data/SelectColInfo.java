package hylib.data;

public class SelectColInfo {
	public String Name;
	public String Alias;
	public int DataType;
	public SqlExpression expression;
	
	public void setName(String name) {
		Name = name;
		Alias = name;
	}

	public void setExp(SqlUtils.ComputeType compType, Object exp) {
		expression = new SqlExpression();
		expression.CompType = compType;
		expression.Exp = exp;
	}
	
	public boolean isNormal() {
		return expression == null || expression.isNormal();
	}
	
	public boolean isSum() {
		return expression != null && expression.isSumType();
	}
	
	public boolean isCount() {
		return expression != null && expression.isCountType();
	}
	
	public boolean isItems() {
		return expression != null && expression.isItems();
	}
	
	// 可求合进行统计的字段
	public boolean isStat() {
		return expression != null && (expression.isCountType() || expression.isSumType());
	}

	@Override
	public String toString() {
		String exp = expression != null ? expression.toString() : Name;
		return exp.equalsIgnoreCase(Alias) ? exp : exp + " " + Alias;
	}
}

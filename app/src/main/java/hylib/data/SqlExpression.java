package hylib.data;

import hylib.data.SqlUtils.ComputeType;

public class SqlExpression {
	public ComputeType CompType;
	public Object Exp;

	@Override
	public String toString() {
		return CompType == ComputeType.None ? Exp.toString() :
			   CompType == ComputeType.COUNT ? "COUNT" :
			   CompType.toString() + "(" + Exp.toString() + ")";
	}
	
	public boolean isNormal() {
		return CompType == ComputeType.None;
	}

	public boolean isCountType() {
		return CompType == ComputeType.COUNT;
	}

	public boolean isSumType() {
		return CompType == ComputeType.SUM;
	}

	public boolean isAvgType() {
		return CompType == ComputeType.AVG;
	}

	public boolean isMaxType() {
		return CompType == ComputeType.MAX;
	}

	public boolean isMinType() {
		return CompType == ComputeType.MIN;
	}

	public boolean isItems() {
		return CompType == ComputeType.Items;
	}
}

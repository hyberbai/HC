package hylib.toolkits;


public class UnitValue {
	public UnitType Type;
	public float Value;
	
	public UnitValue(UnitType type, float value) {
		Type = type; 
		Value = value;
	}
	
	public int IntVal() {
		return (int)Value;
	}
}
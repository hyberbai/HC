package hylib.toolkits;

public class StringBuilderEx  {
	private StringBuilder mBuilder;
	
	public StringBuilderEx() {
		mBuilder = new StringBuilder();
	}

	public void append(String text) {
		mBuilder.append(text);
	}

	public void appendLine(String text) {
		mBuilder.append(text + "\n");
	}

	public void appendLine() {
		mBuilder.append("\n");
	}
	
	@Override
	public String toString() {
		return mBuilder.toString();
	}
}

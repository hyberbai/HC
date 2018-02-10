package hylib.toolkits;

public class ARGB {
	public int A;
	public int R;
	public int G;
	public int B;
	
	public ARGB(int color){
	    A = (color >>> 24);
	    R = (color >>  16) & 0xFF;
	    G = (color >>   8) & 0xFF;
	    B = (color)        & 0xFF;
	}

	public int toColor(){
	    return A << 24 | R << 16 | G << 8 | B;
	}
}
package hylib.toolkits;

import hylib.util.Param;
import hylib.util.ParamList;
import android.R;
import android.R.integer;
import android.graphics.Color;

public class HyColor {
    public static final int LightGreen  = 0xFFC8FFC8;
    public static final int DeepGreen  	= 0xFF108010;
    
    public static final int LightRed 	= 0xFFF0E68C;
    public static final int DeepRed  	= 0xFF60E68C;
    
    public static final int LightBlue 	= 0xFF3399FF;
    public static final int DeepBlue  	= 0xFF2222CC;
    
    public static final int LightYellow = 0xFFFFC864;
    public static final int DeepYellow 	= 0xFF8FC864;
    
    public static final int LightGray 	= 0xEEEEEEEE;
    public static final int DeepGray 	= 0xFF222222;

    public static final int HoloBlue	= 0xff33b5e5;
    
    public static final int LightBG		= 0xFFEEF8FF;
    public static final int LightBGLine	= 0xFFCED8EF;
    
    public static final int Text		= 0xFF333333;
    
    public static final int OK       = LightGreen;
    public static final int WRONG 	 = LightRed;
    public static final int FAIL     = LightRed;
    public static final int ERROR    = LightRed;
    public static final int ALERT    = LightYellow;

    public static byte SafeCC(int cc)
    {
        return (byte)(cc > 255 ? 255 : cc < 0 ? 0 : cc);
    }
    
    public static int Blend(int color1, int color2, float k)
    {
        if (k >= 1) return color1;
        if (k <= 0) return color2;
        float k2 = 1 - k;
        ARGB c1 = new ARGB(color1);
        ARGB c2 = new ARGB(color2);
        c1.R = ((int)(c1.R * k + c2.R * k2));
        c1.G = ((int)(c1.G * k + c2.G * k2));
   		c1.B = ((int)(c1.B * k + c2.B * k2));
        return c1.toColor();
    }

	
	public static ParamList plColor = new ParamList(
			new Param("black", Color.BLACK),
			new Param("white", Color.WHITE),
			new Param("w", Color.WHITE),
			new Param("gray", Color.GRAY),
			new Param("lgray", LightGray),
			new Param("red", Color.RED),
			new Param("green", Color.GREEN),
			new Param("blue", Color.BLUE),
			new Param("r", Color.RED),
			new Param("g", Color.GREEN),
			new Param("b", Color.BLUE),

			new Param("text", Text),
			
			new Param("holo", HoloBlue),
			new Param("lb", HyColor.LightBlue),
			new Param("db", HyColor.DeepBlue),
			new Param("lr", HyColor.LightRed),
			new Param("dr", HyColor.DeepRed),
			new Param("lg", HyColor.LightGreen),
			new Param("dg", HyColor.DeepGreen),
			
			new Param("bg", LightBG),
			new Param("bgl", LightBGLine),
			
			new Param("ok", HyColor.OK),
			new Param("wrong", HyColor.WRONG),
			new Param("err", HyColor.ERROR),
			new Param("fail", HyColor.FAIL),
			new Param("alert", HyColor.ALERT)
		);
}

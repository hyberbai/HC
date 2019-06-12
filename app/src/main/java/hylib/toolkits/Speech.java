package hylib.toolkits;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

import hylib.sys.HyApp;

public class Speech {
    private static TextToSpeech mTextToSpeech =null; // TTS对象  

    public static void Init() {
        if(mTextToSpeech != null) return;
    	final Context context = HyApp.getAppContext();
        //实例并初始化TTS对象
        mTextToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            
            @Override
            public void onInit(int status) {
                if (status==TextToSpeech.SUCCESS) {
                    //设置朗读语言
                    int supported=mTextToSpeech.setLanguage(Locale.getDefault());
                    if ((supported!=TextToSpeech.LANG_AVAILABLE)&&(supported!=TextToSpeech.LANG_COUNTRY_AVAILABLE)) {
                        _D.Out("TextToSpeech 不支持当前语言！");
                    }
                    //Speak("测试");
                }
                
            }
        });
	}

    //朗读监听按钮
    public static void Speak(String text) {
    	if(mTextToSpeech == null) 
    		Init();
        //朗读EditText里的内容
        int r = mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        _D.Out("SpeechResult:" + r);
    }

    public static void Close() {
    	mTextToSpeech.shutdown();
    	mTextToSpeech = null;
    }
}

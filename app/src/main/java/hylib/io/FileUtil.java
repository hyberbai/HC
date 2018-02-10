package hylib.io;

import hylib.toolkits.gs;
import hylib.toolkits.gu;
import hylib.toolkits.gv;
import hylib.util.ParamList;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import android.R.integer;


public class FileUtil {
	
	public static boolean FileExists(String fileName) {  
	    File file= new File(fileName);    
	    return file.exists();    
	}  
	
	public static RandomAccessFile NewFile(String fileName) throws Exception {  
	    File file= new File(fileName);    
	    if(file.exists()) file.delete();
		return new RandomAccessFile(file, "rw");  
	}
	
	public static void SaveToFile(String pathName, byte[] bytes) throws Exception {
		RandomAccessFile file = NewFile(pathName);
		file.write(bytes);
		file.close();
	}

	public static void SetFileLastModified(String pathName, long time) throws Exception {
		File f = new File(pathName);
		f.setLastModified(time);
		long i = f.lastModified();
	}

	public static String[] searchFiles(String path, String pattText, ParamList pl){
        File file = new File(path);
        
        pattText = gs.TextEscape(pattText, ".+$^[](){}|\\/");
        pattText = "^" + pattText.replace("*", "([\\s\\S]*)").replace("?", "([\\s\\S]+)");
        Pattern patt = pattText.isEmpty() ? null : Pattern.compile(pattText);
        
        List<String> list = new ArrayList<String>();
        searchFiles(list, file, patt, pl.BValue("includeSub"));
        return list.toArray(new String[0]);
    }


	public static String[] searchFiles(String path, String pattText){
        return FileUtil.searchFiles(path, pattText, new ParamList());
	}
    
    private static void searchFiles(List<String> list, File file, Pattern patt, boolean includeSub) {
    	File[] files = file.listFiles();
    	if(file.listFiles() == null) return;
    	
        for( File f : files ) {
            if(patt == null || patt.matcher(f.getName()).find())
                list.add(f.getPath());

            if(f.isDirectory() && includeSub)
                searchFiles(list, f, patt, true);
        }
	}
    
	public static long DAY_MS = 24 * 3600 * 1000;
	public static long getIdTime(int id) {
		Date dt = new Date();
	    long time= dt.getTime();
	    return time - time % DAY_MS + (id % DAY_MS);
	}

	public static int getFileTimeID(String pathName) {
		return (int)(new File(pathName).lastModified() % DAY_MS);
	}

	public static boolean DeleteFile(String fileName) {  
		File file = new File(fileName);  

	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        return true;  
	    }  
	    return false;  
	}
	
	public static void DeleteFiles(String[] files) {
		for (String f : files)
			DeleteFile(f);
	}
	
	public static void DeleteFiles(String path, String pattText) {
		String[] files = searchFiles(path, pattText, new ParamList());
		DeleteFiles(files);
	}
	
	public static boolean DeleteDirectory(String path) {  
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
	    if (!path.endsWith(File.separator)) {  
	        path = path + File.separator;  
	    }  
	    File dirFile = new File(path);  
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return false;  
	    }
	    
	    Boolean ok;
	    //删除文件夹下的所有文件(包括子目录)  
	    File[] files = dirFile.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        if (files[i].isFile()) //删除子文件  
	            ok = DeleteFile(files[i].getAbsolutePath());  
	        else  //删除子目录  
	            ok = DeleteDirectory(files[i].getAbsolutePath());  
	        if (!ok) return false;  
	    }  

	    //删除当前目录  
	    return dirFile.delete();
	}  
}

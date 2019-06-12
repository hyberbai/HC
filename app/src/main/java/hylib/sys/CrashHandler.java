package hylib.sys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import hylib.toolkits.ExProc;

import static android.util.Log.i;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 */
public class CrashHandler implements UncaughtExceptionHandler {
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static CrashHandler INSTANCE;
    // 程序的Context对象
    private Context mContext;

    //保证只有一个CrashHandler实例
    private CrashHandler() {
    }

    //获取CrashHandler实例 ,单例模式
    public static CrashHandler getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CrashHandler();
        return INSTANCE;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该重写的方法来处理
     */
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果自定义的没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     */
    public boolean handleException(Throwable ex) {
        if (ex == null || mContext == null)
            return false;
        final String crashReport = getCrashReport(mContext, ex);
        new Thread() {
            public void run() {
                Looper.prepare();
                File file = save2File(crashReport);
                sendAppCrashReport(mContext, crashReport, file);
                Looper.loop();
            }

        }.start();
        return true;
    }

    @SuppressLint("SimpleDateFormat")
    private File save2File(String crashReport) {
        //用于格式化日期,作为日志文件名的一部分
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = dateFormat.format(new Date());
        String fileName = "crash-" + time + "-" + System.currentTimeMillis() + ".txt";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                //存储路径，是sd卡的crash文件夹
                File dir = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + File.separator + "crash");
                if (!dir.exists()) dir.mkdir();
                File file = new File(dir, fileName);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(crashReport.toString().getBytes());
                fos.close();
                return file;
            } catch (Exception e) {
                //sd卡存储，记得加上权限，不然这里会抛出异常
                i("Show", "save2File error:" + e.getMessage());
            }
        }
        return null;
    }

    private void sendAppCrashReport(final Context context, final String crashReport, final File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setTitle("系统出错")
            .setMessage("")
            .setPositiveButton("发送",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                String[] tos = {"bhb900@163.com"};
                                intent.putExtra(Intent.EXTRA_EMAIL, tos);

                                intent.putExtra(Intent.EXTRA_SUBJECT, "Android客户端 - 错误报告");
                                if (file != null)
                                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

                                intent.putExtra(Intent.EXTRA_TEXT, crashReport);
                                intent.setType("text/plain");
                                intent.setType("message/rfc882");
                                Intent.createChooser(intent, "{ host: smtp.163.com, name: bhbtest@163.com, pwd: b3... }");
                                context.startActivity(intent);
                            } catch (Exception e) {
                                ExProc.Show(e);
                            } finally {
                                Teminate(dialog);
                            }
                        }
                    })
            .setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Teminate(dialog);
                        }
                    });

        AlertDialog dialog = builder.create();
        //需要的窗口句柄方式，没有这句会报错的
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    private void Teminate(DialogInterface dialog){
        dialog.dismiss();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    /**
     * 获取APP崩溃异常报告
     */
    private String getCrashReport(Context context, Throwable ex) {
        PackageInfo pinfo = getPackageInfo(context);
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("软件版本: " + pinfo.versionName + "(" + pinfo.versionCode + ")\n");
        exceptionStr.append("安卓版本: " + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.MODEL + ")\n");
        exceptionStr.append("调用堆栈: " + ex.getMessage() + "\n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++)
            exceptionStr.append(elements[i] + "\n");

        return exceptionStr.toString();
    }

    /**
     * 获取App安装包信息
     */
    private PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

}
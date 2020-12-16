package pl.avgle.videos.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;
    private static final String PATH = Environment.getExternalStorageDirectory() + "/AvgleVideos/CrashLog/";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".txt";
    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private CrashHandler(){
    }

    public static CrashHandler getsInstance(){
        return sInstance;
    }

    public void init(Context context){
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            //导出异常信息到SD卡中
            dumpExceptionToSDCard(ex);
            //这里可以上传异常信息到服务器，便于开发人员分析日志从而解决bug
            uploadExceptionToServer();
        }catch (IOException e){
            e.printStackTrace();
        }
        ex.printStackTrace();
        //如果系统提供默认的异常处理器，则交给系统去结束程序，否则就由自己结束自己
        if(mDefaultCrashHandler != null){
            mDefaultCrashHandler.uncaughtException(thread, ex);
        }else {
            //自己处理
            try {
                //延迟2秒杀进程
                Thread.sleep(2000);
                Toast.makeText(mContext, "程序出错，正在退出", Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private void dumpExceptionToSDCard(Throwable ex) throws IOException{
        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            if(DEBUG){
                Log.e(TAG, "sdcard unmounted,skip dump exception");
                return;
            }
        }
        File dir = new File(PATH);
        if(!dir.exists()){
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(new Date(current));
        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
            Log.e(TAG, "dump crash info seccess");
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
            Log.e(TAG,"dump crash info failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw)throws PackageManager.NameNotFoundException{
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.getLongVersionCode());
        //Android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        //CPU架构
        pw.print("CPU ABI: ");
        for (int i=0,size=Build.SUPPORTED_ABIS.length; i<size; i++) {
            if (i == 0)
                pw.print("[" + Build.SUPPORTED_ABIS[i]);
            pw.print("," + Build.SUPPORTED_ABIS[i]);
            if (i == Build.SUPPORTED_ABIS.length -1)
                pw.println(Build.SUPPORTED_ABIS[i] + "]");
        }
    }

    private void uploadExceptionToServer(){
        //将异常信息发送到服务器
    }
}
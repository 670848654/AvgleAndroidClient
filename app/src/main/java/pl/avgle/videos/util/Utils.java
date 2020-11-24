package pl.avgle.videos.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.avgle.videos.BuildConfig;
import pl.avgle.videos.R;
import pl.avgle.videos.main.view.activity.VideosActivity;

public class Utils {

    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static AlertDialog alertDialog;

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    /**
     * 创建文件夹 文件
     */
    public static void createFile() {
        File cache = new File(Environment.getExternalStorageDirectory() + "/AvgleVideos/Cache");
        File database = new File(Environment.getExternalStorageDirectory() + "/AvgleVideos/Database");
        if (!cache.exists()) cache.mkdirs();
        if (!database.exists()) database.mkdirs();
    }

    /**
     * 好评百分比
     *
     * @param like  喜欢的数
     * @param count 总数
     * @return
     */
    public static String praise(double like, double count) {
        if (like == 0)
            return "0%";
        else if (count == 0)
            return "100%";
        else {
            double p3 = like / count * 100;
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
            df.setMaximumFractionDigits(0);
            return df.format(p3) + "%";
        }
    }

    /**
     * 秒转时分秒
     *
     * @param duration_d
     * @return
     */
    public static String timeParse(double duration_d) {
        DecimalFormat df = new DecimalFormat("#"); //四舍五入转换成整数
        int duration = Integer.parseInt(df.format(duration_d));
        int temp = 0;
        StringBuffer sb = new StringBuffer();
        temp = duration / 3600;
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

        temp = duration % 3600 / 60;
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

        temp = duration % 3600 % 60;
        sb.append((temp < 10) ? "0" + temp : "" + temp);
        return sb.toString();
    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * 选择浏览器
     *
     * @param url
     */
    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(context.getApplicationContext(), "没有找到匹配的程序", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014/06/14"）
     *
     * @param time
     * @return
     */
    public static String toDate(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd");
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    /**
     * 获取xml String
     * @param id
     * @return
     */
    public static String getString(@StringRes int id) {
        return getContext().getResources().getString(id);
    }

    /**
     * 频道选择
     * @param activity
     * @param type
     * @param order
     * @param name
     * @param bundle
     * @return
     */
    public static void showHomeTimeDialog(Activity activity, int type, String order, String name, Bundle bundle) {
        String[] Items = getArray(R.array.home_order);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(name);
        builder.setItems(Items, (arg0, index) -> {
            switch (index) {
                case 0:
                    stringBuffer.append(" - "  + Items[0]);
                    bundle.putString("time", "t");
                    break;
                case 1:
                    stringBuffer.append(" - "  + Items[1]);
                    bundle.putString("time", "w");
                    break;
                case 2:
                    stringBuffer.append(" - "  + Items[2]);
                    bundle.putString("time", "m");
                    break;
                case 3:
                    stringBuffer.append(" - "  + Items[3]);
                    bundle.putString("time", "a");
                    break;
            }
            bundle.putInt("type", type);
            bundle.putString("order", order);
            bundle.putString("name", stringBuffer.toString());
            Intent intent = new Intent(activity, VideosActivity.class);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getASVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public static String[] getArray(@ArrayRes int id) {
        return getContext().getResources().getStringArray(id);
    }

    /**
     * 判断是否有NavigationBar
     *
     * @param activity
     * @return
     */
    public static boolean checkHasNavigationBar(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    /**
     * 获得NavigationBar的高度 +15
     */
    public static int getNavigationBarHeight(Activity activity) {
        int result = 0;
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0 && checkHasNavigationBar(activity)) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result + 15;
    }

    /**
     * dp转px
     * @param context
     * @param dp
     * @return
     */
    public static int dpToPx(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }

    /**
     * 加载框
     *
     * @return
     */
    public static ProgressDialog getProDialog(Context context, @StringRes int id) {
        ProgressDialog p = new ProgressDialog(context);
        p.setMessage(getString(id));
        p.setCancelable(false);
        p.show();
        return p;
    }


    /**
     * 关闭加载框
     *
     * @param p
     */
    public static void cancelProDialog(ProgressDialog p) {
        if (p != null)
            p.dismiss();
    }

    /**
     * 发现新版本弹窗
     * @param activity
     * @param version
     * @param body
     * @param posListener
     * @param negListener
     */
    public static void findNewVersion(Activity activity,
                                      String version,
                                      String body,
                                      DialogInterface.OnClickListener posListener,
                                      DialogInterface.OnClickListener negListener) {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
        builder.setMessage(body);
        builder.setTitle(getString(R.string.find_new_version) + version);
        builder.setPositiveButton(getString(R.string.update_now), posListener);
        builder.setNegativeButton(getString(R.string.update_after), negListener);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static int getActivityAppBarLayoutHeight() {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels / 16 * 9;
    }

    public static int getVideoItemHeight() {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return (dm.widthPixels - dpToPx(context,8)) / 16 * 9;
    }

    public static int getChannelTagHeight() {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return (dm.widthPixels / 2 - dpToPx(context,8)) / 16 * 9;
    }
}

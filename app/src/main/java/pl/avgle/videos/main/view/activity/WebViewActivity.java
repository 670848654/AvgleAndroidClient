package pl.avgle.videos.main.view.activity;

import android.app.PictureInPictureParams;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.OnClick;
import pl.avgle.videos.R;
import pl.avgle.videos.main.base.BaseActivity;
import pl.avgle.videos.main.base.MyWebView;
import pl.avgle.videos.main.base.Presenter;
import pl.avgle.videos.net.NetworkBasic;

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.webview)
    MyWebView myWebView;
    @BindView(R.id.progressBar)
    ProgressBar pg;
    @BindView(R.id.touch)
    LinearLayout touch;
    @BindView(R.id.speed)
    TextView speed;
    private String url;
    private long rxtxTotal =0;
    private DecimalFormat showFloatFormat = new DecimalFormat("0.00");
    private Handler handler = new Handler();
    private boolean isPip = false;

    @Override
    protected void initBeforeView() {
        hideNavBar();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_webview;
    }

    @Override
    protected Presenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initData() {
        hideGap();
        hideNavBar();
        getBundle();
        initWebView();
        handler.post(task);
    }

    public void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            url = bundle.getString("url");
        }
    }

    public void initWebView() {
        myWebView.setBackgroundColor(0);
        myWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        initHardwareAccelerate();
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
                if (url.contains("https://avgle.com/templates/frontend/avgle-main-ah.js")) {
                    String r = NetworkBasic.getSync(url);
                    int idx = r.indexOf("':if(!window[_") + 2;
                    String newResult = ";document.addEventListener('DOMContentLoaded', function(){setTimeout(function(){console.log('123');closeAd();}, 3000);});" + r.substring(0, idx) + "return false;" + r.substring(idx);
                    InputStream data = new ByteArrayInputStream(newResult.getBytes(StandardCharsets.UTF_8));
                    return new WebResourceResponse("text/plain", "UTF-8", data);
                } else if (url.contains("https://ads-a.juicyads.com/") && url.contains(".jpg")) {
                    return new WebResourceResponse("text/plain", "UTF-8", null);
                } else {
                    return super.shouldInterceptRequest(view, url);
                }
            }
        });
        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pg.setVisibility(View.GONE);
                } else {
                    pg.setVisibility(View.VISIBLE);
                    pg.setProgress(newProgress);
                }
            }
        });
        myWebView.loadUrl("file:///android_asset/avgle.html?url=" + url);
    }

    /**
     * 启用硬件加速
     */
    private void initHardwareAccelerate() {
        try {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        cancel();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        //释放资源
        if (myWebView != null)
            myWebView.destroy();
        super.onDestroy();
    }

    @Override
    protected void setLandscape() {

    }

    @Override
    protected void setPortrait() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavBar();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!touch.isShown() && !mActivityFinish) {
                    touch.setVisibility(View.VISIBLE);
                    touch.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
                    new Handler().postDelayed(() -> {
                        if (!mActivityFinish) {
                            touch.setVisibility(View.GONE);
                            touch.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
                        }
                    },2500);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @OnClick({R.id.ref, R.id.exit})
    public void touchClick(TextView textView) {
        touch.setVisibility(View.GONE);
        switch (textView.getId()) {
            case R.id.ref:
                myWebView.loadUrl("file:///android_asset/avgle.html?url=" + url);
                break;
            case R.id.exit:
                finish();
                break;
        }
    }

    private Runnable task = new Runnable() {
        public void run() {
            handler.postDelayed(this,2000);
            updateViewData();
        }
    };

    public void cancel(){
        handler.removeCallbacksAndMessages(null);
    }

    public void updateViewData() {
        long tempSum = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
        long rxtxLast = tempSum -rxtxTotal;
        double totalSpeed = rxtxLast *1000 /2000d;
        rxtxTotal = tempSum;
        speed.setText(showSpeed(totalSpeed));
    }

    private String showSpeed(double speed) {
        String speedString;
        if (speed >=1048576d)
            speedString =showFloatFormat.format(speed /1048576d) +"MB/s";
        else
            speedString =showFloatFormat.format(speed /1024d) +"KB/s";
        return speedString;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void enterPicInPic() {
        PictureInPictureParams builder = new PictureInPictureParams.Builder().build();
        enterPictureInPictureMode(builder);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (isInPictureInPictureMode) isPip = true;
        else isPip = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onUserLeaveHint() {
        enterPicInPic();
        super.onUserLeaveHint();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isPip) finish();
    }
}

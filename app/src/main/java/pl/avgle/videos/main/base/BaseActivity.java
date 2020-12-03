package pl.avgle.videos.main.base;

import android.Manifest;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.avgle.videos.R;
import pl.avgle.videos.adapter.SelectAdapter;
import pl.avgle.videos.application.Avgle;
import pl.avgle.videos.bean.SelectBean;
import pl.avgle.videos.database.DatabaseUtil;
import pl.avgle.videos.util.SharedPreferencesUtils;
import pl.avgle.videos.util.StatusBarUtil;
import pl.avgle.videos.util.Utils;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity<V, P extends Presenter<V>> extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    protected P mPresenter;
    protected View errorView, emptyView;
    protected TextView errorTitle;
    protected TextView mBottomSheetDialogTitle;
    protected BottomSheetDialog mBottomSheetDialog;
    protected RecyclerView selectRecyclerView;
    protected SelectAdapter selectAdapter;
    protected List<SelectBean> selectBeanList;
    protected Avgle application;
    protected int limit = 50;
    protected int nowPage = 0;
    //是否是第一次加载
    protected boolean isLoad = false;
    //是否有更更多
    protected boolean hasMore = true;
    protected boolean isErr = true;
    private Unbinder mUnBinder;
    protected boolean mActivityFinish = false;
    protected boolean isPortrait;
    protected boolean isDarkTheme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration mConfiguration = getResources().getConfiguration();
        isDarkTheme = (Boolean) SharedPreferencesUtils.getParam(this, "darkTheme", false);
        if (isDarkTheme) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        int ori = mConfiguration.orientation;
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) isPortrait = false;
        else if (ori == mConfiguration.ORIENTATION_PORTRAIT) isPortrait = true;
        initBeforeView();
        setContentView(getLayout());
        if (Utils.checkHasNavigationBar(this)) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getWindow().setNavigationBarDividerColor(Color.TRANSPARENT);
            }
        }
        mUnBinder = ButterKnife.bind(this);
        if (application == null) {
            application = (Avgle) getApplication();
        }
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Utils.createFile();
            DatabaseUtil.CREATE_TABLES();
            initData();
            setStatusBarColor();
            initCustomViews();
            mPresenter = createPresenter();
            loadData();
        } else {
            EasyPermissions.requestPermissions(this, Utils.getString(R.string.permissions_text),
                    300, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        initBottomSheetDialog();
    }

    public void initCustomViews() {
        errorView = getLayoutInflater().inflate(R.layout.base_error_view, null);
        errorTitle = errorView.findViewById(R.id.title);
        emptyView = getLayoutInflater().inflate(R.layout.base_emnty_view, null);
    }

    public void initBottomSheetDialog() {
        View selectView = LayoutInflater.from(this).inflate(R.layout.dialog_select, null);
        mBottomSheetDialogTitle = selectView.findViewById(R.id.title);
        selectRecyclerView = selectView.findViewById(R.id.select_list);
        selectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectAdapter = new SelectAdapter(this, selectBeanList);
        selectRecyclerView.setAdapter(selectAdapter);
        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(selectView);
    }

    protected abstract void initBeforeView();

    protected abstract int getLayout();

    protected abstract P createPresenter();

    protected abstract void loadData();

    protected abstract void initData();

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //有权限
        initData();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //无权限
        Toast.makeText(this, Utils.getString( R.string.no_permissions_text), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Android 9 异形屏适配
     */
    protected void hideGap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
    }

    /**
     * 隐藏虚拟导航按键
     */
    protected void hideNavBar() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * 显示虚拟导航按键
     */
    protected void showNavBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    public void setStatusBarColor() {
        if (!getRunningActivityName().equals("StartActivity") &&
                !getRunningActivityName().equals("ChannelActivity") &&
                !getRunningActivityName().equals("VideosActivity") &&
                !getRunningActivityName().equals("WebViewActivity")) {
                StatusBarUtil.setColorForSwipeBack(this, getResources().getColor(R.color.colorPrimary), 0);
        }
    }

    private String getRunningActivityName() {
        String contextString = this.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1,
                contextString.indexOf("@"));
    }

    @Override
    protected void onDestroy() {
        mActivityFinish = true;
        if (null != mPresenter)
            mPresenter.detachView();
        mUnBinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isPortrait = false;
            setLandscape();
        } else {
            isPortrait = true;
            setPortrait();
        }
    }

    protected abstract void setLandscape();

    protected abstract void setPortrait();
}

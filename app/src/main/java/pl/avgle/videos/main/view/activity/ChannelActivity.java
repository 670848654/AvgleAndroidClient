package pl.avgle.videos.main.view.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.wuyr.rippleanimation.RippleAnimation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pl.avgle.videos.R;
import pl.avgle.videos.adapter.ChannelAdapter;
import pl.avgle.videos.bean.ChannelBean;
import pl.avgle.videos.bean.EventState;
import pl.avgle.videos.config.QueryType;
import pl.avgle.videos.database.DatabaseUtil;
import pl.avgle.videos.main.base.BaseActivity;
import pl.avgle.videos.main.contract.ChannelContract;
import pl.avgle.videos.main.presenter.ChannelPresenter;
import pl.avgle.videos.util.SharedPreferencesUtils;
import pl.avgle.videos.util.StatusBarUtil;
import pl.avgle.videos.util.Utils;

public class ChannelActivity extends BaseActivity<ChannelContract.View, ChannelPresenter> implements NavigationView.OnNavigationItemSelectedListener, ChannelContract.View {
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    private ChannelAdapter mChannelAdapter;
    @BindView(R.id.mSwipe)
    SwipeRefreshLayout mSwipe;
    private List<ChannelBean.ResponseBean.CategoriesBean> list = new ArrayList<>();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private SearchView mSearchView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    private long exitTime = 0;
    private int[][] states = new int[][]{
            new int[]{-android.R.attr.state_checked},
            new int[]{android.R.attr.state_checked}
    };

    int position = 0;
    private GridLayoutManager gridLayoutManager;

    private TextView themeView;
    private boolean isChangingTheme = false;

    @Override
    protected void initBeforeView() {}

    @Override
    protected int getLayout() {
        return R.layout.activity_channel;
    }

    @Override
    protected ChannelPresenter createPresenter() {
        return new ChannelPresenter(this);
    }

    @Override
    protected void loadData() {
        mPresenter.loadData();
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        initToolbar();
        initSwipe();
        initDrawer();
        initAdapter();
    }

    public void initToolbar() {
        toolbar.setTitle(Utils.getString(R.string.channel_title));
        setSupportActionBar(toolbar);
    }

    public void initSwipe() {
        mSwipe.setColorSchemeResources(R.color.colorAccent, R.color.blue500, R.color.purple500);
        mSwipe.setOnRefreshListener(() -> {
            list.clear();
            mChannelAdapter.notifyDataSetChanged();
            loadData();
        });
    }

    public void initDrawer() {
        StatusBarUtil.setColorForDrawerLayout(this, drawer, getResources().getColor(R.color.colorPrimary), 0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        });
        toggle.syncState();

        int[] colors = new int[]{getResources().getColor(R.color.text_color_primary),
                getResources().getColor(R.color.pornhub)
        };
        ColorStateList csl = new ColorStateList(states, colors);
        View view = navigationView.getHeaderView(0);
        themeView = view.findViewById(R.id.theme);
        if (isDarkTheme) themeView.setText(Utils.getString(R.string.set_light_theme));
        else themeView.setText(Utils.getString(R.string.set_dark_theme));
        themeView.setOnClickListener(v -> {
            if (Utils.isFastClick())
                setTheme(isDarkTheme);
        });
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setTheme(boolean isDark) {
        isChangingTheme = true;
        if (isDark) {
            isDarkTheme = false;
            themeView.setText(Utils.getString(R.string.set_dark_theme));
            SharedPreferencesUtils.setParam(getApplicationContext(),"darkTheme",false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            isDarkTheme = true;
            themeView.setText(Utils.getString(R.string.set_light_theme));
            SharedPreferencesUtils.setParam(getApplicationContext(),"darkTheme",true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    @Override
    public void showLoadingView() {
        runOnUiThread(() -> {
            if (!mActivityFinish) {
                mSwipe.setRefreshing(true);
                showEmptyVIew();
            }
        });
    }

    @Override
    public void showLoadSuccessView(ChannelBean bean) {
        runOnUiThread(() -> {
            if (!mActivityFinish) {
                mSwipe.setRefreshing(false);
                for (int i = 0; i < bean.getResponse().getCategories().size(); i++) {
                    list.add(bean.getResponse().getCategories().get(i));
                }
                if (isPortrait) setPortrait();
                else setLandscape();
                mChannelAdapter.setNewData(this.list);
            }
        });
    }

    @Override
    public void showLoadUserSuccessView(List<ChannelBean.ResponseBean.CategoriesBean> list) {}

    @Override
    public void showLoadErrorView(String msg) {
        runOnUiThread(() -> {
            if (!mActivityFinish) {
                showErrorView(msg);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showEmptyVIew() {
        runOnUiThread(() -> {
            if (!mActivityFinish) mChannelAdapter.setEmptyView(emptyView);
        });
    }

    public void showErrorView(String text) {
        mSwipe.setRefreshing(false);
        errorTitle.setText(text);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mChannelAdapter.setEmptyView(errorView);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if ((System.currentTimeMillis() - exitTime) > 2000) {
            application.showToastMsg(Utils.getString(R.string.exit_app));
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.channel_menu, menu);
        final MenuItem item = menu.findItem(R.id.search);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setQueryHint(Utils.getString(R.string.search_text));
        mSearchView.setMaxWidth(2000);
        mSearchView.findViewById(androidx.appcompat.R.id.search_plate).setBackground(null);
        mSearchView.findViewById(androidx.appcompat.R.id.submit_area).setBackground(null);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    mSearchView.onActionViewCollapsed();
                    Utils.hideKeyboard(mSearchView);
                    mSearchView.clearFocus();
                    Intent intent = new Intent(ChannelActivity.this, VideosActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", QueryType.QUERY_TYPE);
                    bundle.putString("name", query);
                    bundle.putString("order", (String) SharedPreferencesUtils.getParam(ChannelActivity.this, "videos_order", "mr"));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        int id = item.getItemId();
        if (Utils.isFastClick())
            switch (id) {
                case R.id.nav_tags:
                    startActivity(new Intent(this, TagsActivity.class));
                    break;
                case R.id.nav_new:
                    Utils.showHomeTimeDialog(this, QueryType.NEW_TYPE, "mr", "Most Recent Videos", bundle);
                    break;
                case R.id.nav_hot:
                    Utils.showHomeTimeDialog(this, QueryType.HOT_TYPE, "mv", "Hot Videos", bundle);
                    break;
                case R.id.nav_featured:
                    Utils.showHomeTimeDialog(this, QueryType.FEATURED_TYPE, "tr", "Featured Videos", bundle);
                    break;
                case R.id.nav_favorite:
                    startActivity(new Intent(this, FavoriteActivity.class));
                    break;
                case R.id.nav_about:
                    startActivity(new Intent(this, AboutActivity.class));
                    break;
            }
        bundle.clear();
        return true;
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        if (Utils.checkHasNavigationBar(this))
            mRecyclerView.setPadding(0,0,0, Utils.getNavigationBarHeight(this));
        mRecyclerView.setHasFixedSize(true);
        mChannelAdapter = new ChannelAdapter(list);
        mChannelAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ChannelBean.ResponseBean.CategoriesBean bean = (ChannelBean.ResponseBean.CategoriesBean) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.channel_card_view:
                    Intent intent = new Intent(ChannelActivity.this, VideosActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", QueryType.CHANNEL_TYPE);
                    bundle.putInt("cid", Integer.parseInt(bean.getCHID()));
                    bundle.putString("img", bean.getCover_url());
                    bundle.putString("name", bean.getShortname());
                    bundle.putString("order", (String) SharedPreferencesUtils.getParam(ChannelActivity.this, "videos_order", "mr"));
                    intent.putExtras(bundle);
                    if (isPortrait)
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ChannelActivity.this, view, "sharedImg").toBundle());
                    else
                        startActivity(intent);
                    break;
            }
        });
        /*
        mChannelAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            ImageView favoriteView = view.findViewById(R.id.favorite_view);
            ChannelBean.ResponseBean.CategoriesBean bean = (ChannelBean.ResponseBean.CategoriesBean) adapter.getItem(position);
            mBottomSheetDialogTitle.setText(bean.getName());
            selectBeanList = new ArrayList<>();
            if (bean.isFavorite())
                selectBeanList.add(new SelectBean(Utils.getString(R.string.remove_favorite), R.drawable.baseline_remove_white_48dp));
            else
                selectBeanList.add(new SelectBean(Utils.getString(R.string.add_favorite), R.drawable.baseline_add_white_48dp));
            selectAdapter.setNewData(selectBeanList);
            selectAdapter.setOnItemClickListener((selectAdapter, selectView, selectPosition) -> {
                if (bean.isFavorite()) {
                    bean.setFavorite(false);
                    removeCollection(favoriteView, bean);
                } else {
                    bean.setFavorite(true);
                    collectionChannel(favoriteView, bean);
                }
                mBottomSheetDialog.dismiss();
            });
            mBottomSheetDialog.show();
            return true;
        });
         */
        mRecyclerView.setAdapter(mChannelAdapter);
    }

    /*
     * 收藏频道
     *
     * @param bean
     */
    /*
    public void collectionChannel(ImageView favoriteView, ChannelBean.ResponseBean.CategoriesBean bean) {
        if (DatabaseUtil.checkChannel(bean.getCHID()))
            application.showToastMsg(Utils.getString(R.string.channel_is_exist));
        else {
            DatabaseUtil.addChannel(bean);
            application.showToastMsg(bean.getName() + "\n" + Utils.getString(R.string.favorite_success));
            favoriteView.setVisibility(View.VISIBLE);
        }
    }
     */

    /*
     * 移除收藏
     * @param favoriteView
     * @param bean
     */
    /*
    private void removeCollection(ImageView favoriteView, ChannelBean.ResponseBean.CategoriesBean bean) {
        DatabaseUtil.deleteChannel(bean.getCHID());
        application.showToastMsg(bean.getName() + "\n" + Utils.getString(R.string.remove_favorite_success));
        favoriteView.setVisibility(View.GONE);
    }
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseUtil.closeDB();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void setLandscape() {
        if (list.size() > 0) {
            if (gridLayoutManager != null)
                position = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            gridLayoutManager = new GridLayoutManager(this, 4);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.getLayoutManager().scrollToPosition(position);
        }
    }

    @Override
    protected void setPortrait() {
        if (list.size() > 0) {
            if (gridLayoutManager != null)
                position = ((GridLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            gridLayoutManager = new GridLayoutManager(this, Utils.isTabletDevice(this) ? 3 : 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.getLayoutManager().scrollToPosition(position);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventState eventState) {
        /*
        if (eventState.getState() == 0 && list.size() > 0) {
            mChannelAdapter.setNewData(new ArrayList<>());
            loadData();
        }
         */
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int[] lightColors = new int[]{getResources().getColor(R.color.light_navigation_text_color),
                getResources().getColor(R.color.light_navigation_tini_color)
        };
        int[] darkColors = new int[]{getResources().getColor(R.color.dark_navigation_text_color),
                getResources().getColor(R.color.dark_navigation_tini_color)
        };
        if (isChangingTheme) {
            RippleAnimation.create(themeView).setDuration(800).start();
            navigationView.setBackgroundColor(isDarkTheme ? getResources().getColor(R.color.dark_navigation_color) : getResources().getColor(R.color.light_navigation_color));
            ColorStateList csl = new ColorStateList(states, isDarkTheme ? darkColors : lightColors);
            navigationView.setItemTextColor(csl);
            navigationView.setItemIconTintList(csl);
            drawer.setBackgroundColor(isDarkTheme ? getResources().getColor(R.color.dark_navigation_color) : getResources().getColor(R.color.light_navigation_color));
            toolbar.setBackgroundColor(isDarkTheme ? getResources().getColor(R.color.dark_toolbar_color) : getResources().getColor(R.color.light_toolbar_color));
            errorView.setBackgroundColor(isDarkTheme ? getResources().getColor(R.color.dark_window_color) : getResources().getColor(R.color.light_window_color));
            errorTitle.setTextColor(isDarkTheme ? getResources().getColor(R.color.dark_navigation_text_color) : getResources().getColor(R.color.light_navigation_text_color));
            emptyView.setBackgroundColor(isDarkTheme ? getResources().getColor(R.color.dark_window_color) : getResources().getColor(R.color.light_window_color));
            StatusBarUtil.setColorForDrawerLayout(this, drawer, isDarkTheme ? getResources().getColor(R.color.dark_toolbar_color) : getResources().getColor(R.color.light_toolbar_color), 0);
            isChangingTheme = false;
        }
    }
}

package pl.avgle.videos.main.view.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import cn.jzvd.Jzvd;
import jp.wasabeef.blurry.Blurry;
import pl.avgle.videos.R;
import pl.avgle.videos.adapter.VideosAdapter;
import pl.avgle.videos.bean.EventState;
import pl.avgle.videos.bean.SelectBean;
import pl.avgle.videos.bean.TagsBean;
import pl.avgle.videos.bean.VideoBean;
import pl.avgle.videos.config.QueryType;
import pl.avgle.videos.config.VideosOrderType;
import pl.avgle.videos.custom.CustomLoadMoreView;
import pl.avgle.videos.custom.JZPlayer;
import pl.avgle.videos.database.DatabaseUtil;
import pl.avgle.videos.main.base.BaseActivity;
import pl.avgle.videos.main.contract.VideoContract;
import pl.avgle.videos.main.presenter.VideoPresenter;
import pl.avgle.videos.util.SharedPreferencesUtils;
import pl.avgle.videos.util.StatusBarUtil;
import pl.avgle.videos.util.Utils;

public class VideosActivity extends BaseActivity<VideoContract.View, VideoPresenter> implements VideoContract.View, JZPlayer.PlayErrorListener, JZPlayer.CompleteListener {
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    private VideosAdapter mVideosAdapter;
    @BindView(R.id.mSwipe)
    SwipeRefreshLayout mSwipe;
    private List<VideoBean.ResponseBean.VideosBean> list = new ArrayList<>();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //activity传递的参数
    private int type = QueryType.DEFAULT;
    private String toolbarTitle = "";
    private String order = "";
    private int cid = 0;
    private String time = "";
    private String img;
    @BindView(R.id.header_img)
    ImageView headerImg;
    @BindView(R.id.collaps_toolbar_layout)
    CollapsingToolbarLayout st;
    private boolean isLoading;

    private JZPlayer player;
    private RelativeLayout hdView;
    private int index;

    private BottomSheetDialog mOrderDialog;
    private RadioGroup mOrderGroup;

    private TagsBean.ResponseBean.CollectionsBean tagsBean;
    private List<String> userTags;
    private MenuItem orderItem, favoriteItem;
    private boolean tagIsFavorite = false;

    int position = 0;
    private GridLayoutManager gridLayoutManager;
    private AlertDialog alertDialog;
    
    @Override
    public void playError() {
        removePlayer();
        Toast.makeText(this, Utils.getString(R.string.play_preview_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void complete() {
        removePlayer();
    }

    @Override
    protected void initBeforeView() {}

    @Override
    protected int getLayout() {
        return R.layout.activity_videos;
    }

    @Override
    protected VideoPresenter createPresenter() {
        if (type == QueryType.CHANNEL_TYPE)
            return new VideoPresenter(isLoad, type, nowPage, cid, limit, order, this);
        else if (type == QueryType.COLLECTIONS_TYPE || type == QueryType.QUERY_TYPE)
            return new VideoPresenter(isLoad, type, toolbarTitle, nowPage, limit, order, this);
        else if (type == QueryType.NEW_TYPE || type == QueryType.HOT_TYPE || type == QueryType.FEATURED_TYPE)
            return new VideoPresenter(isLoad, type, nowPage, order, time, limit, this);
        else if (type == QueryType.JAVS_TYPE)
            return new VideoPresenter(isLoad, type, toolbarTitle, nowPage, limit, order, this);
        else
            return null;
    }

    @Override
    protected void loadData() {
        mPresenter.loadData();
    }

    @Override
    protected void initData() {
        if (Utils.checkHasNavigationBar(this))
            StatusBarUtil.setTransparentForImageView(this, toolbar);
        else
            StatusBarUtil.setTransparentForImageView(this, null);
        getBundle();
        initToolbar();
        initSwipe();
        initAdapter();
        setHeaderImg();
        initOrderDialog();
    }

    public void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle && !bundle.isEmpty()) {
            toolbarTitle = bundle.getString("name");
            type = bundle.getInt("type");
            cid = bundle.getInt("cid");
            order = bundle.getString("order");
            img = bundle.getString("img");
            time = bundle.getString("time");
            if (bundle.getSerializable("bean") != null)
                tagsBean = (TagsBean.ResponseBean.CollectionsBean) bundle.getSerializable("bean");
        }
    }

    public void initToolbar() {
        st.setTitle(toolbarTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> {
            if (isPortrait) supportFinishAfterTransition();
            else finish();
        });
    }

    public void initSwipe() {
        mSwipe.setColorSchemeResources(R.color.colorAccent, R.color.blue500, R.color.purple500);
        mSwipe.setOnRefreshListener(() -> {
            mVideosAdapter.setNewData(new ArrayList<>());
            list.clear();
            isLoad = false;
            nowPage = 0;
            mPresenter = createPresenter();
            loadData();
        });
    }

    public void initAdapter() {
        if (!isPortrait) appBarLayout.setExpanded(false, false);
        else appBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                Utils.getActivityAppBarLayoutHeight()));
        if (Utils.checkHasNavigationBar(this))
            mRecyclerView.setPadding(0,0,0, Utils.getNavigationBarHeight(this));
        mRecyclerView.setHasFixedSize(true);
        mVideosAdapter = new VideosAdapter(this, list);
        mVideosAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {}

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
//                player = view.findViewById(R.id.player);
//                hdView = view.findViewById(R.id.hd_view);
                if (view.findViewById(R.id.player) == player) detachedFromWindow();
            }
        });
        mVideosAdapter.setOnItemClickListener((adapter, view, position) -> {
            VideoBean.ResponseBean.VideosBean bean = list.get(position);
            mBottomSheetDialogTitle.setText(bean.getTitle());
            selectBeanList = new ArrayList<>();
            selectBeanList.add(new SelectBean(Utils.getString(R.string.preview), R.drawable.baseline_videocam_white_48dp));
            selectBeanList.add(new SelectBean(Utils.getString(R.string.videos), R.drawable.baseline_movie_white_48dp));
            selectBeanList.add(new SelectBean(Utils.getString(R.string.search_keyword), R.drawable.baseline_zoom_in_white_48dp));
            if (bean.isFavorite())
                selectBeanList.add(new SelectBean(Utils.getString(R.string.remove_favorite), R.drawable.baseline_remove_white_48dp));
            else
                selectBeanList.add(new SelectBean(Utils.getString(R.string.add_favorite), R.drawable.baseline_add_white_48dp));
            selectBeanList.add(new SelectBean(Utils.getString(R.string.browser), R.drawable.baseline_open_in_new_white_48dp));
            selectAdapter.setNewData(selectBeanList);
            selectAdapter.setOnItemClickListener((selectAdapter, selectView, selectPosition) -> {
                ImageView favoriteView = view.findViewById(R.id.favorite_view);
                switch (selectPosition) {
                    case 0:
                        index = position;
                        player = view.findViewById(R.id.player);
                        hdView = view.findViewById(R.id.hd_view);
                        player.setListener(this, this);
                        openPlayer(player, bean);
                        break;
                    case 1:
                        startActivity(new Intent(this, WebViewActivity.class).putExtra("url", bean.getEmbedded_url()));
                        break;
                    case 2:
                        Intent intent = new Intent(this, VideosActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", QueryType.QUERY_TYPE);
                        bundle.putString("name", bean.getKeyword());
                        bundle.putString("order", (String) SharedPreferencesUtils.getParam(this, "videos_order", "mr"));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 3:
                        if (bean.isFavorite()) {
                            bean.setFavorite(false);
                            removeVideo(favoriteView, bean);
                        } else {
                            bean.setFavorite(true);
                            collectionChannel(favoriteView, bean);
                        }
                        break;
                    case 4:
                        Utils.openBrowser(VideosActivity.this, bean.getVideo_url());
                        break;
                }
                mBottomSheetDialog.dismiss();
            });
            mBottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
            mBottomSheetDialog.show();
        });
        mVideosAdapter.setLoadMoreView(new CustomLoadMoreView());
        mVideosAdapter.setOnLoadMoreListener(() -> mRecyclerView.postDelayed(() -> {
            mSwipe.setEnabled(false);
            if (!hasMore) {
                mVideosAdapter.loadMoreEnd();
                mSwipe.setEnabled(true);
            } else {
                if (isErr) {
                    mSwipe.setEnabled(false);
                    nowPage++;
                    isLoad = true;
                    mPresenter = createPresenter();
                    loadData();
                } else {
                    isErr = true;
                    mVideosAdapter.loadMoreFail();
                    mSwipe.setEnabled(true);
                }
            }
        }, 500), mRecyclerView);
        mRecyclerView.setAdapter(mVideosAdapter);
    }

    public void setHeaderImg() {
        switch (type) {
            case QueryType.CHANNEL_TYPE:
                ImageLoader.getInstance().displayImage(img, headerImg, getSimpleOptions());
                break;
            case QueryType.COLLECTIONS_TYPE:
                if (!img.equals(""))
                    ImageLoader.getInstance().displayImage(img, headerImg, getSimpleOptions());
                else
                    headerImg.setImageDrawable(isDarkTheme ? getDrawable(R.drawable.dark_header) : getDrawable(R.drawable.light_header));
                break;
            default:
                headerImg.setImageDrawable(isDarkTheme ? getDrawable(R.drawable.dark_header) : getDrawable(R.drawable.light_header));
                break;
        }
    }

    public void initOrderDialog() {
        View orderView = LayoutInflater.from(this).inflate(R.layout.dialog_order, null);
        mOrderGroup = orderView.findViewById(R.id.order_group);
        mOrderGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.bw:
                    order = VideosOrderType.BW;
                    break;
                case R.id.mr:
                    order = VideosOrderType.MR;
                    break;
                case R.id.mv:
                    order = VideosOrderType.MV;
                    break;
                case R.id.tr:
                    order = VideosOrderType.TR;
                    break;
                case R.id.tf:
                    order = VideosOrderType.TF;
                    break;
                case R.id.lg:
                    order = VideosOrderType.LG;
                    break;
            }
            mOrderDialog.dismiss();
            mVideosAdapter.notifyDataSetChanged();
            isLoad = false;
            nowPage = 0;
            mPresenter = createPresenter();
            loadData();
        });
        mOrderDialog = new BottomSheetDialog(this);
        mOrderDialog.setContentView(orderView);
    }

    private DisplayImageOptions getSimpleOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return options;
    }

    @Override
    public void showLoadSuccessView(VideoBean bean, boolean isLoad) {
        isLoading = false;
        runOnUiThread(() -> {
            if (!mActivityFinish) {
                mSwipe.setRefreshing(false);
                if (type == QueryType.COLLECTIONS_TYPE || type == QueryType.QUERY_TYPE) {
                    userTags = DatabaseUtil.queryAllTagTitles();
                    if (userTags.contains(toolbarTitle)) {
                        tagIsFavorite = true;
                        favoriteItem.setIcon(getDrawable(R.drawable.baseline_star_white_48dp));
                        favoriteItem.setTitle(Utils.getString(R.string.remove_favorite));
                    } else {
                        tagIsFavorite = false;
                        favoriteItem.setIcon(getDrawable(R.drawable.baseline_star_border_white_48dp));
                        favoriteItem.setTitle(Utils.getString(R.string.add_favorite));
                    }
                    favoriteItem.setVisible(true);
                }
                if (type == QueryType.CHANNEL_TYPE || type == QueryType.QUERY_TYPE || type == QueryType.JAVS_TYPE) orderItem.setVisible(true);
                hasMore = bean.getResponse().isHas_more();
                setLoadState(true);
                List<VideoBean.ResponseBean.VideosBean> data = new ArrayList<>();
                for (int i = 0; i < bean.getResponse().getVideos().size(); i++) {
                    data.add(bean.getResponse().getVideos().get(i));
                }
                if (!isLoad) {
                    list = data;
                    mVideosAdapter.setNewData(list);
                    if (isPortrait) setPortrait();
                    else setLandscape();
                } else
                    mVideosAdapter.addData(data);
            }
        });
    }

    public void openPlayer(JZPlayer player, VideoBean.ResponseBean.VideosBean bean) {
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        player.setVisibility(View.VISIBLE);
        player.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        hdView.setVisibility(View.GONE);
        hdView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
        player.titleTextView.setVisibility(View.GONE);
        player.bottomProgressBar.setVisibility(View.GONE);
        player.setUp( bean.getPreview_video_url(),  bean.getTitle(), Jzvd.SCREEN_WINDOW_LIST);
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(bean.getPreview_url());
        if (null != bitmap)
            Blurry.with(this)
                    .radius(4)
                    .sampling(2)
                    .async()
                    .from(ImageLoader.getInstance().loadImageSync(bean.getPreview_url()))
                    .into(player.thumbImageView);
        player.startButton.performClick();
        player.startVideo();
    }

    public void detachedFromWindow () {
        if (player != null) removePlayerView();
    }

    public void removePlayer() {
        if (player != null)  removePlayerView();
    }

    public void removePlayerView() {
        player.releaseAllVideos();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        player.setVisibility(View.GONE);
        player.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
        hdView.setVisibility(View.VISIBLE);
        hdView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        player = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.videos_menu, menu);
        orderItem = menu.findItem(R.id.order);
        favoriteItem = menu.findItem(R.id.favorite);
        favoriteItem.setVisible(false);
        if (type == QueryType.DEFAULT || type == QueryType.NEW_TYPE || type == QueryType.HOT_TYPE || type == QueryType.FEATURED_TYPE) orderItem.setVisible(false);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchItem.setOnMenuItemClickListener(item -> {
            if (Utils.isFastClick()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
                View view = LayoutInflater.from(this).inflate(R.layout.dialog_search, null);
                TextInputLayout til = view.findViewById(R.id.text_hint);
                Spinner spinner = view.findViewById(R.id.spinner);
                AtomicBoolean isJav = new AtomicBoolean(false);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                isJav.set(false);
                                til.setHint(getString(R.string.search_tag_text));
                                break;
                            case 1:
                                isJav.set(true);
                                til.setHint(getString(R.string.search_jav_text));
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                EditText et = view.findViewById(R.id.search_text);
                builder.setPositiveButton(Utils.getString(R.string.confirm), null);
                builder.setNegativeButton(Utils.getString(R.string.cancel), null);
                builder.setCancelable(false);
                alertDialog = builder.setView(view).create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                    String text = et.getText().toString();
                    if (!text.trim().isEmpty()) {
                        toolbarTitle = text;
                        st.setTitle(text);
                        type = isJav.get() ? QueryType.JAVS_TYPE : QueryType.QUERY_TYPE;
                        mVideosAdapter.setNewData(new ArrayList<>());
                        tagsBean = null;
                        favoriteItem.setVisible(false);
                        Toast.makeText(this, isJav.get() ? Utils.getString(R.string.search_with_jav_number) :  Utils.getString(R.string.search_with_keyword), Toast.LENGTH_SHORT).show();
                        list.clear();
                        isLoad = false;
                        nowPage = 0;
                        mPresenter = createPresenter();
                        loadData();
                        alertDialog.dismiss();
                    } else {
                        et.setError(Utils.getString(R.string.favorite_tag_dialog_error));
                        return;
                    }
                });
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> alertDialog.dismiss());
            }
            return false;
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.favorite:
                if (tagIsFavorite)
                    removeTag(toolbarTitle);
                else
                    addTag(toolbarTitle);
                break;
            case R.id.order:
                if (isLoading)
                    Toast.makeText(this, Utils.getString(R.string.do_not_operate), Toast.LENGTH_SHORT).show();
                else {
                    mOrderDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
                    mOrderDialog.show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setLoadState(boolean loadState) {
        isErr = loadState;
        mVideosAdapter.loadMoreComplete();
    }

    public void showErrorView(String text) {
        mSwipe.setRefreshing(false);
        if (!isPortrait) mRecyclerView.setNestedScrollingEnabled(false);
        errorTitle.setText(text);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mVideosAdapter.setEmptyView(errorView);
    }

    /**
     * 收藏视频
     *
     * @param bean
     */
    public void collectionChannel(ImageView favoriteView, VideoBean.ResponseBean.VideosBean bean) {
        DatabaseUtil.addVideo(bean);
        application.showToastMsg(bean.getVid() + "\n" + Utils.getString(R.string.favorite_success));
        favoriteView.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new EventState(2, isPortrait));
    }

    /**
     * 移除收藏
     */
    private void removeVideo(ImageView favoriteView, VideoBean.ResponseBean.VideosBean bean) {
        DatabaseUtil.deleteVideo(bean.getVid());
        application.showToastMsg(bean.getVid() + "\n" + Utils.getString(R.string.remove_favorite_success));
        favoriteView.setVisibility(View.GONE);
        EventBus.getDefault().post(new EventState(2, isPortrait));
    }

    @Override
    public void showUserFavoriteView(List<VideoBean.ResponseBean.VideosBean> list) {

    }

    @Override
    public void showLoadingView() {
        isLoading = true;
        runOnUiThread(() -> {
            if (!mActivityFinish && !isLoad) {
                mSwipe.setRefreshing(true);
                showEmptyVIew();
            }
        });

    }

    @Override
    public void showLoadErrorView(String msg) {
        isLoading = false;
        runOnUiThread(() -> {
            if (!mActivityFinish) {
                setLoadState(false);
                if (!isLoad)
                    showErrorView(msg);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void showEmptyVIew() {
        runOnUiThread(() -> {
            if (!mActivityFinish) mVideosAdapter.setEmptyView(emptyView);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.releaseAllVideos();
            finish();
        }
    }

    @Override
    protected void setLandscape() {
        setAppBarLayout(false);
        if (list.size() > 0) {
            setGridSpaceItemDecoration(mRecyclerView, 4);
            if (gridLayoutManager != null)
                position = ((GridLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            appBarLayout.setExpanded(false, false);
            gridLayoutManager = new GridLayoutManager(this, 4);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.getLayoutManager().scrollToPosition(position);
        }
    }

    @Override
    protected void setPortrait() {
        setAppBarLayout(true);
        if (list.size() > 0) {
            setGridSpaceItemDecoration(mRecyclerView,Utils.isTabletDevice(this) ? 2 : 1);
            if (gridLayoutManager != null)
                position = ((GridLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            gridLayoutManager = new GridLayoutManager(this, Utils.isTabletDevice(this) ? 2 : 1);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.getLayoutManager().scrollToPosition(position);
            if (position > 3) appBarLayout.setExpanded(false, false);
        }
    }

    private void setAppBarLayout(boolean nestedScrollingEnabled) {
        appBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                Utils.getActivityAppBarLayoutHeight()));
        mRecyclerView.setNestedScrollingEnabled(nestedScrollingEnabled);
        if (!isPortrait) appBarLayout.setExpanded(false, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null)
            player.releaseAllVideos();
    }

    /**
     * 移除收藏
     */
    private void removeTag(String title) {
        DatabaseUtil.deleteTag(title);
        tagIsFavorite = false;
        application.showToastMsg(title + "\n" + Utils.getString(R.string.remove_favorite_success));
        favoriteItem.setIcon(getDrawable(R.drawable.baseline_star_border_white_48dp));
        favoriteItem.setTitle(Utils.getString(R.string.add_favorite));
        EventBus.getDefault().post(new EventState(1, isPortrait));
    }

    /**
     * 自定义标签
     */
    public void addTag(String title) {
        if (tagsBean != null) {
            DatabaseUtil.addTags(tagsBean);
        } else {
            TagsBean.ResponseBean.CollectionsBean bean = new TagsBean.ResponseBean.CollectionsBean();
            bean.setId(UUID.randomUUID().toString());
            bean.setTitle(title);
            bean.setKeyword(Utils.getString(R.string.favorite_tag_dialog_title));
            bean.setCover_url("");
            bean.setTotal_views(0);
            bean.setVideo_count(0);
            bean.setCollection_url("");
            DatabaseUtil.addTags(bean);
        }
        tagIsFavorite = true;
        application.showToastMsg(title + "\n" + Utils.getString(R.string.favorite_success));
        favoriteItem.setIcon(getDrawable(R.drawable.baseline_star_white_48dp));
        favoriteItem.setTitle(Utils.getString(R.string.remove_favorite));
        EventBus.getDefault().post(new EventState(1, isPortrait));
    }
}

package pl.avgle.videos.main.view.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.Jzvd;
import jp.wasabeef.blurry.Blurry;
import pl.avgle.videos.R;
import pl.avgle.videos.adapter.VideosAdapter;
import pl.avgle.videos.bean.ChangeState;
import pl.avgle.videos.bean.EventState;
import pl.avgle.videos.bean.SelectBean;
import pl.avgle.videos.bean.VideoBean;
import pl.avgle.videos.config.QueryType;
import pl.avgle.videos.custom.CustomLoadMoreView;
import pl.avgle.videos.custom.JZPlayer;
import pl.avgle.videos.database.DatabaseUtil;
import pl.avgle.videos.main.base.LazyFragment;
import pl.avgle.videos.main.contract.VideoContract;
import pl.avgle.videos.main.presenter.VideoPresenter;
import pl.avgle.videos.main.view.activity.VideosActivity;
import pl.avgle.videos.main.view.activity.WebViewActivity;
import pl.avgle.videos.util.SharedPreferencesUtils;
import pl.avgle.videos.util.Utils;

public class FavoriteVideosFragment extends LazyFragment<VideoContract.View, VideoPresenter> implements VideoContract.View, JZPlayer.PlayErrorListener, JZPlayer.CompleteListener, MaterialSearchBar.OnSearchActionListener {
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.searchBar)
    MaterialSearchBar searchBar;
    private VideosAdapter mVideosAdapter;
    private  List<VideoBean.ResponseBean.VideosBean> list = new ArrayList<>();
    private View view;
    private JZPlayer player;
    private RelativeLayout hdView;
//    private List<VideoBean.ResponseBean.VideosBean> searchList = new ArrayList<>();
    private boolean isSearch = false;
    private FloatingActionButton fab;

    int position = 0;
    private GridLayoutManager gridLayoutManager;
    private int limit = 50;
    //是否是第一次加载
    protected boolean isLoad = false;
    protected boolean isErr = true;
    private String selection = "";
    private int videosCount;
    private VideoBean.ResponseBean.VideosBean playingBean;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_favorite, container, false);
            mUnBinder = ButterKnife.bind(this, view);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        fab = getActivity().findViewById(R.id.fab);
        if (isFragmentVisible) setFabOnClick();
        if (Utils.checkHasNavigationBar(getActivity()))
            mRecyclerView.setPadding(0,0,0, Utils.getNavigationBarHeight(getActivity()));
        initAdapter();
        initSearchBar();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (fab != null) setFabOnClick();
        }
    }

    private void setFabOnClick() {
        fab.setOnClickListener(view -> {
            if (searchBar.isSearchOpened()) {
                setSearchBarGone();
                onSearchConfirmed(searchBar.getText());
            } else {
                searchBar.openSearch();
                searchBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected VideoPresenter createPresenter() {
        return new VideoPresenter(true, selection, list.size(), limit, this);
    }

    public void initAdapter() {
        if (mVideosAdapter == null) {
            mVideosAdapter = new VideosAdapter(getActivity(), list);
            mVideosAdapter.bindToRecyclerView(mRecyclerView);
            mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
                @Override
                public void onChildViewAttachedToWindow(@NonNull View view) {}

                @Override
                public void onChildViewDetachedFromWindow(@NonNull View view) {
//                    JZPlayer player = view.findViewById(R.id.player);
//                    hdView = view.findViewById(R.id.hd_view);
                    if (view.findViewById(R.id.player) == player) removePlayer();
                }
            });
            mVideosAdapter.setOnItemClickListener((adapter, view, position) -> {
                VideoBean.ResponseBean.VideosBean bean = (VideoBean.ResponseBean.VideosBean) adapter.getItem(position);
                mBottomSheetDialogTitle.setText(list.get(position).getTitle());
                selectBeanList = new ArrayList<>();
                selectBeanList.add(new SelectBean(Utils.getString(R.string.preview), R.drawable.baseline_videocam_white_48dp));
                selectBeanList.add(new SelectBean(Utils.getString(R.string.videos), R.drawable.baseline_movie_white_48dp));
                selectBeanList.add(new SelectBean(Utils.getString(R.string.search_keyword), R.drawable.baseline_zoom_in_white_48dp));
                selectBeanList.add(new SelectBean(Utils.getString(R.string.remove_favorite), R.drawable.baseline_remove_white_48dp));
                selectBeanList.add(new SelectBean(Utils.getString(R.string.browser), R.drawable.baseline_open_in_new_white_48dp));
                selectAdapter.setNewData(selectBeanList);
                selectAdapter.setOnItemClickListener((selectAdapter, selectView, selectPosition) -> {
                    switch (selectPosition) {
                        case 0:
                            removePlayer();
                            player = view.findViewById(R.id.player);
                            player.SAVE_PROGRESS = false;
                            hdView = view.findViewById(R.id.hd_view);
                            player.setListener(this, this);
                            playingBean = bean;
                            openPlayer(playingBean);
                            break;
                        case 1:
                            startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("url", bean.getEmbedded_url()));
                            break;
                        case 2:
                            Intent intent = new Intent(getActivity(), VideosActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", QueryType.QUERY_TYPE);
                            bundle.putString("name", bean.getKeyword());
                            bundle.putString("order", (String) SharedPreferencesUtils.getParam(getActivity(), "videos_order", "mr"));
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                        case 3:
                            removeVideo(position, bean.getVid());
                            break;
                        case 4:
                            Utils.openBrowser(getActivity(), bean.getVideo_url());
                            break;
                    }
                    mBottomSheetDialog.dismiss();
                });
                mBottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
                mBottomSheetDialog.show();
            });
            mVideosAdapter.setLoadMoreView(new CustomLoadMoreView());
            mVideosAdapter.setOnLoadMoreListener(() -> mRecyclerView.postDelayed(() -> {
                if (list.size() >= videosCount) {
                    mVideosAdapter.loadMoreEnd();
                } else {
                    if (isErr) {
                        isLoad = true;
                        mPresenter = createPresenter();
                        loadData();
                    } else {
                        isErr = true;
                        mVideosAdapter.loadMoreFail();
                    }
                }
            }, 500), mRecyclerView);
            mRecyclerView.setAdapter(mVideosAdapter);
        }
    }

    public void setLoadState(boolean loadState) {
        isErr = loadState;
        mVideosAdapter.loadMoreComplete();
    }

    public void openPlayer(VideoBean.ResponseBean.VideosBean bean) {
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        player.setVisibility(View.VISIBLE);
        player.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in));
        if (bean.isHd()) {
            hdView.setVisibility(View.GONE);
            hdView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out));
        }
        player.titleTextView.setVisibility(View.GONE);
        player.bottomProgressBar.setVisibility(View.GONE);
        player.setUp( bean.getPreview_video_url(),  bean.getTitle(), Jzvd.SCREEN_WINDOW_NORMAL);
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(bean.getPreview_url());
        if (null != bitmap)
            Blurry.with(getActivity())
                    .radius(4)
                    .sampling(2)
                    .async()
                    .from(ImageLoader.getInstance().loadImageSync(bean.getPreview_url()))
                    .into(player.thumbImageView);
        player.startButton.performClick();
        player.startVideo();
    }

    public void removePlayer() {
        if (player != null)  removePlayerView();
    }

    public void removePlayerView() {
        player.releaseAllVideos();
        player.setVisibility(View.GONE);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        player.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out));
        if (playingBean.isHd()) {
            hdView.setVisibility(View.VISIBLE);
            hdView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in));
        }
        player = null;
        playingBean = null;
    }

    /**
     * 移除收藏
     */
    private void removeVideo(int position, String vid) {
        DatabaseUtil.deleteVideo(vid);
        mVideosAdapter.remove(position);
        if (list.size() == 0 )
            if (isSearch) showLoadErrorView(Utils.getString(R.string.favorite_video_empty_error));
            else showLoadErrorView(Utils.getString(R.string.empty_channel));
    }

    public void initSearchBar() {
        searchBar.setOnSearchActionListener(this);
        searchBar.findViewById(R.id.mt_arrow).setOnClickListener(v -> {
            setSearchBarGone();
        });
    }

    @Override
    protected void loadData() {
        videosCount = DatabaseUtil.queryVideosCount(selection);
        mPresenter.loadData();
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventState eventState) {
        isPortrait = eventState.isPortrait();
        if (eventState.getState() == 2 && list.size() > 0) {
            if (!isSearch) loadData();
        }
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeState(ChangeState changeState) {
        if (changeState.isPortrait()) setPortrait();
        else setLandscape();
    }

    protected void setLandscape() {
        setLandscapeRecyclerView();
    }

    private void setLandscapeRecyclerView() {
        if (list.size() > 0) {
            setGridSpaceItemDecoration(mRecyclerView, 4);
            if (gridLayoutManager != null)
                position = ((GridLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            gridLayoutManager = new GridLayoutManager(getActivity(), 4);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.getLayoutManager().scrollToPosition(position);
        }
    }

    protected void setPortrait() {
        setPortraitRecyclerView();
    }

    private void setPortraitRecyclerView() {
        if (list.size() > 0) {
            setGridSpaceItemDecoration(mRecyclerView, Utils.isTabletDevice(getActivity()) ? 2 : 1);
            if (gridLayoutManager != null)
                position = ((GridLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            gridLayoutManager = new GridLayoutManager(getActivity(), Utils.isTabletDevice(getActivity()) ? 2 : 1);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.getLayoutManager().scrollToPosition(position);
        }
    }

    @Override
    public void playError() {
        removePlayer();
        Toast.makeText(getActivity(), Utils.getString(R.string.play_preview_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void complete() {
        removePlayer();
    }

    @Override
    public void showLoadSuccessView(VideoBean bean, boolean isLoad) {}

    @Override
    public void showUserFavoriteView(List<VideoBean.ResponseBean.VideosBean> videosBeanList) {
        loading.setVisibility(View.GONE);
        setLoadState(true);
        if (!isLoad) {
            list = videosBeanList;
            mVideosAdapter.setNewData(list);
            setRecyclerView();
        } else
            mVideosAdapter.addData(videosBeanList);
    }

    @Override
    public void showLoadingView() {}

    @Override
    public void showLoadErrorView(String msg) {
        loading.setVisibility(View.GONE);
        setLoadState(false);
        if (isSearch) errorTitle.setText(Utils.getString(R.string.favorite_video_empty_error));
        else errorTitle.setText(msg);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mVideosAdapter.setEmptyView(errorView);
    }

    @Override
    public void showEmptyVIew() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null)
            player.releaseAllVideos();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null)
            player.releaseAllVideos();
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {}

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Utils.hideKeyboard(searchBar);
        setSearchBarGone();
        list.clear();
        if (text.toString().isEmpty()) {
            isSearch = false;
            selection = "";
        } else {
            isSearch = true;
            selection = text.toString();
        }
        mPresenter = createPresenter();
        loadData();
    }

    @Override
    public void onButtonClicked(int buttonCode) {}

    public void setSearchBarGone() {
        searchBar.closeSearch();
        searchBar.setVisibility(View.GONE);
    }

    private void setRecyclerView() {
        if (isPortrait) setPortrait();
        else setLandscape();
    }
}

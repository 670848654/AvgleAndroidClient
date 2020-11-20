package pl.avgle.videos.main.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.Jzvd;
import jp.wasabeef.blurry.Blurry;
import pl.avgle.videos.R;
import pl.avgle.videos.adapter.VideosAdapter;
import pl.avgle.videos.bean.SelectBean;
import pl.avgle.videos.bean.VideoBean;
import pl.avgle.videos.config.QueryType;
import pl.avgle.videos.custom.JZPlayer;
import pl.avgle.videos.database.DatabaseUtil;
import pl.avgle.videos.main.base.LazyFragment;
import pl.avgle.videos.main.contract.VideoContract;
import pl.avgle.videos.main.presenter.VideoPresenter;
import pl.avgle.videos.util.SharedPreferencesUtils;
import pl.avgle.videos.util.Utils;

public class FavoriteVideosFragment extends LazyFragment<VideoContract.View, VideoPresenter> implements VideoContract.View, JZPlayer.CompleteListener,MaterialSearchBar.OnSearchActionListener {
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
    private int index;
    private List<VideoBean.ResponseBean.VideosBean> searchList = new ArrayList<>();
    private boolean isSearch = false;
    private FloatingActionButton fab;

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
        return new VideoPresenter(true, this);
    }

    public void initAdapter() {
        if (mVideosAdapter == null) {
            mVideosAdapter = new VideosAdapter(list);
            mVideosAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
            mVideosAdapter.bindToRecyclerView(mRecyclerView);
            mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
                @Override
                public void onChildViewAttachedToWindow(@NonNull View view) {

                }

                @Override
                public void onChildViewDetachedFromWindow(@NonNull View view) {
                    JZPlayer player = view.findViewById(R.id.player);
                    hdView = view.findViewById(R.id.hd_view);
                    detachedFromWindow(player);
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
                            removePlayer(player);
                            index = position;
                            player = view.findViewById(R.id.player);
                            hdView = view.findViewById(R.id.hd_view);
                            player.setListener(this);
                            openPlayer(player, bean);
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
                mBottomSheetDialog.show();
            });
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(mVideosAdapter);
        }
    }

    public void openPlayer(JZPlayer player, VideoBean.ResponseBean.VideosBean bean) {
        player.setVisibility(View.VISIBLE);
        player.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in));
        hdView.setVisibility(View.GONE);
        hdView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out));
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

    public void detachedFromWindow (JZPlayer player) {
        if (player != null) {
            if (player == mVideosAdapter.getViewByPosition(index, R.id.player)) {
                removePlayerView(player);
            }
        }
    }

    public void removePlayer(JZPlayer player) {
        if (player != null) {
            removePlayerView(player);
        }
    }

    public void removePlayerView(JZPlayer player) {
        player.releaseAllVideos();
        player.setVisibility(View.GONE);
        player.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out));
        hdView.setVisibility(View.VISIBLE);
        hdView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in));
    }

    /**
     * 移除收藏
     */
    private void removeVideo(int position, String vid) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getVid().equals(vid)) {
                if (isSearch)
                    list.remove(i);
                mVideosAdapter.remove(position);
                break;
            }
        }
        DatabaseUtil.deleteVideo(vid);
        if (list.size() == 0 && searchList.size() == 0) {
            mVideosAdapter.setNewData(list);
            showLoadErrorView("收藏为空");
        } else if (searchList.size() == 0) {
            errorTitle.setText(Utils.getString(R.string.favorite_video_empty_error));
            mVideosAdapter.setEmptyView(errorView);
        }
    }

    public void initSearchBar() {
        searchBar.setOnSearchActionListener(this);
        searchBar.findViewById(R.id.mt_arrow).setOnClickListener(v -> {
            setSearchBarGone();
        });
    }

    @Override
    protected void loadData() {
        mPresenter.loadData();
    }

    @Override
    public void complete() {
        removePlayer(player);
    }

    @Override
    public void showLoadSuccessView(VideoBean bean, boolean isLoad) {

    }

    @Override
    public void showUserFavoriteView(List<VideoBean.ResponseBean.VideosBean> list) {
        loading.setVisibility(View.GONE);
        this.list = list;
        mVideosAdapter.setNewData(this.list);
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void showLoadErrorView(String msg) {
        loading.setVisibility(View.GONE);
        errorTitle.setText(msg);
        mVideosAdapter.setEmptyView(errorView);
    }

    @Override
    public void showEmptyVIew() {

    }

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
        searchList.clear();
        if (text.toString().isEmpty()) {
            isSearch = false;
            mVideosAdapter.setNewData(list);
        } else {
            isSearch = true;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getKeyword().toLowerCase().contains(text.toString().toLowerCase()) || list.get(i).getTitle().toLowerCase().contains(text.toString().toLowerCase()))
                    searchList.add(list.get(i));
            }
            mVideosAdapter.setNewData(searchList);
            if (searchList.size() == 0) {
                errorTitle.setText(Utils.getString(R.string.favorite_video_empty_error));
                mVideosAdapter.setEmptyView(errorView);
            }
        }
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onButtonClicked(int buttonCode) {}

    public void setSearchBarGone() {
        searchBar.closeSearch();
        searchBar.setVisibility(View.GONE);
    }
}

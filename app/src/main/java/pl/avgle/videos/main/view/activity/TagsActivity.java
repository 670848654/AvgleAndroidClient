package pl.avgle.videos.main.view.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pl.avgle.videos.R;
import pl.avgle.videos.adapter.TagsAdapter;
import pl.avgle.videos.bean.EventState;
import pl.avgle.videos.bean.SelectBean;
import pl.avgle.videos.bean.TagsBean;
import pl.avgle.videos.config.QueryType;
import pl.avgle.videos.custom.CustomLoadMoreView;
import pl.avgle.videos.database.DatabaseUtil;
import pl.avgle.videos.main.base.BaseActivity;
import pl.avgle.videos.main.contract.TagsContract;
import pl.avgle.videos.main.presenter.TagsPresenter;
import pl.avgle.videos.util.SharedPreferencesUtils;
import pl.avgle.videos.util.Utils;

public class TagsActivity extends BaseActivity<TagsContract.View, TagsPresenter> implements TagsContract.View {
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    private TagsAdapter mTagsAdapter;
    @BindView(R.id.mSwipe)
    SwipeRefreshLayout mSwipe;
    private List<TagsBean.ResponseBean.CollectionsBean> list = new ArrayList<>();
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    int position = 0;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void initBeforeView() {}

    @Override
    protected int getLayout() {
        return R.layout.activity_collections;
    }

    @Override
    protected TagsPresenter createPresenter() {
        return new TagsPresenter(isLoad, nowPage, limit, this);
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
        initAdapter();
    }

    public void initToolbar() {
        toolbar.setTitle(Utils.getString(R.string.tag_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    public void initSwipe() {
        mSwipe.setColorSchemeResources(R.color.colorAccent, R.color.blue500, R.color.purple500);
        mSwipe.setOnRefreshListener(() -> {
            list.clear();
            mTagsAdapter.notifyDataSetChanged();
            isLoad = false;
            nowPage = 0;
            mPresenter = createPresenter();
            loadData();
        });
    }

    public void initAdapter() {
        if (Utils.checkHasNavigationBar(this))
            mRecyclerView.setPadding(0,0,0, Utils.getNavigationBarHeight(this));
        mRecyclerView.setHasFixedSize(true);
        mTagsAdapter = new TagsAdapter(this, list);
        mTagsAdapter.setOnItemClickListener((adapter, view, position) -> {
            TagsBean.ResponseBean.CollectionsBean bean = (TagsBean.ResponseBean.CollectionsBean) adapter.getItem(position);
            Intent intent = new Intent(TagsActivity.this, VideosActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("bean", bean);
            bundle.putInt("type", QueryType.COLLECTIONS_TYPE);
            bundle.putString("name", bean.getTitle());
            bundle.putString("img", bean.getCover_url());
            bundle.putString("order", (String) SharedPreferencesUtils.getParam(TagsActivity.this, "videos_order", "mr"));
            intent.putExtras(bundle);
            if (isPortrait)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(TagsActivity.this, view, "sharedImg").toBundle());
            else
                startActivity(intent);
        });
        mTagsAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            ImageView favoriteView = view.findViewById(R.id.favorite_view);
            TagsBean.ResponseBean.CollectionsBean bean = (TagsBean.ResponseBean.CollectionsBean) adapter.getItem(position);
            mBottomSheetDialogTitle.setText(bean.getTitle());
            selectBeanList = new ArrayList<>();
            if (bean.isFavorite())
                selectBeanList.add(new SelectBean(Utils.getString(R.string.remove_favorite), R.drawable.baseline_remove_white_48dp));
            else
                selectBeanList.add(new SelectBean(Utils.getString(R.string.add_favorite), R.drawable.baseline_add_white_48dp));
            selectAdapter.setNewData(selectBeanList);
            selectAdapter.setOnItemClickListener((selectAdapter, selectView, selectPosition) -> {
                if (bean.isFavorite()) {
                    bean.setFavorite(false);
                    removeTag(favoriteView, bean);
                } else {
                    bean.setFavorite(true);
                    collectionTag(favoriteView, bean);
                }
                mBottomSheetDialog.dismiss();
            });
            mBottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
            mBottomSheetDialog.show();
            return true;
        });
        mTagsAdapter.setLoadMoreView(new CustomLoadMoreView());
        mTagsAdapter.setOnLoadMoreListener(() -> mRecyclerView.postDelayed(() -> {
            mSwipe.setEnabled(false);
            if (!hasMore) {
                //数据全部加载完毕
                mTagsAdapter.loadMoreEnd();
                mSwipe.setEnabled(true);
            } else {
                if (isErr) {
                    mSwipe.setEnabled(false);
                    //成功获取更多数据
                    nowPage++;
                    isLoad = true;
                    mPresenter = createPresenter();
                    loadData();
                } else {
                    //获取更多数据失败
                    isErr = true;
                    mTagsAdapter.loadMoreFail();
                    mSwipe.setEnabled(true);
                }
            }
        }, 500), mRecyclerView);
        mRecyclerView.setAdapter(mTagsAdapter);
    }

    /**
     * 收藏标签
     *
     * @param bean
     */
    public void collectionTag(ImageView favoriteView, TagsBean.ResponseBean.CollectionsBean bean) {
        if (DatabaseUtil.checkTag(bean.getTitle()))
            application.showToastMsg(Utils.getString(R.string.tag_is_exist));
        else {
            DatabaseUtil.addTags(bean);
            application.showToastMsg(bean.getTitle() + "\n" + Utils.getString(R.string.favorite_success));
            favoriteView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 移除收藏
     */
    private void removeTag(ImageView favoriteView, TagsBean.ResponseBean.CollectionsBean bean) {
        DatabaseUtil.deleteTag(bean.getTitle());
        application.showToastMsg(bean.getTitle() + "\n" + Utils.getString(R.string.remove_favorite_success));
        favoriteView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadSuccessView(TagsBean bean, boolean isLoad) {
        runOnUiThread(() -> {
            if (!mActivityFinish) {
                mSwipe.setRefreshing(false);
                hasMore = bean.getResponse().isHas_more();
                setLoadState(true);
                List<TagsBean.ResponseBean.CollectionsBean> data = new ArrayList<>();
                for (int i = 0; i < bean.getResponse().getCollections().size(); i++) {
                    data.add(bean.getResponse().getCollections().get(i));
                }
                if (!isLoad) {
                    list = data;
                    mTagsAdapter.setNewData(list);
                    if (isPortrait) setPortrait();
                    else setLandscape();
                } else
                    mTagsAdapter.addData(data);
            }
        });
    }

    @Override
    public void showUserFavoriteView(List<TagsBean.ResponseBean.CollectionsBean> list) {}

    @Override
    public void showLoadingView() {
        runOnUiThread(() -> {
            if (!mActivityFinish && !isLoad) {
                mSwipe.setRefreshing(true);
                showEmptyVIew();
            }
        });
    }

    @Override
    public void showLoadErrorView(String msg) {
        runOnUiThread(() -> {
            if (!mActivityFinish) {
                setLoadState(false);
                if (!isLoad)
                    showErrorView(msg);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setLoadState(boolean loadState) {
        isErr = loadState;
        mTagsAdapter.loadMoreComplete();
    }

    @Override
    public void showEmptyVIew() {
        mTagsAdapter.setEmptyView(emptyView);
    }

    public void showErrorView(String text) {
        mSwipe.setRefreshing(false);
        errorTitle.setText(text);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mTagsAdapter.setEmptyView(errorView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ref(EventState eventState) {
        if (eventState.getState() == 1) {
            mTagsAdapter.setNewData(new ArrayList<>());
            loadData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void setLandscape() {
        if (list.size() > 0) {
            if (gridLayoutManager != null)
                position = ((GridLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            gridLayoutManager = new GridLayoutManager(this, 4);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.getLayoutManager().scrollToPosition(position);
            setGridSpaceItemDecoration(mRecyclerView,4);
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
            setGridSpaceItemDecoration(mRecyclerView, Utils.isTabletDevice(this) ? 3 : 2);
        }
    }
}

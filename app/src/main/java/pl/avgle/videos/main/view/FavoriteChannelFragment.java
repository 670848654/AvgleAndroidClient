package pl.avgle.videos.main.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.avgle.videos.R;
import pl.avgle.videos.adapter.ChannelAdapter;
import pl.avgle.videos.bean.ChannelBean;
import pl.avgle.videos.bean.SelectBean;
import pl.avgle.videos.config.QueryType;
import pl.avgle.videos.database.DatabaseUtil;
import pl.avgle.videos.main.base.LazyFragment;
import pl.avgle.videos.main.contract.ChannelContract;
import pl.avgle.videos.main.presenter.ChannelPresenter;
import pl.avgle.videos.util.SharedPreferencesUtils;
import pl.avgle.videos.util.Utils;

public class FavoriteChannelFragment extends LazyFragment<ChannelContract.View, ChannelPresenter> implements ChannelContract.View  {
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading)
    ProgressBar loading;
    private ChannelAdapter mChannelAdapter;
    private List<ChannelBean.ResponseBean.CategoriesBean> list = new ArrayList<>();
    private View view;

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
        if (Utils.checkHasNavigationBar(getActivity())) mRecyclerView.setPadding(0,0,0, Utils.getNavigationBarHeight(getActivity()));
        initAdapter();
        return view;
    }

    @Override
    protected ChannelPresenter createPresenter() {
        return new ChannelPresenter(true, this);
    }

    public void initAdapter() {
        if (mChannelAdapter == null) {
            mChannelAdapter = new ChannelAdapter(list);
            mChannelAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
            mChannelAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                ChannelBean.ResponseBean.CategoriesBean bean = (ChannelBean.ResponseBean.CategoriesBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.channel_card_view:
                        Intent intent = new Intent(getActivity(), VideosActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", QueryType.CHANNEL_TYPE);
                        bundle.putInt("cid", Integer.parseInt(bean.getCHID()));
                        bundle.putString("img", bean.getCover_url());
                        bundle.putString("name", bean.getShortname());
                        bundle.putString("order", (String) SharedPreferencesUtils.getParam(getActivity(), "videos_order", "mr"));
                        intent.putExtras(bundle);
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "sharedImg").toBundle());
                        break;
                }
            });
            /**
             * 移除收藏
             */
            mChannelAdapter.setOnItemLongClickListener((adapter, view, position) -> {
                mBottomSheetDialogTitle.setText(list.get(position).getName());
                selectBeanList = new ArrayList<>();
                selectBeanList.add(new SelectBean(Utils.getString(R.string.remove_favorite), R.drawable.baseline_remove_white_48dp));
                selectAdapter.setNewData(selectBeanList);
                selectAdapter.setOnItemClickListener((selectAdapter, selectView, selectPosition) -> {
                    removeCollection(position, list.get(position).getCHID());
                    mBottomSheetDialog.dismiss();
                });
                mBottomSheetDialog.show();
                return true;
            });
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            mRecyclerView.setAdapter(mChannelAdapter);
        }
    }

    /**
     * 移除收藏
     */
    private void removeCollection(int position, String chid) {
        DatabaseUtil.deleteChannel(chid);
        mChannelAdapter.remove(position);
        if (list.size() == 0) {
            mChannelAdapter.setNewData(list);
            showLoadErrorView("收藏为空");
        }
    }

    @Override
    protected void loadData() {
        mPresenter.loadData();
    }

    @Override
    public void showLoadSuccessView(ChannelBean bean) {

    }

    @Override
    public void showLoadUserSuccessView(List<ChannelBean.ResponseBean.CategoriesBean> list) {
        loading.setVisibility(View.GONE);
        this.list = list;
        mChannelAdapter.setNewData(list);
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void showLoadErrorView(String msg) {
        loading.setVisibility(View.GONE);
        errorTitle.setText(msg);
        mChannelAdapter.setEmptyView(errorView);
    }

    @Override
    public void showEmptyVIew() {

    }
}

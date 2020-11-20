package pl.avgle.videos.main.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.avgle.videos.R;
import pl.avgle.videos.adapter.TagsAdapter;
import pl.avgle.videos.bean.SelectBean;
import pl.avgle.videos.bean.TagsBean;
import pl.avgle.videos.config.QueryType;
import pl.avgle.videos.database.DatabaseUtil;
import pl.avgle.videos.main.base.LazyFragment;
import pl.avgle.videos.main.contract.TagsContract;
import pl.avgle.videos.main.presenter.TagsPresenter;
import pl.avgle.videos.util.SharedPreferencesUtils;
import pl.avgle.videos.util.Utils;

public class FavoriteTagsFragment extends LazyFragment<TagsContract.View, TagsPresenter> implements TagsContract.View, View.OnClickListener {
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading)
    ProgressBar loading;
    private TagsAdapter mTagsAdapter;
    private List<TagsBean.ResponseBean.CollectionsBean> list = new ArrayList<>();
    private View view;
    private AlertDialog alertDialog;
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
        fab.setOnClickListener(view -> addTags());
    }

    @Override
    protected TagsPresenter createPresenter() {
        return new TagsPresenter(true, this);
    }

    public void initAdapter() {
        if (mTagsAdapter == null) {
            mTagsAdapter = new TagsAdapter(list);
            mTagsAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
            mTagsAdapter.setOnItemClickListener((adapter, view, position) -> {
                TagsBean.ResponseBean.CollectionsBean bean = (TagsBean.ResponseBean.CollectionsBean) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), VideosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", QueryType.COLLECTIONS_TYPE);
                bundle.putString("name", bean.getTitle());
                bundle.putString("img", bean.getCover_url());
                bundle.putString("order", (String) SharedPreferencesUtils.getParam(getActivity(), "videos_order", "mr"));
                intent.putExtras(bundle);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "sharedImg").toBundle());
            });
            mTagsAdapter.setOnItemLongClickListener((adapter, view, position) -> {
                mBottomSheetDialogTitle.setText(list.get(position).getTitle());
                selectBeanList = new ArrayList<>();
                selectBeanList.add(new SelectBean(Utils.getString(R.string.remove_favorite), R.drawable.baseline_remove_white_48dp));
                selectAdapter.setNewData(selectBeanList);
                selectAdapter.setOnItemClickListener((selectAdapter, selectView, selectPosition) -> {
                    removeTag(position, list.get(position).getTitle());
                    mBottomSheetDialog.dismiss();
                });
                mBottomSheetDialog.show();
                return true;
            });
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            mRecyclerView.setAdapter(mTagsAdapter);
        }
    }

    /**
     * 移除收藏
     */
    private void removeTag(int position, String title) {
        DatabaseUtil.deleteTag(title);
        mTagsAdapter.remove(position);
        if (list.size() == 0) {
            mTagsAdapter.setNewData(list);
            showLoadErrorView("没有收藏");
        }
    }

//    @OnClick(R.id.add)
//    public void addUserTag() {
//        addTags();
//    }

    /**
     * 添加标签
     */
    public void addTags() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_tag, null);
        final EditText et = view.findViewById(R.id.tag);
        builder.setPositiveButton(Utils.getString(R.string.favorite_tag_dialog_positive), null);
        builder.setNegativeButton(Utils.getString(R.string.favorite_tag_dialog_negative), null);
        builder.setTitle(Utils.getString(R.string.favorite_tag_dialog_title));
        builder.setCancelable(false);
        alertDialog = builder.setView(view).create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String text = et.getText().toString();
            if (!text.trim().isEmpty()) {
                addTag(text);
                alertDialog.dismiss();
            } else {
                et.setError(Utils.getString(R.string.favorite_tag_dialog_error));
                return;
            }
        });
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> alertDialog.dismiss());
    }

    /**
     * 自定义标签
     */
    public void addTag(final String str) {
        if (DatabaseUtil.checkTag(str))
            application.showToastMsg(Utils.getString(R.string.tag_is_exist));
        else {
            TagsBean.ResponseBean.CollectionsBean bean = new TagsBean.ResponseBean.CollectionsBean();
            bean.setId(UUID.randomUUID().toString());
            bean.setTitle(str);
            bean.setKeyword("自定义");
            bean.setCover_url("");
            bean.setTotal_views(0);
            bean.setVideo_count(0);
            bean.setCollection_url("");
            DatabaseUtil.addTags(bean);
            mTagsAdapter.addData(0, bean);
        }
    }

    @Override
    protected void loadData() {
        mPresenter.loadData();
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void showLoadErrorView(String msg) {
        loading.setVisibility(View.GONE);
        errorTitle.setText(msg);
        mTagsAdapter.setEmptyView(errorView);
    }

    @Override
    public void showEmptyVIew() {

    }

    @Override
    public void showLoadSuccessView(TagsBean bean, boolean isLoad) {

    }

    @Override
    public void showUserFavoriteView(List<TagsBean.ResponseBean.CollectionsBean> list) {
        loading.setVisibility(View.GONE);
        this.list = list;
        mTagsAdapter.setNewData(this.list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                Toast.makeText(getActivity(), "??", Toast.LENGTH_SHORT);
                break;
        }
    }
}

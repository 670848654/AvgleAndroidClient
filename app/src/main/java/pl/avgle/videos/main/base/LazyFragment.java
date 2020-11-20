package pl.avgle.videos.main.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import butterknife.Unbinder;
import pl.avgle.videos.R;
import pl.avgle.videos.adapter.SelectAdapter;
import pl.avgle.videos.application.Avgle;
import pl.avgle.videos.bean.SelectBean;

public  abstract class LazyFragment<V, P extends Presenter<V>> extends Fragment {
    protected boolean isFragmentVisible;
    private boolean isPrepared;
    private boolean isFirstLoad = true;
    private boolean forceLoad = false;
    protected P mPresenter;
    protected View errorView, emptyView;
    protected TextView errorTitle;
    protected Avgle application;
    protected TextView mBottomSheetDialogTitle;
    protected BottomSheetDialog mBottomSheetDialog;
    protected RecyclerView selectRecyclerView;
    protected SelectAdapter selectAdapter;
    protected List<SelectBean> selectBeanList;
    protected Unbinder mUnBinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isFirstLoad = true;
        mPresenter = createPresenter();
        initCustomViews();
        initmBottomSheetDialog();
        if (application == null) application = (Avgle) getActivity().getApplication();
        View view = initViews(inflater, container, savedInstanceState);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    protected void onVisible() {
        isFragmentVisible = true;
        lazyLoad();
    }

    protected void onInvisible() {
        isFragmentVisible = false;
    }

    protected void lazyLoad() {
        if (isPrepared() && isFragmentVisible()) {
            if (forceLoad || isFirstLoad()) {
                forceLoad = false;
                isFirstLoad = false;
                loadData();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消View的关联
        if (null != mPresenter)
            mPresenter.detachView();
        isPrepared = false;
        mUnBinder.unbind();
    }

    public void initCustomViews() {
        errorView = getLayoutInflater().inflate(R.layout.base_error_view, null);
        errorTitle = errorView.findViewById(R.id.title);
        emptyView = getLayoutInflater().inflate(R.layout.base_emnty_view, null);
    }

    public void initmBottomSheetDialog() {
        View selectView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select, null);
        mBottomSheetDialogTitle = selectView.findViewById(R.id.title);
        selectRecyclerView = selectView.findViewById(R.id.select_list);
        selectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        selectAdapter = new SelectAdapter(getActivity(), selectBeanList);
        selectRecyclerView.setAdapter(selectAdapter);
        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        mBottomSheetDialog.setContentView(selectView);
    }

    protected abstract View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract P createPresenter();

    protected abstract void loadData();

    public boolean isPrepared() {
        return isPrepared;
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    public boolean isFragmentVisible() {
        return isFragmentVisible;
    }
}

package pl.avgle.videos.main.presenter;

import java.util.List;

import pl.avgle.videos.R;
import pl.avgle.videos.bean.TagsBean;
import pl.avgle.videos.main.base.BasePresenter;
import pl.avgle.videos.main.base.Presenter;
import pl.avgle.videos.main.contract.TagsContract;
import pl.avgle.videos.main.model.TagsModel;
import pl.avgle.videos.util.Utils;

public class TagsPresenter extends Presenter<TagsContract.View> implements BasePresenter, TagsContract.LoadDataCallBack {
    private TagsContract.View view;
    private TagsModel model;
    private boolean isLoad;
    private int page;
    private int limit;
    private boolean isFavorite = false;

    public TagsPresenter(boolean isLoad, int page, int limit, TagsContract.View view) {
        super(view);
        this.isLoad = isLoad;
        this.page = page;
        this.limit = limit;
        this.view = view;
        model = new TagsModel();
    }

    public TagsPresenter(boolean isFavorite, TagsContract.View view) {
        super(view);
        this.isFavorite = isFavorite;
        this.view = view;
        model = new TagsModel();
    }

    @Override
    public void success(TagsBean bean, boolean isLoad) {
        if (!bean.isSuccess())
            view.showLoadErrorView(Utils.getString(R.string.api_error));
        else if (bean.getResponse().getCollections().size() > 0)
            view.showLoadSuccessView(bean, isLoad);
        else
            view.showLoadErrorView(Utils.getString(R.string.no_data));
    }

    @Override
    public void userTagsSuccess(List<TagsBean.ResponseBean.CollectionsBean> list) {
        view.showUserFavoriteView(list);
    }

    @Override
    public void error(String msg) {
        view.showLoadErrorView(msg);
    }

    @Override
    public void loadData() {
        if (isFavorite) {
            model.getUserTagsData(this);
        } else {
            view.showLoadingView();
            model.getData(this, isLoad, page, limit);
        }
    }
}

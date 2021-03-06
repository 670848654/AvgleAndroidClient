package pl.avgle.videos.main.presenter;

import java.util.List;

import pl.avgle.videos.R;
import pl.avgle.videos.bean.ChannelBean;
import pl.avgle.videos.main.base.BasePresenter;
import pl.avgle.videos.main.base.Presenter;
import pl.avgle.videos.main.contract.ChannelContract;
import pl.avgle.videos.main.model.ChannelModel;
import pl.avgle.videos.util.Utils;

public class ChannelPresenter extends Presenter<ChannelContract.View> implements BasePresenter, ChannelContract.LoadDataCallBack {
    private ChannelContract.View view;
    private ChannelModel model;
    private boolean isFavorite = false;

    public ChannelPresenter(ChannelContract.View view) {
        super(view);
        this.view = view;
        model = new ChannelModel();
    }

    public ChannelPresenter(boolean isFavorite, ChannelContract.View view) {
        super(view);
        this.isFavorite = isFavorite;
        this.view = view;
        model = new ChannelModel();
    }

    @Override
    public void success(ChannelBean bean) {
        if (!bean.isSuccess())
            view.showLoadErrorView(Utils.getString(R.string.api_error));
        else if (bean.getResponse().getCategories().size() > 0)
            view.showLoadSuccessView(bean);
        else
            view.showLoadErrorView(Utils.getString(R.string.no_data));
    }

    @Override
    public void success(List<ChannelBean.ResponseBean.CategoriesBean> list) {
        view.showLoadUserSuccessView(list);
    }

    @Override
    public void error(String msg) {
        view.showLoadErrorView(msg);
    }

    @Override
    public void loadData() {
        if (isFavorite) {
            model.getUserChannelData(this);
        } else {
            view.showLoadingView();
            model.getChannelData(this);
        }
    }
}

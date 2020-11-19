package pl.avgle.videos.main.contract;

import java.util.List;

import pl.avgle.videos.bean.ChannelBean;
import pl.avgle.videos.main.base.BaseLoadDataCallback;
import pl.avgle.videos.main.base.BaseView;

public interface ChannelContract {
    interface Model {
        void getChannelData(LoadDataCallBack callBack);
        void getUserChannelData(LoadDataCallBack callBack);
    }

    interface View extends BaseView {
        void showLoadSuccessView(ChannelBean bean);
        void showLoadUserSuccessView(List<ChannelBean.ResponseBean.CategoriesBean> list);
    }

    interface LoadDataCallBack extends BaseLoadDataCallback {
        void success(ChannelBean bean);
        void success(List<ChannelBean.ResponseBean.CategoriesBean> list);
    }
}

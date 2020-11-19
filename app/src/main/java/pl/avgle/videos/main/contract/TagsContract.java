package pl.avgle.videos.main.contract;

import java.util.List;

import pl.avgle.videos.bean.TagsBean;
import pl.avgle.videos.main.base.BaseLoadDataCallback;
import pl.avgle.videos.main.base.BaseView;

public interface TagsContract {
    interface Model {
        void getData(LoadDataCallBack callBack, boolean isLoad, int page, int limit);
        void getUserTagsData(LoadDataCallBack callBack);
    }

    interface View extends BaseView {
        void showLoadSuccessView(TagsBean bean, boolean isLoad);
        void showUserFavoriteView(List<TagsBean.ResponseBean.CollectionsBean> list);
    }

    interface LoadDataCallBack extends BaseLoadDataCallback {
        void success(TagsBean bean, boolean isLoad);
        void userTagsSuccess(List<TagsBean.ResponseBean.CollectionsBean> list);
    }
}

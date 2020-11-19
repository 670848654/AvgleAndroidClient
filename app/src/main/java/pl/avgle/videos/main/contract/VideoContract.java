package pl.avgle.videos.main.contract;

import java.util.List;

import pl.avgle.videos.bean.VideoBean;
import pl.avgle.videos.main.base.BaseLoadDataCallback;
import pl.avgle.videos.main.base.BaseView;

public interface VideoContract {
    interface Model {
        //获取频道video list
        void getChannelData(LoadDataCallBack callBack, boolean isLoad, int type, int page, int CHID, int limit, String order);

        //获取tags query video list
        void getTagsSearchData(LoadDataCallBack callBack, boolean isLoad, int type, String title, int page, int limit, String order);

        //获取其他 list
        void getOtherData(LoadDataCallBack callBack, boolean isLoad, int type, int page, String order, String time, int limit);

        //获取用户收藏 list
        void getUserVideosData(LoadDataCallBack callBack);
    }

    interface View extends BaseView {
        //显示加载成功视图
        void showLoadSuccessView(VideoBean bean, boolean isLoad);

        void showUserFavoriteView(List<VideoBean.ResponseBean.VideosBean> list);
    }

    interface LoadDataCallBack extends BaseLoadDataCallback {
        void userTagsSuccess(List<VideoBean.ResponseBean.VideosBean> list);

        void userEmpty();

        void success(VideoBean bean, boolean isLoad);
    }
}

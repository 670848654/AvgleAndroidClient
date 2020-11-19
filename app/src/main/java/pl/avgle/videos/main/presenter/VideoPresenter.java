package pl.avgle.videos.main.presenter;

import java.util.List;

import pl.avgle.videos.bean.VideoBean;
import pl.avgle.videos.config.QueryType;
import pl.avgle.videos.main.base.BasePresenter;
import pl.avgle.videos.main.base.Presenter;
import pl.avgle.videos.main.contract.VideoContract;
import pl.avgle.videos.main.model.VideosModel;

public class VideoPresenter extends Presenter<VideoContract.View> implements BasePresenter, VideoContract.LoadDataCallBack {
    private VideoContract.View view;
    private VideosModel videosModel;
    private boolean isLoad;
    private String title;
    private int page;
    private int CHID;
    private int limit;
    private String order;
    private int type;
    private String time;
    private boolean isFavorite = false;

    //频道
    public VideoPresenter(boolean isLoad, int type, int page, int CHID, int limit, String order, VideoContract.View view) {
        super(view);
        this.view = view;
        this.isLoad = isLoad;
        this.type = type;
        this.page = page;
        this.CHID = CHID;
        this.limit = limit;
        this.order = order;
        videosModel = new VideosModel();
    }

    //tags or search
    public VideoPresenter(boolean isLoad, int type, String title, int page, int limit, String order, VideoContract.View view) {
        super(view);
        this.view = view;
        this.isLoad = isLoad;
        this.type = type;
        this.title = title;
        this.page = page;
        this.limit = limit;
        this.order = order;
        videosModel = new VideosModel();
    }

    //other
    public VideoPresenter(boolean isLoad, int type, int page, String order, String time, int limit, VideoContract.View view) {
        super(view);
        this.view = view;
        this.isLoad = isLoad;
        this.type = type;
        this.time = time;
        this.page = page;
        this.limit = limit;
        this.order = order;
        videosModel = new VideosModel();
    }

    public VideoPresenter(boolean isFavorite, VideoContract.View view) {
        super(view);
        this.view = view;
        this.isFavorite = isFavorite;
        videosModel = new VideosModel();
    }

    @Override
    public void userTagsSuccess(List<VideoBean.ResponseBean.VideosBean> list) {
        view.showUserFavoriteView(list);
    }

    @Override
    public void userEmpty() {
        view.showLoadErrorView("收藏为空");
    }

    @Override
    public void success(VideoBean bean, boolean isLoad) {
        if (!bean.isSuccess())
            view.showLoadErrorView("接口返回出错");
        else if (bean.getResponse().getVideos().size() > 0)
            view.showLoadSuccessView(bean, isLoad);
        else
            view.showLoadErrorView("接口没有返回任何数据");
    }

    @Override
    public void error(String msg) {
        view.showLoadErrorView(msg);
    }

    @Override
    public void loadData() {
        if (isFavorite) {
            videosModel.getUserVideosData(this);
        } else {
            view.showLoadingView();
            if (type == QueryType.CHANNEL_TYPE)
                videosModel.getChannelData(this, isLoad, type, page, CHID, limit, order);
            else if (type == QueryType.COLLECTIONS_TYPE || type == QueryType.QUERY_TYPE)
                videosModel.getTagsSearchData(this, isLoad, type, title, page, limit, order);
            else if (type == QueryType.NEW_TYPE || type == QueryType.HOT_TYPE || type == QueryType.FEATURED_TYPE)
                videosModel.getOtherData(this, isLoad, type, page, order, time, limit);
        }
    }
}

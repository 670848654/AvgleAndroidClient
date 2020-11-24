package pl.avgle.videos.main.model;

import java.io.IOException;
import java.util.List;

import pl.avgle.videos.api.AvgleService;
import pl.avgle.videos.bean.VideoBean;
import pl.avgle.videos.database.DatabaseUtil;
import pl.avgle.videos.main.contract.VideoContract;
import pl.avgle.videos.util.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosModel implements VideoContract.Model {
    AvgleService service = RetrofitUtils.MainRetrofit().create(AvgleService.class);

    @Override
    public void getChannelData(final VideoContract.LoadDataCallBack callBack, final boolean isLoad, int type, int page, int CHID, int limit, String order) {
        service.getChannelVideos(page, CHID, limit, order).enqueue(new Callback<VideoBean>() {
            @Override
            public void onResponse(Call<VideoBean> call, Response<VideoBean> response) {
                if (response.isSuccessful()) {
                    VideoBean videoBean = response.body();
                    List<String> userFavorites = DatabaseUtil.queryAllVideoVIDs();
                    if (userFavorites.size() > 0) {
                        for (VideoBean.ResponseBean.VideosBean videosBean : videoBean.getResponse().getVideos()) {
                            if (userFavorites.contains(videosBean.getVid()))
                                videosBean.setFavorite(true);
                            else
                                videosBean.setFavorite(false);
                        }
                    }
                    callBack.success(response.body(), isLoad);
                }
                else
                    try {
                        callBack.error(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onFailure(Call<VideoBean> call, Throwable t) {
                callBack.error(t.getMessage());
            }
        });
    }

    @Override
    public void getTagsSearchData(final VideoContract.LoadDataCallBack callBack, final boolean isLoad, int type, String title, int page, int limit, String order) {
        service.getTagsSearchVideos(title, page, limit, order).enqueue(new Callback<VideoBean>() {
            @Override
            public void onResponse(Call<VideoBean> call, Response<VideoBean> response) {
                if (response.isSuccessful()) {
                    VideoBean videoBean = response.body();
                    List<String> userFavorites = DatabaseUtil.queryAllVideoVIDs();
                    if (userFavorites.size() > 0) {
                        for (VideoBean.ResponseBean.VideosBean videosBean : videoBean.getResponse().getVideos()) {
                            if (userFavorites.contains(videosBean.getVid()))
                                videosBean.setFavorite(true);
                            else
                                videosBean.setFavorite(false);
                        }
                    }
                    callBack.success(response.body(), isLoad);
                }
                else
                    try {
                        callBack.error(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onFailure(Call<VideoBean> call, Throwable t) {
                callBack.error(t.getMessage());
            }
        });
    }

    @Override
    public void getOtherData(final VideoContract.LoadDataCallBack callBack, final boolean isLoad, int type, int page, String order, String time, int limit) {
        service.getOtherVideos(page, order, time, limit).enqueue(new Callback<VideoBean>() {
            @Override
            public void onResponse(Call<VideoBean> call, Response<VideoBean> response) {
                if (response.isSuccessful()) {
                    VideoBean videoBean = response.body();
                    List<String> userFavorites = DatabaseUtil.queryAllVideoVIDs();
                    if (userFavorites.size() > 0) {
                        for (VideoBean.ResponseBean.VideosBean videosBean : videoBean.getResponse().getVideos()) {
                            if (userFavorites.contains(videosBean.getVid()))
                                videosBean.setFavorite(true);
                            else
                                videosBean.setFavorite(false);
                        }
                    }
                    callBack.success(response.body(), isLoad);
                }
                else
                    try {
                        callBack.error(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onFailure(Call<VideoBean> call, Throwable t) {
                callBack.error(t.getMessage());
            }
        });
    }

    @Override
    public void getUserVideosData(VideoContract.LoadDataCallBack callBack) {
        List<VideoBean.ResponseBean.VideosBean> list = DatabaseUtil.queryAllVideos();
        if (list.size() > 0)
            callBack.userTagsSuccess(list);
        else
            callBack.userEmpty();
    }
}

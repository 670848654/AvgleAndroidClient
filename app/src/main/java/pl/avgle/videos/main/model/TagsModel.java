package pl.avgle.videos.main.model;

import java.io.IOException;
import java.util.List;

import pl.avgle.videos.R;
import pl.avgle.videos.api.AvgleService;
import pl.avgle.videos.bean.TagsBean;
import pl.avgle.videos.database.DatabaseUtil;
import pl.avgle.videos.main.contract.TagsContract;
import pl.avgle.videos.util.RetrofitUtils;
import pl.avgle.videos.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagsModel implements TagsContract.Model {

    @Override
    public void getData(final TagsContract.LoadDataCallBack callBack, final boolean isLoad, int page, int limit) {
        AvgleService service = RetrofitUtils.MainRetrofit().create(AvgleService.class);
        service.getTags(page, limit).enqueue(new Callback<TagsBean>() {
            @Override
            public void onResponse(Call<TagsBean> call, Response<TagsBean> response) {
                if (response.isSuccessful()) {
                    TagsBean tagsBean = response.body();
                    List<String> userFavorites = DatabaseUtil.queryAllTagIds();
                    if (userFavorites.size() > 0) {
                        for (TagsBean.ResponseBean.CollectionsBean collectionsBean : tagsBean.getResponse().getCollections()) {
                            if (userFavorites.contains(collectionsBean.getId()))
                                collectionsBean.setFavorite(true);
                            else
                                collectionsBean.setFavorite(false);
                        }
                    }
                    callBack.success(tagsBean, isLoad);
                }
                else
                    try {
                        callBack.error(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onFailure(Call<TagsBean> call, Throwable t) {
                callBack.error(t.getMessage());
            }
        });
    }

    @Override
    public void getUserTagsData(TagsContract.LoadDataCallBack callBack) {
        List<TagsBean.ResponseBean.CollectionsBean> list = DatabaseUtil.queryAllTags();
        if (list.size() > 0)
            callBack.userTagsSuccess(list);
        else
            callBack.error(Utils.getString(R.string.empty_channel));
    }
}

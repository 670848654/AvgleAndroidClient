package pl.avgle.videos.main.model;

import java.io.IOException;
import java.util.List;

import pl.avgle.videos.api.AvgleService;
import pl.avgle.videos.bean.ChannelBean;
import pl.avgle.videos.database.DatabaseUtil;
import pl.avgle.videos.main.contract.ChannelContract;
import pl.avgle.videos.util.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelModel implements ChannelContract.Model {

    @Override
    public void getChannelData(final ChannelContract.LoadDataCallBack callBack) {
        AvgleService service = RetrofitUtils.MainRetrofit().create(AvgleService.class);
        service.getChannel().enqueue(new Callback<ChannelBean>() {
            @Override
            public void onResponse(Call<ChannelBean> call, Response<ChannelBean> response) {
                if (response.isSuccessful())
                    callBack.success(response.body());
                else {
                    try {
                        callBack.error(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChannelBean> call, Throwable t) {
                callBack.error(t.getMessage());
            }
        });
    }

    @Override
    public void getUserChannelData(final ChannelContract.LoadDataCallBack callBack) {
        List<ChannelBean.ResponseBean.CategoriesBean> list = DatabaseUtil.queryAllChannel();
        if (list.size() > 0)
            callBack.success(list);
        else
            callBack.error("收藏为空");
    }
}

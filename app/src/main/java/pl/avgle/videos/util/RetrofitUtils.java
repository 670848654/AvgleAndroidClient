package pl.avgle.videos.util;

import pl.avgle.videos.api.AvgleApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    public static Retrofit MainRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AvgleApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}

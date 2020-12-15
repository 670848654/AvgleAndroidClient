package pl.avgle.videos.api;

import pl.avgle.videos.bean.ChannelBean;
import pl.avgle.videos.bean.TagsBean;
import pl.avgle.videos.bean.VideoBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AvgleService {
    /**
     * 获取频道
     * Get all video categories api
     */
    @GET("v1/categories")
    @Headers({"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36"})
    Call<ChannelBean> getChannel();

    /**
     * 获取频道视频列表
     * List all videos api
     * 参数
     * page [0, +inf)
     * o  bw (Last viewed) mr (Latest) mv (Most viewed) tr (Top rated) tf (Most favoured) lg (Longest)
     * t  t (1 day) w (1 week) m (1 month) a (Forever)
     * type public private
     * c CHID of a valid video category (integer)
     * limit [1, 250]
     */
    @GET("v1/videos/{page}")
    @Headers({"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36"})
    Call<VideoBean> getChannelVideos(@Path("page") int page, @Query("c") int CHID, @Query("limit") int limit, @Query("o") String order);

    /**
     * 获取Tags
     * Get all video collections api
     * 参数
     * page [0, +inf)
     * limit [1, 250]
     */
    @GET("v1/collections/{page}")
    @Headers({"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36"})
    Call<TagsBean> getTags(@Path("page") int page, @Query("limit") int limit);

    /**
     * 获取视频列表
     * List all videos api
     * 参数
     * page [0, +inf)
     * o  bw (Last viewed) mr (Latest) mv (Most viewed) tr (Top rated) tf (Most favoured) lg (Longest)
     * t  t (1 day) w (1 week) m (1 month) a (Forever)
     * type public private
     * c CHID of a valid video category (integer)
     * limit [1, 250]
     */
    @GET("v1/videos/{page}")
    @Headers({"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36"})
    Call<VideoBean> getOtherVideos(@Path("page") int page, @Query("o") String order, @Query("t") String time, @Query("limit") int limit);

    /**
     * 获取tags query视频列表
     * Search videos api
     * 参数
     * query URL escaped non empty string (三上悠亜)
     * page [0, +inf)
     * limit [1, 250]
     */
    @GET("v1/search/{title}/{page}")
    @Headers({"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36"})
    Call<VideoBean> getTagsSearchVideos(@Path("title") String title, @Path("page") int page, @Query("limit") int limit, @Query("o") String order);

    /**
     * 获取tags query视频列表
     * Search videos api
     * 参数
     * query URL escaped non empty string (sdde-480)
     * page [0, +inf)
     * limit [1, 250]
     */
    @GET("v1/jav/{title}/{page}")
    @Headers({"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36"})
    Call<VideoBean> getJavsSearchVideos(@Path("title") String title, @Path("page") int page, @Query("limit") int limit, @Query("o") String order);

}

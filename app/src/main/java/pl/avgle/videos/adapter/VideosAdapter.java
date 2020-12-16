package pl.avgle.videos.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import pl.avgle.videos.R;
import pl.avgle.videos.bean.VideoBean;
import pl.avgle.videos.config.ImageConfig;
import pl.avgle.videos.util.SharedPreferencesUtils;
import pl.avgle.videos.util.Utils;

public class VideosAdapter extends BaseQuickAdapter<VideoBean.ResponseBean.VideosBean, BaseViewHolder> {
    private Context context;
    public VideosAdapter(Context context, List list) {
        super(R.layout.videos_item, list);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoBean.ResponseBean.VideosBean item) {
        boolean isDarkTheme =(Boolean) SharedPreferencesUtils.getParam(context,"darkTheme",false);
        CardView cardView = helper.getView(R.id.videos_card_view);
        cardView.setCardBackgroundColor(isDarkTheme ? context.getResources().getColor(R.color.dark_window_color ) : context.getResources().getColor(R.color.light_window_color ));
        helper.setTextColor(R.id.videosName, isDarkTheme ? context.getResources().getColor(R.color.dark_navigation_text_color) : context.getResources().getColor(R.color.light_navigation_text_color));
        ImageLoader.getInstance().displayImage(item.getPreview_url(), (ImageView) helper.getView(R.id.videos_img), ImageConfig.getSimpleOptions());
        ImageView favoriteView = helper.getView(R.id.favorite_view);
        if (item.isFavorite())
            favoriteView.setVisibility(View.VISIBLE);
        else
            favoriteView.setVisibility(View.GONE);
        helper.setText(R.id.videosName, item.getTitle());
        helper.setText(R.id.keyword, item.getKeyword().isEmpty() ? "无关键字" : item.getKeyword());
        String time = Utils.timeParse(item.getDuration());
        helper.setText(R.id.date, Utils.toDate(String.valueOf(item.getAddtime())));
        helper.setText(R.id.videosDuration, time.isEmpty() ? "null" : time);
        if (item.isHd()) {
            helper.getView(R.id.hd).setVisibility(View.VISIBLE);
            if (item.getFramerate() > 30) {
                helper.getView(R.id.fps).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.fps).setVisibility(View.INVISIBLE);
            }
        } else {
            helper.getView(R.id.hd).setVisibility(View.INVISIBLE);
            helper.getView(R.id.fps).setVisibility(View.INVISIBLE);
        }
    }
}

package pl.avgle.videos.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import pl.avgle.videos.R;
import pl.avgle.videos.bean.ChannelBean;
import pl.avgle.videos.config.ImageConfig;
import pl.avgle.videos.util.SharedPreferencesUtils;

public class ChannelAdapter extends BaseQuickAdapter<ChannelBean.ResponseBean.CategoriesBean, BaseViewHolder> {
    private Context context;
    public ChannelAdapter(Context context, List list) {
        super(R.layout.channel_item, list);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChannelBean.ResponseBean.CategoriesBean item) {
         /*
        ImageView favoriteView = helper.getView(R.id.favorite_view);
        if (item.isFavorite())
            favoriteView.setVisibility(View.VISIBLE);
        else
            favoriteView.setVisibility(View.GONE);
         */
        helper.addOnClickListener(R.id.channel_card_view);
        boolean isDarkTheme = (Boolean) SharedPreferencesUtils.getParam(context,"darkTheme",false);
        CardView cardView = helper.getView(R.id.channel_card_view);
        cardView.setCardBackgroundColor(isDarkTheme ? context.getResources().getColor(R.color.dark_window_color ) : context.getResources().getColor(R.color.light_window_color ));
        ImageLoader.getInstance().displayImage(item.getCover_url(), (ImageView) helper.getView(R.id.img), ImageConfig.getSimpleOptions());
        helper.setText(R.id.categoriesName, item.getName());
        helper.setText(R.id.categorieCount, item.getTotal_videos() + "");
    }
}

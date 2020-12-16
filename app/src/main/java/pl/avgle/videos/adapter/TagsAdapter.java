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
import pl.avgle.videos.bean.TagsBean;
import pl.avgle.videos.config.ImageConfig;
import pl.avgle.videos.util.SharedPreferencesUtils;

public class TagsAdapter extends BaseQuickAdapter<TagsBean.ResponseBean.CollectionsBean, BaseViewHolder> {
    private Context context;
    public TagsAdapter(Context context, List list) {
        super(R.layout.tags_item, list);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TagsBean.ResponseBean.CollectionsBean item) {
        ImageView favoriteView = helper.getView(R.id.favorite_view);
        boolean isDarkTheme =(Boolean) SharedPreferencesUtils.getParam(context,"darkTheme",false);
        CardView cardView = helper.getView(R.id.channel_card_view);
        cardView.setCardBackgroundColor(isDarkTheme ? context.getResources().getColor(R.color.dark_window_color ) : context.getResources().getColor(R.color.light_window_color ));
        if (item.isFavorite()) favoriteView.setVisibility(View.VISIBLE);
        else favoriteView.setVisibility(View.GONE);
        if (!item.getCover_url().equals("")) {
            ImageLoader.getInstance().displayImage(item.getCover_url(), (ImageView) helper.getView(R.id.collectionsImg), ImageConfig.getSimpleOptions());
            helper.setVisible(R.id.custom_tag, false);
        } else {
            helper.setVisible(R.id.custom_tag, true);
            helper.setImageResource(R.id.collectionsImg, isDarkTheme ? R.drawable.dark_header : R.drawable.light_header);
        }
        helper.setText(R.id.collectionsName, item.getTitle());
    }
}

package pl.avgle.videos.adapter;

import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import pl.avgle.videos.R;
import pl.avgle.videos.bean.ChannelBean;
import pl.avgle.videos.config.ImageConfig;
import pl.avgle.videos.util.Utils;

public class ChannelAdapter extends BaseQuickAdapter<ChannelBean.ResponseBean.CategoriesBean, BaseViewHolder> {
    public ChannelAdapter(List list) {
        super(R.layout.channel_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChannelBean.ResponseBean.CategoriesBean item) {
        helper.getView(R.id.item_view).setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,
                Utils.getChannelTagHeight()));
        helper.addOnClickListener(R.id.channel_card_view);
        ImageLoader.getInstance().displayImage(item.getCover_url(), (ImageView) helper.getView(R.id.img), ImageConfig.getSimpleOptions());
        helper.setText(R.id.categoriesName, item.getName());
        helper.setText(R.id.categorieCount, item.getTotal_videos() + "");
    }
}

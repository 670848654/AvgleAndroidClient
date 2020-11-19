package pl.avgle.videos.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import pl.avgle.videos.R;
import pl.avgle.videos.bean.SelectBean;

public class SelectAdapter extends BaseQuickAdapter<SelectBean, BaseViewHolder> {
    private Context context;
    public SelectAdapter(Context context, List list) {
        super(R.layout.select_item, list);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectBean item) {
        ImageView icon = helper.getView(R.id.img);
        icon.setImageDrawable(context.getDrawable(item.getImgId()));
        helper.setText(R.id.title, item.getTitle());
    }
}
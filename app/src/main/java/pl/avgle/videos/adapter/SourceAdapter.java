package pl.avgle.videos.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import pl.avgle.videos.R;
import pl.avgle.videos.bean.SourceBean;

public class SourceAdapter extends BaseQuickAdapter<SourceBean, BaseViewHolder> {

    public SourceAdapter(List list) {
        super(R.layout.source_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, SourceBean item) {
        helper.setText(R.id.title, item.getTitle());
        helper.setText(R.id.author, item.getAuthor());
        helper.setText(R.id.desc, item.getDesc());
    }
}

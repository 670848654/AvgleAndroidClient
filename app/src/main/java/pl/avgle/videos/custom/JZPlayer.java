package pl.avgle.videos.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;

public class JZPlayer extends JzvdStd {
    private CompleteListener listener;

    public JZPlayer(Context context) {
        super(context);
    }

    public JZPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListener(CompleteListener listener) {
        this.listener = listener;
    }

    public interface CompleteListener {
        void complete();
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen) {
        super.setUp(jzDataSource, screen);
        batteryTimeLayout.setVisibility(GONE);
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        listener.complete();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}

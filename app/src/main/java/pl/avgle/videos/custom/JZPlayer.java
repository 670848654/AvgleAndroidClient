package pl.avgle.videos.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;

public class JZPlayer extends JzvdStd {
    private PlayErrorListener playErrorListener;
    private CompleteListener completeListener;

    public JZPlayer(Context context) {
        super(context);
    }

    public JZPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListener(PlayErrorListener playErrorListener, CompleteListener completeListener) {
        this.playErrorListener = playErrorListener;
        this.completeListener = completeListener;
    }

    public interface PlayErrorListener {
        void playError();
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
        completeListener.complete();
    }

    @Override
    public void onStateError() {
        super.onStateError();
        playErrorListener.playError();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}

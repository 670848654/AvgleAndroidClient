package pl.avgle.videos.main.view.activity;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import pl.avgle.videos.R;
import pl.avgle.videos.adapter.FavoriteFragmentAdapter;
import pl.avgle.videos.main.base.BaseActivity;
import pl.avgle.videos.main.base.Presenter;
import pl.avgle.videos.util.Utils;

public class FavoriteActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private FavoriteFragmentAdapter favoriteFragmentAdapter;
    private String[] tabTitleArr = Utils.getArray(R.array.favorite_arr);

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_favorite;
    }

    @Override
    protected Presenter createPresenter() {
        return null;
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initData() {
        initToolbar();
        initFab();
        initFragment();
    }

    @Override
    protected void setLandscape() {}

    @Override
    protected void setPortrait() {}

    private void initToolbar() {
        toolbar.setTitle(getResources().getString(R.string.favorite_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    public void initFab() {
        if (Utils.checkHasNavigationBar(this)) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();
            params.setMargins(Utils.dpToPx(this, 16),
                    Utils.dpToPx(this, 16),
                    Utils.dpToPx(this, 16),
                    Utils.getNavigationBarHeight(this));
            fab.setLayoutParams(params);
        }
    }

    private void initFragment() {
        tab.addTab(tab.newTab());
        tab.addTab(tab.newTab());
        tab.setupWithViewPager(viewpager);
        tab.getTabAt(0).select();
        tab.setSelectedTabIndicatorColor(getResources().getColor(R.color.tabSelectedTextColor));
        favoriteFragmentAdapter = new FavoriteFragmentAdapter(getSupportFragmentManager(), tab.getTabCount());
        viewpager.setAdapter(favoriteFragmentAdapter);
        tab.getTabAt(0).setText(tabTitleArr[0]);
        tab.getTabAt(1).setText(tabTitleArr[1]);
        viewpager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            /*
            case 0:
                fab.setVisibility(View.GONE);
                fab.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
                break;
             */
            case 0:
                fab.setImageDrawable(this.getDrawable(R.drawable.baseline_add_white_48dp));
                if (!fab.isShown()) {
                    fab.setVisibility(View.VISIBLE);
                    fab.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
                }
                break;
            case 1:
                fab.setImageDrawable(this.getDrawable(R.drawable.baseline_search_white_48dp));
                if (!fab.isShown()) {
                    fab.setVisibility(View.VISIBLE);
                    fab.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
                }
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

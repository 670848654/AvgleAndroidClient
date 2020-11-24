package pl.avgle.videos.main.view.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pl.avgle.videos.R;
import pl.avgle.videos.adapter.SourceAdapter;
import pl.avgle.videos.bean.SourceBean;
import pl.avgle.videos.main.base.BaseActivity;
import pl.avgle.videos.main.base.Presenter;
import pl.avgle.videos.util.Utils;

public class OpenSourceActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mSwipe)
    SwipeRefreshLayout mSwipe;
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    private SourceAdapter adapter;
    private List<SourceBean> list = new ArrayList<>();

    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_source;
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
        initSwipe();
        initList();
        initAdapter();
    }

    public void initToolbar() {
        toolbar.setTitle(Utils.getString(R.string.open_source_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> supportFinishAfterTransition());
    }

    public void initSwipe() {
        mSwipe.setEnabled(false);
    }

    public void initList() {
        list.add(new SourceBean("BaseRecyclerViewAdapterHelper", "CymChad", "BRVAH:Powerful and flexible RecyclerAdapter", "https://github.com/CymChad/BaseRecyclerViewAdapterHelper"));
        list.add(new SourceBean("Android-Universal-Image-Loader", "nostra13", "Powerful and flexible library for loading, caching and displaying images on Android.", "https://github.com/nostra13/Android-Universal-Image-Loader"));
        list.add(new SourceBean("EasyPermissions", "googlesamples", "Simplify Android M system permissions", "https://github.com/googlesamples/easypermissions"));
        list.add(new SourceBean("MaterialEditText", "rengwuxian", "EditText in Material Design", "https://github.com/rengwuxian/MaterialEditText"));
        list.add(new SourceBean("JiaoZiVideoPlayer", "lipangit", "Android VideoPlayer MediaPlayer VideoView MediaView Float View And Fullscreen", "https://github.com/lipangit/JiaoZiVideoPlayer"));
        list.add(new SourceBean("ExoPlayer", "google", "An extensible media player for Android", "https://github.com/google/ExoPlayer"));
        list.add(new SourceBean("Blurry", "wasabeef", "Blurry is an easy blur library for Android", "https://github.com/wasabeef/Blurry"));
        list.add(new SourceBean("retrofit", "square", "Type-safe HTTP client for Android and Java by Square, Inc.", "https://github.com/square/retrofit"));
        list.add(new SourceBean("butterknife", "JakeWharton", "Bind Android views and callbacks to fields and methods. ", "https://github.com/JakeWharton/butterknife"));
        list.add(new SourceBean("gson", "google", "A Java serialization/deserialization library to convert Java Objects into JSON and back", "https://github.com/google/gson"));
        list.add(new SourceBean("MaterialSearchBar", "mancj", "Material Design Search Bar for Android", "https://github.com/mancj/MaterialSearchBar"));
        list.add(new SourceBean("EventBus", "greenrobot", "Event bus for Android and Java that simplifies communication between Activities, Fragments, Threads, Services, etc. Less code, better quality.", "https://github.com/greenrobot/EventBus"));
    }

    public void initAdapter() {
        if (Utils.checkHasNavigationBar(this))
            mRecyclerView.setPadding(0,0,0, Utils.getNavigationBarHeight(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SourceAdapter(list);
        adapter.openLoadAnimation();
        adapter.setNotDoAnimationCount(1);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (Utils.isFastClick())
                Utils.openBrowser(OpenSourceActivity.this, list.get(position).getUrl());
        });
        mRecyclerView.setAdapter(adapter);
    }
}

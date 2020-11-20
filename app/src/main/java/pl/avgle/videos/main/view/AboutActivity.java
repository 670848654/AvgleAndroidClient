package pl.avgle.videos.main.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pl.avgle.videos.R;
import pl.avgle.videos.api.UpdateApi;
import pl.avgle.videos.main.base.BaseActivity;
import pl.avgle.videos.main.base.Presenter;
import pl.avgle.videos.net.HttpGet;
import pl.avgle.videos.util.Utils;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.source_text)
    TextView source_text;
    @BindView(R.id.cache)
    TextView cache;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ProgressDialog p;

    @Override
    protected void initBeforeView() {}

    @Override
    protected int getLayout() {
        return R.layout.activity_about;
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
        initText();
    }

    public void initToolbar() {
        toolbar.setTitle(Utils.getString(R.string.about_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    public void initText() {
        String textSource = "<font color='#ea4336'>A</font><font color='#fbbc05'>v</font><font color='#4185f4'>g</font><font color='#33a853'>l</font><font color='#ea4336'>e</font>";
        source_text.setText(Html.fromHtml(textSource));
        cache.setText(Environment.getExternalStorageDirectory() + Utils.getString(R.string.cache_text));
        version.setText(Utils.getASVersionName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_source:
                if (Utils.isFastClick()) startActivity(new Intent(AboutActivity.this,OpenSourceActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.avgle, R.id.check_update})
    public void openBrowser(RelativeLayout relativeLayout) {
        switch (relativeLayout.getId()) {
            case R.id.avgle:
                Utils.openBrowser(this, Utils.getString(R.string.source_url));
                break;
            case R.id.check_update:
                p = Utils.getProDialog(this, R.string.check_update_text);
                new Handler().postDelayed(() -> new HttpGet(UpdateApi.CHECK_UPDATE, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> {
                            Utils.cancelProDialog(p);
                            application.showToastMsg(Utils.getString(R.string.ck_network_error_start));
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        try {
                            JSONObject obj = new JSONObject(json);
                            String newVersion = obj.getString("tag_name");
                            if (newVersion.equals(Utils.getASVersionName()))
                                runOnUiThread(() -> {
                                    Utils.cancelProDialog(p);
                                    application.showToastMsg(Utils.getString(R.string.no_new_version));
                                });
                            else {
                                String downUrl = obj.getJSONArray("assets").getJSONObject(0).getString("browser_download_url");
                                String body = obj.getString("body");
                                runOnUiThread(() -> {
                                    Utils.cancelProDialog(p);
                                    Utils.findNewVersion(AboutActivity.this,
                                            newVersion,
                                            body,
                                            (dialog, which) -> {
                                                dialog.dismiss();
                                                Utils.openBrowser(AboutActivity.this, downUrl);
                                            },
                                            (dialog, which) -> dialog.dismiss()
                                    );
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }), 1000);
                break;
        }
    }
}

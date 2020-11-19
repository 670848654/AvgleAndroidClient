package pl.avgle.videos.main.view;

import android.content.Intent;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pl.avgle.videos.R;
import pl.avgle.videos.api.UpdateApi;
import pl.avgle.videos.main.base.BaseActivity;
import pl.avgle.videos.main.base.Presenter;
import pl.avgle.videos.net.HttpGet;
import pl.avgle.videos.util.StatusBarUtil;
import pl.avgle.videos.util.Utils;

public class StartActivity extends BaseActivity {
    @Override
    protected void initBeforeView() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_start;
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
        hideNavBar();
        StatusBarUtil.setTranslucent(this, 0);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            checkUpdate();
        }, 1500);
    }

    private void checkUpdate() {
        new HttpGet(UpdateApi.CHECK_UPDATE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    application.showToastMsg(Utils.getString(R.string.ck_network_error_start));
                    openMain();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject obj = new JSONObject(json);
                    String newVersion = obj.getString("tag_name");
                    if (newVersion.equals(Utils.getASVersionName()))
                        runOnUiThread(() -> openMain());
                    else {
                        String downUrl = obj.getJSONArray("assets").getJSONObject(0).getString("browser_download_url");
                        String body = obj.getString("body");
                        runOnUiThread(() -> Utils.findNewVersion(StartActivity.this,
                                newVersion,
                                body,
                                (dialog, which) -> {
                                    dialog.dismiss();
                                    Utils.openBrowser(StartActivity.this, downUrl);
                                },
                                (dialog, which) -> {
                                    dialog.dismiss();
                                    openMain();
                                })
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void openMain() {
        startActivity(new Intent(StartActivity.this, ChannelActivity.class));
        StartActivity.this.finish();
    }
}

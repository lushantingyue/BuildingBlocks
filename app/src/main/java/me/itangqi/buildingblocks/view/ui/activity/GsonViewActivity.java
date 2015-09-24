package me.itangqi.buildingblocks.view.ui.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;

import me.itangqi.buildingblocks.R;
import me.itangqi.buildingblocks.domin.application.App;
import me.itangqi.buildingblocks.model.entity.DailyGson;
import me.itangqi.buildingblocks.presenters.GsonNewsPresenter;
import me.itangqi.buildingblocks.view.IGsonNews;
import me.itangqi.buildingblocks.view.ui.activity.base.SwipeBackActivity;

/**
 * Created by Troy on 2015/9/24.
 */
public class GsonViewActivity extends SwipeBackActivity implements IGsonNews{

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_IMG = "img";
    public static final String EXTRA_TITLE = "title";

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private LinearLayout mLinearLayout;
    private Handler mHandler = new UIHandler(this);
    private GsonNewsPresenter mPresenter;
    private ImageView mHeader;
    private ImageView mImageView_avatar;
    private TextView mTextView_author;
    private TextView mTextView_bio;

    private DailyGson mDailyGson;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_gson_news;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getIntent().getIntExtra(EXTRA_ID, 0);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        mPresenter = new GsonNewsPresenter(this);
        initView(title);
        mPresenter.getGsonNews(id);
    }

    @Override
    public void loadGson(DailyGson dailyGson) {
        mDailyGson = dailyGson;
        Glide.with(this).load(dailyGson.getImage()).fitCenter().into(mHeader);
        new UITask().execute(dailyGson);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    private void initView(String title) {
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_content);
        mHeader = (ImageView) findViewById(R.id.news_header);
        mImageView_avatar = (ImageView) findViewById(R.id.iv_avatar);
        mTextView_author = (TextView) findViewById(R.id.tv_author);
        mTextView_bio = (TextView) findViewById(R.id.tv_bio);
        mCollapsingToolbarLayout.setTitle(title);
    }

    private static class UIHandler extends Handler {
        private final WeakReference<Activity> mActivityWeakReference;

        public UIHandler(Activity activity) {
            mActivityWeakReference = new WeakReference<Activity>(activity);
        }
    }

    private class UITask extends AsyncTask<DailyGson,Integer,Boolean> {

        @Override
        protected Boolean doInBackground(DailyGson... params) {
            Map<String, LinkedHashMap<String, String>> soup = mPresenter.getContentMap(params[0]);
            LinkedHashMap<String, String> extra = soup.get("extra");
            LinkedHashMap<String, String> article = soup.get("article");
            loadExtra(extra);
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        private void loadExtra(LinkedHashMap<String, String> extra) {
            for (Map.Entry<String, String> entry : extra.entrySet()) {
                if (entry.getValue().equals("avatar")) {
                    Glide.with(App.getContext()).load(entry.getKey()).into(mImageView_avatar);
                }
                if (entry.getValue().equals("author")) {
                    mTextView_author.setText(entry.getKey());
                }
                if (entry.getValue().equals("bio")) {
                    mTextView_bio.setText(entry.getKey());
                }
            }
        }

        private void loadArticle(LinkedHashMap<String, String> article) {
            for (Map.Entry<String, String> entry : article.entrySet()) {

            }
        }
    }
}

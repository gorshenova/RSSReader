package com.egorshenova.rss.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.database.dao.FeedDataSource;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.ui.base.BaseActivity;

import java.util.List;


public class SplashActivity extends BaseActivity {

    final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                FeedDataSource feedDataSource = new FeedDataSource();
                final List<RSSFeed> allFeeds = feedDataSource.getAllFeeds();
                GlobalContainer.getInstance().setFeeds(allFeeds);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });
        t.start();
    }

    @Override
    public int getContentHolderId() {
        return 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }
}

/*
package com.egorshenova.rss.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.egorshenova.rss.R;
import com.egorshenova.rss.database.dao.FeedDataSource;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.ui.base.BaseActivity;

import java.util.List;

*/
/**
 * Created by eyablonskaya on 10-Feb-17.
 *//*


public class SplashActivity extends BaseActivity {
    FeedDataSource feedDataSource;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        feedDataSource =  new FeedDataSource();
        feedDataSource.open();


        final Handler h =  new Handler();

        Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {

                List<RSSFeed> allFeeds = feedDataSource.getAllFeeds();
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i =  new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                    }
                });
            }
        });
        t.start();

    }

    @Override
    protected void onResume() {
        feedDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        feedDataSource.close();
        super.onPause();
    }

    @Override
    public int getContentHolderId() {
        return 0;
    }
}
*/

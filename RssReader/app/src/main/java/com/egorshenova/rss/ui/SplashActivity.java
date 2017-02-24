package com.egorshenova.rss.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.egorshenova.rss.mvp.splash.SplashContract;
import com.egorshenova.rss.mvp.splash.SplashPresenter;
import com.egorshenova.rss.ui.base.BaseActivity;
import com.egorshenova.rss.utils.Logger;


public class SplashActivity extends BaseActivity implements SplashContract.View {

    private Logger logger = Logger.getLogger(SplashActivity.class);
    private SplashPresenter presenter =  new SplashPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.attachView(this);
        presenter.loadSavedFeeds();
    }

    @Override
    public int getContentHolderId() {
        return 0;
    }

    @Override
    protected void onResume() {
        logger.debug("onResume");
        super.onResume();
        presenter.attachView(this);
    }

    @Override
    protected void onPause() {
        logger.debug("onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        logger.debug("onDestroy");
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void openHomeScreen() {
        logger.debug("openHomeScreen");
        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}

package com.egorshenova.rss.mvp.splash;

import android.os.Handler;
import android.os.Looper;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.database.dao.FeedDataSource;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.BasePresenter;
import com.egorshenova.rss.utils.Logger;

import java.util.List;

/**
 * Created by eyablonskaya on 22-Feb-17.
 */

public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {

    final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void detachView() {
        super.detachView();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void loadSavedFeeds() {
        Logger.debug(SplashPresenter.class, "loadSavedFeeds");
        checkViewAttached();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //load only feeds without items
                //items for current feed are loaded in feed content view after the first opening
                FeedDataSource feedDataSource = new FeedDataSource();
                final List<RSSFeed> allFeeds = feedDataSource.getAllFeeds();

                GlobalContainer.getInstance().setFeeds(allFeeds);

                Logger.debug(SplashPresenter.class, "---> all feeds: " + allFeeds);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().openHomeScreen();
                    }
                });
            }
        });
        t.start();
    }
}

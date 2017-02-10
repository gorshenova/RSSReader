package com.egorshenova.rss.mvp.home;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.database.dao.FeedDataSource;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.BasePresenter;
/*import com.egorshenova.rss.ui.HomeActivity;
import com.egorshenova.rss.ui.SplashActivity;*/
import com.egorshenova.rss.utils.Logger;

import java.io.IOException;
import java.util.List;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {
    private FeedDataSource feedDataSource;

    @Override
    public void initializeContent() {

        feedDataSource = new FeedDataSource();
        feedDataSource.open();

        final Handler h = new Handler();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                final List<RSSFeed> allFeeds = feedDataSource.getAllFeeds();
                GlobalContainer.getInstance().setFeeds(allFeeds);

                h.post(new Runnable() {
                    @Override
                    public void run() {
                        if (allFeeds != null && allFeeds.size() > 0) {
                            setupTabsLabels();
                            getView().openFeedContentView(allFeeds.get(0));
                            getView().updateTabLayoutVisibility(View.VISIBLE);
                        } else {
                            getView().updateTabLayoutVisibility(View.GONE);
                            getView().openAddFeedView();
                        }
                    }
                });
            }
        });
        t.start();


       /* List<RSSFeed> feeds = GlobalContainer.getInstance().getFeeds();
        if (feeds != null && feeds.size() > 0) {
            setupTabsLabels();
            getView().openFeedContentView(feeds.get(0));
            getView().updateTabLayoutVisibility(View.VISIBLE);
        } else {
            getView().updateTabLayoutVisibility(View.GONE);
            getView().openAddFeedView();
        }*/
    }

    @Override
    public void saveFeeds() {
        try {
            GlobalContainer.getInstance().saveFeeds();
        } catch (IOException e) {
            Logger.getLogger(HomePresenter.class).error(e.getMessage(), e);
        }
    }

    private void setupTabsLabels() {
        List<RSSFeed> feeds = GlobalContainer.getInstance().getFeeds();
        for (RSSFeed f : feeds) {
            getView().addTab(f);
        }
    }
}

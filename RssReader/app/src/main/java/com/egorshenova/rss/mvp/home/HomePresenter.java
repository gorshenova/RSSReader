package com.egorshenova.rss.mvp.home;

import android.os.Handler;
import android.view.View;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.database.dao.FeedDataSource;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.BasePresenter;

import java.util.List;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {
    private FeedDataSource feedDataSource;

    @Override
    public void attachView(HomeContract.View view) {
        super.attachView(view);
        feedDataSource = new FeedDataSource();
        feedDataSource.open();
    }

    @Override
    public void detachView() {
        super.detachView();
        feedDataSource.close();
    }

    @Override
    public void initializeContent() {
        getView().showProgress();

        final Handler h = new Handler();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                final List<RSSFeed> allFeeds = feedDataSource.getAllFeeds();
                GlobalContainer.getInstance().setFeeds(allFeeds);

                h.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().hideProgress();

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
    }

    private void setupTabsLabels() {
        List<RSSFeed> feeds = GlobalContainer.getInstance().getFeeds();
        for (RSSFeed f : feeds) {
            getView().addTab(f);
        }
    }
}

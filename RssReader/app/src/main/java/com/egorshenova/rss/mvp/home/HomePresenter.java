package com.egorshenova.rss.mvp.home;

import android.os.Handler;
import android.view.View;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.RSSReaderApplication;
import com.egorshenova.rss.database.dao.FeedDataSource;
import com.egorshenova.rss.models.FeedChangeObject;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.BasePresenter;
import com.egorshenova.rss.utils.Logger;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter, Observer {

    private Logger logger = Logger.getLogger(HomePresenter.class);

    @Override
    public void attachView(HomeContract.View view) {
        super.attachView(view);
        RSSReaderApplication.get().getFeedObservable().addObserver(this);
    }

    @Override
    public void detachView() {
        super.detachView();
        RSSReaderApplication.get().getFeedObservable().deleteObserver(this);
    }

    @Override
    public void initializeContent() {
        getView().showProgress();

        final Handler h = new Handler();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                FeedDataSource feedDataSource = new FeedDataSource();
                final List<RSSFeed> allFeeds = feedDataSource.getAllFeeds();
                GlobalContainer.getInstance().setFeeds(allFeeds);
                getView().updateMenu();
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


    @Override
    public void update(Observable observable, Object o) {
        //This method is notified after feed data changes
        FeedChangeObject obj = (FeedChangeObject) o;
        logger.debug("Observable starts. Feed data changed: " + obj);

        if (obj.isUpdated()) {
            //update tab
            getView().updateTab(obj.getFeed());
        } else {
            //add new tab
            getView().addTab(obj.getFeed());
        }

        // show feed content
        getView().openFeedContentView(obj.getFeed());

        //update menu
        getView().updateMenu();
    }

    private void setupTabsLabels() {
        List<RSSFeed> feeds = GlobalContainer.getInstance().getFeeds();
        for (RSSFeed f : feeds) {
            getView().addTab(f);
        }
    }
}

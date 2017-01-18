package com.egorshenova.rss.mvp.home;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.BasePresenter;

import java.util.List;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

    @Override
    public void initializeContent() {
        List<RSSFeed> feeds = GlobalContainer.getInstance().getFeeds();
        if (feeds != null && feeds.size() > 0) {
            getView().openFeedContentView(feeds.get(0));
        } else {
            getView().openAddFeedView();
        }
    }

    @Override
    public void saveFeeds() {
        GlobalContainer.getInstance().saveFeeds();
    }
}

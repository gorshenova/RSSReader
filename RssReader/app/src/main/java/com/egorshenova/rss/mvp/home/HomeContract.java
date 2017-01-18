package com.egorshenova.rss.mvp.home;

import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.IBasePresenter;
import com.egorshenova.rss.mvp.abs.IBaseView;

public interface HomeContract {
    interface View extends IBaseView {
        void openFeedContentView(RSSFeed feed);

        void openAddFeedView();
    }

    interface Presenter extends IBasePresenter<HomeContract.View> {
        void initializeContent();
        void saveFeeds();
    }
}
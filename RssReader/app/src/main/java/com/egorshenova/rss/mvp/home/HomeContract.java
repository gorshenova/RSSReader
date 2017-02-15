package com.egorshenova.rss.mvp.home;

import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.IBasePresenter;
import com.egorshenova.rss.mvp.abs.IBaseView;

import java.util.List;

public interface HomeContract {
    interface View extends IBaseView {
        void openFeedContentView(RSSFeed feed);

        void openAddFeedView();

        void addTab(RSSFeed feed);

        void updateTabLayoutVisibility(int visibility);

        void showProgress();

        void hideProgress();
    }

    interface Presenter extends IBasePresenter<HomeContract.View> {
        void initializeContent();
    }
}

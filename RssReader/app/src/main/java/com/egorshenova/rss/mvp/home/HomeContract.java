package com.egorshenova.rss.mvp.home;

import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.IBasePresenter;
import com.egorshenova.rss.mvp.abs.IBaseView;

public interface HomeContract {
    interface View extends IBaseView {

        void openAddFeedView();

        void openFeedContentView(RSSFeed feed);

        void showProgress();

        void hideProgress();

        void updateMenu();

    }

    interface Presenter extends IBasePresenter<HomeContract.View> {
        void initializeContent();
    }
}

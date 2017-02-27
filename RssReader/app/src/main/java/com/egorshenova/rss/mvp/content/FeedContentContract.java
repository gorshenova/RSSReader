package com.egorshenova.rss.mvp.content;

import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.IBasePresenter;
import com.egorshenova.rss.mvp.abs.IBaseView;

import java.util.List;

public interface FeedContentContract {

    interface View extends IBaseView {

        void showLoading();

        void hideLoading();

        void showError(String message);


        void addTabsAndShowContent(List<RSSFeed> feeds, int selectedTabPost);

        void updateFeedContent(RSSFeed feed);

    }

    interface Presenter extends IBasePresenter<FeedContentContract.View> {

        void initializeContent(RSSFeed selectedFeed);

        void sortByNewest();

        void sortByOldest();

        void updateFeed(RSSFeed selectedFeed);
    }
}

package com.egorshenova.rss.mvp.content;

import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.IBasePresenter;
import com.egorshenova.rss.mvp.abs.IBaseView;

public interface FeedContentContract {

    interface View extends IBaseView {

        void showLoading();

        void hideLoading();

        void showFeedContent(RSSFeed feed);

        void showError(String message);

    }

    interface Presenter extends IBasePresenter<FeedContentContract.View> {

        void openFeedContent();

        void sortByNewest();

        void sortByOldest();

        void updateFeed();
    }
}

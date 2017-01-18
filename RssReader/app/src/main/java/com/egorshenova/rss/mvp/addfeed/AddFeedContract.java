package com.egorshenova.rss.mvp.addfeed;

import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.IBasePresenter;
import com.egorshenova.rss.mvp.abs.IBaseView;

public interface AddFeedContract {

    interface View extends IBaseView{
        void showError(String message);
        void showLoading();
        void hideLoading();
        void openRSSContent(RSSFeed feed);
    }

    interface Presenter extends IBasePresenter<AddFeedContract.View>{
        void fetchFeed(String urlStr);
    }
}

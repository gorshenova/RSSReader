package com.egorshenova.rss.mvp.menu;

import com.egorshenova.rss.models.RSSMenuItem;
import com.egorshenova.rss.mvp.abs.IBasePresenter;
import com.egorshenova.rss.mvp.abs.IBaseView;

import java.util.List;

public interface MenuContract {

    interface View extends IBaseView {
        void showMenuItems(List<RSSMenuItem> menuItems);
    }

    interface Presenter extends IBasePresenter<MenuContract.View> {
        void prepareMenuItems();
    }
}

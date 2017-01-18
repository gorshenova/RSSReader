package com.egorshenova.rss.mvp.menu;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.R;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.models.RSSMenuItem;
import com.egorshenova.rss.mvp.abs.BasePresenter;

import java.util.ArrayList;
import java.util.List;

public class MenuPresenter extends BasePresenter<MenuContract.View> implements MenuContract.Presenter {

    @Override
    public void prepareMenuItems() {
        List<RSSFeed> feeds = GlobalContainer.getInstance().getFeeds();

        List<RSSMenuItem> menuList = new ArrayList<>();
        for(RSSFeed f: feeds){
            RSSMenuItem item = new RSSMenuItem(RSSMenuItem.MenuAction.ACTION_OPEN_FEED, f);
            menuList.add(item);
        }
        menuList.add(new RSSMenuItem(getContext().getResources().getString(R.string.add_rss_feed), RSSMenuItem.MenuAction.ADD_FEED_ACTION));
        getView().showMenuItems(menuList);
    }
}

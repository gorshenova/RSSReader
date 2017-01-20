package com.egorshenova.rss.callbacks;

import com.egorshenova.rss.models.RSSMenuItem;

public interface MenuClickCallback {
    void onMenuOpenFeedClick(RSSMenuItem menuItem);

    void onMenuAddFeedClick();
}

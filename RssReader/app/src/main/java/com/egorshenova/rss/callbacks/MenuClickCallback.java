package com.egorshenova.rss.callbacks;

import com.egorshenova.rss.models.RSSMenuItem;

public interface MenuClickCallback {
    void onMenuItemClick(RSSMenuItem menuItem);

    void onAddFeedClick();
}

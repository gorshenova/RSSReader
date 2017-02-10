package com.egorshenova.rss.utils;

import com.egorshenova.rss.models.RSSItem;

import java.util.Comparator;
import java.util.Date;

public class ComparatorByPubDate implements Comparator<RSSItem> {

    @Override
    public int compare(RSSItem ob1, RSSItem ob2) {
        Date date1 = new Date(ob1.getPubDate());
        Date date2 = new Date(ob2.getPubDate());
        if ((date1 != null && date2 != null) && date1.before(date2)) {
            return -1;
        } else if ((date1 != null && date2 != null) && date1.after(date2)) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object object) {
        return false;
    }
}

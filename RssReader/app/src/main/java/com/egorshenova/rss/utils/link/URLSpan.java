package com.egorshenova.rss.utils.link;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.style.ClickableSpan;
import android.view.View;

public class URLSpan extends ClickableSpan implements ParcelableSpan {

    public static final int URL_SPAN = 11;

    private final String mURL;

    private URLClickListener urlClickListener;

    public URLSpan(String url) {
        mURL = url;
    }

    public URLSpan(Parcel src) {
        mURL = src.readString();
    }

    public void setOnUrlClickListener(URLClickListener urlClickListener) {
        this.urlClickListener = urlClickListener;
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    /** @hide */
    public int getSpanTypeIdInternal() {
        return URL_SPAN;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    /** @hide */
    public void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeString(mURL);
    }

    public String getURL() {
        return mURL;
    }

    @Override
    public void onClick(View widget) {
        if (urlClickListener != null) {
            urlClickListener.onClick(getURL());
        }
    }
}

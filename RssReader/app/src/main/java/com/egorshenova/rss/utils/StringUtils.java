package com.egorshenova.rss.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.webkit.URLUtil;

import com.egorshenova.rss.utils.link.Linkify;
import com.egorshenova.rss.utils.link.URLClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtils {

    public static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";

    public static boolean isURLValid(String url) {
        return URLUtil.isValidUrl(url.toLowerCase().trim());
    }

    public static long converDateToLong(String dateStr, String language) throws ParseException {
        // date format: Wed, 31 Oct 2012 12:12:42 +0100
        Locale locale = Locale.US;
        if (language != null) {
            locale = new Locale(language);
        }

        Logger.getLogger(StringUtils.class).debug("===> dateStr: " + dateStr + ", language: " + language + ", locale: " + locale.toString());

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, locale);
        Date date = sdf.parse(dateStr);
        return date.getTime();
    }

    public static SpannableString linkifyText(String description, URLClickListener urlClickListener) {
        SpannableString spannableText = new SpannableString(description);
        spannableText.setSpan(new RelativeSizeSpan(1.5f), description.length() - 1, description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Linkify.addLinks(spannableText, Linkify.WEB_URLS, urlClickListener);
        return spannableText;
    }

    public static String replaceSingleQuoteByDoubleOne(String str){
        return str.replace("'", "''");
    }
}

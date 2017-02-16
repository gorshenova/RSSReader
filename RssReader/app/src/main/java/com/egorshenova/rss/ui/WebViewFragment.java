package com.egorshenova.rss.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.egorshenova.rss.Constants;
import com.egorshenova.rss.R;
import com.egorshenova.rss.ui.base.BaseFragment;

public class WebViewFragment extends BaseFragment {

    private WebView mWebView;

    public static WebViewFragment getInstance(Bundle args) {
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_web_view, container, false);
        mWebView = (WebView) v.findViewById(R.id.webview);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to openDB in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String url = getParam(Constants.BUNDLE_KEY_SHOW_URL);
        mWebView.loadUrl(url);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private String getParam(String tag) {
        Bundle args = getArguments();
        return (args != null ? args.getString(tag) : "");
    }
}

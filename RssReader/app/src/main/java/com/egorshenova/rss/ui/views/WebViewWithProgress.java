package com.egorshenova.rss.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.egorshenova.rss.R;

public class WebViewWithProgress extends FrameLayout {

    public interface OnPageLoaded {
        void onPageLoaded();
    }

    private WebView webView;
    private ProgressBar progressBar;
    private String url;
    private OnPageLoaded callback;

    {
        View.inflate(getContext(), R.layout.layout_web_view_with_progress, this);
        webView = (WebView) findViewById(R.id.web_view_);
        progressBar = (ProgressBar) findViewById(R.id.web_view_progress);
    }

    public WebViewWithProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public WebViewWithProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WebViewWithProgress(Context context) {
        super(context);
        initView();
    }

    @SuppressWarnings("deprecation")
    protected void initView() {
        getWebView().setWebViewClient(
                new ProgressWebViewClient(getProgressBar()));
        getWebView().getSettings().setPluginState(WebSettings.PluginState.ON);
        getWebView().getSettings().setJavaScriptEnabled(true);
        getWebView().getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        getWebView().getSettings().setSupportMultipleWindows(false);
        getWebView().getSettings().setSupportZoom(false);
    }

    protected class ProgressWebViewClient extends WebViewClient {

        private ProgressBar progressBar;
        private boolean loadingFinished = true;
        private boolean redirect = false;

        public ProgressWebViewClient(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            loadingFinished = false;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!redirect) {
                loadingFinished = true;
            }

            if (loadingFinished && !redirect) {
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                redirect = false;
            }
            if (callback != null)
                callback.onPageLoaded();
        }
    }

    public WebView getWebView() {
        return webView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        getWebView().loadUrl(getUrl());
    }

    public void setOnPageLoaded(OnPageLoaded callback) {
        this.callback = callback;
    }

    public boolean canGoBack() {
        return webView.canGoBack();
    }

    public boolean canGoForward() {
        return webView.canGoForward();
    }

    public void goBack() {
        webView.goBack();
    }

    public void goForward() {
        webView.goForward();
    }
}

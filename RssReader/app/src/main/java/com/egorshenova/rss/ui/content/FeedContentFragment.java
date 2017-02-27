package com.egorshenova.rss.ui.content;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.egorshenova.rss.Constants;
import com.egorshenova.rss.R;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.content.FeedContentContract;
import com.egorshenova.rss.mvp.content.FeedContentPresenter;
import com.egorshenova.rss.ui.WebViewFragment;
import com.egorshenova.rss.ui.base.BaseFragment;
import com.egorshenova.rss.utils.DialogHelper;
import com.egorshenova.rss.utils.Logger;
import com.egorshenova.rss.utils.link.URLClickListener;

import java.util.List;

public class FeedContentFragment extends BaseFragment implements FeedContentContract.View {

    private Logger logger = Logger.getLogger(FeedContentFragment.class);
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TabLayout tabLayout;
    private FeedContentAdapter adapter;
    private ProgressBar progressBar;
    private FeedContentPresenter presenter;

    public static FeedContentFragment getInstance(Bundle args) {
        FeedContentFragment frag = new FeedContentFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logger.debug("onCreateView");

        View view = inflater.inflate(R.layout.fragment_feed_content, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.content_progressbar);
        progressBar.setVisibility(View.GONE);
        recyclerView = (RecyclerView) view.findViewById(R.id.content_recycler_view);
        adapter = new FeedContentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.content_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //disable swipe
                swipeRefreshLayout.setEnabled(false);

                //update feed
                TabLayout.Tab selectedTab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
                presenter.updateFeed((RSSFeed) selectedTab.getTag());

                //activate swipe
                swipeRefreshLayout.setEnabled(true);

                //cancel loading
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        adapter.setUrlClickListener(new URLClickListener() {
            @Override
            public void onClick(String url) {
                logger.debug("urlClickCallback onClick");
                openWebViewFragment(url);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                logger.debug("onTabSelected");
                showFeedContent((RSSFeed) tab.getTag());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //TODO Not implementation yet
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //TODO Not implementation yet
            }
        });

        RSSFeed selectedFeed = getFeedByArguments();
        presenter = new FeedContentPresenter();
        presenter.attachView(this);
        presenter.initializeContent(selectedFeed);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        logger.debug("onViewCreated");
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy");
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void showError(String message) {
        DialogHelper.showSingleButtonDialog(getContext(), message);
    }

    private RSSFeed getFeedByArguments() {
        return (RSSFeed) getArguments().getSerializable(Constants.BUNDLE_KEY_FEED);
    }

    public void sortByOldest() {
        presenter.sortByOldest();
    }

    public void sortByNewest() {
        presenter.sortByNewest();
    }

    @Override
    public void addTabsAndShowContent(List<RSSFeed> feeds, int selectedTabPost) {
        logger.debug("addTabsAndShowContent");
        for (RSSFeed f : feeds) {
            addTab(f);
        }
        tabLayout.getTabAt(selectedTabPost).select();
    }

    @Override
    public void updateFeedContent(RSSFeed feed) {
        logger.debug("updateFeedContent");
        int tabPosition = tabLayout.getSelectedTabPosition();
        TabLayout.Tab tab = tabLayout.getTabAt(tabPosition);
        tab.setText(feed.getTitle());
        tab.select();
    }

    private void addTab(RSSFeed feed) {
        logger.debug("addTab: " + feed);
        TabLayout.Tab newTab = tabLayout.newTab();
        newTab.setTag(feed);
        newTab.setText(feed.getTitle());

        //feedId starts from 1 but tab index starts from 0, so tabPosition is calculated as feedId - 1
        //add tab also includes the 'onTabSelected' method call
        int tabPosition = feed.getId() - 1;
        tabLayout.addTab(newTab, tabPosition);
    }

    private void showFeedContent(RSSFeed feed) {
        logger.debug("showFeedContent, feed: " + feed);
        adapter.setItems(feed.getItems());
        adapter.notifyDataSetChanged();
    }

    private void openWebViewFragment(String url) {
        logger.debug("openWebViewFragment");
        Bundle args = new Bundle();
        args.putString(Constants.BUNDLE_KEY_SHOW_URL, url);
        openFragment(WebViewFragment.getInstance(args), Constants.FRAGMENT_TAG_WEB_VIEW);
    }

}

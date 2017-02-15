package com.egorshenova.rss.ui.content;

import android.os.Bundle;
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
import com.egorshenova.rss.ui.base.BaseFragment;
import com.egorshenova.rss.utils.DialogHelper;
import com.egorshenova.rss.utils.Logger;
import com.egorshenova.rss.utils.link.URLClickListener;

public class FeedContentFragment extends BaseFragment implements FeedContentContract.View {

    private Logger logger = Logger.getLogger(FeedContentFragment.class);
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FeedContentAdapter adapter;
    private ProgressBar progressBar;
    private FeedContentPresenter presenter;
    private URLClickListener listener;

    public static FeedContentFragment getInstance(Bundle args) {
        FeedContentFragment frag = new FeedContentFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_content, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.content_progressbar);
        progressBar.setVisibility(View.GONE);
        recyclerView = (RecyclerView) view.findViewById(R.id.content_recycler_view);
        adapter = new FeedContentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.content_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //disable swipe
                swipeRefreshLayout.setEnabled(false);

                //update feed
                presenter.updateFeed();

                //activate swipe
                swipeRefreshLayout.setEnabled(true);

                //cancel loading
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RSSFeed feed = getFeedByArguments();
        presenter = new FeedContentPresenter(feed);
        presenter.attachView(this);
        presenter.openFeedContent();
    }

    @Override
    public void onDestroy() {
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

    public void showFeedContent(RSSFeed feed) {
        adapter.setItems(feed.getItems());
        adapter.notifyDataSetChanged();
        adapter.setUrlClickListener(listener);
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

    public void setUrlClickCallback(URLClickListener listener) {
        this.listener = listener;
        if(adapter!= null){
            adapter.setUrlClickListener(listener);
        }
    }
}

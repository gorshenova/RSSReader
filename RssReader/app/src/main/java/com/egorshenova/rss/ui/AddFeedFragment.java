package com.egorshenova.rss.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.egorshenova.rss.R;
import com.egorshenova.rss.callbacks.AddFeedCallback;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.addfeed.AddFeedContract;
import com.egorshenova.rss.mvp.addfeed.AddFeedPresenter;
import com.egorshenova.rss.ui.base.BaseFragment;
import com.egorshenova.rss.utils.DialogHelper;

public class AddFeedFragment extends BaseFragment implements AddFeedContract.View, View.OnClickListener{

    private LinearLayout rootView;
    private EditText urlEditText;
    private Button fetchButton, clearButton;
    private AddFeedPresenter presenter;
    private AddFeedCallback callback;

    public static AddFeedFragment getInstance(Bundle args){
        AddFeedFragment fragment = new AddFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_feed, container, false);
        rootView =  (LinearLayout) view.findViewById(R.id.add_feed_root_view);
        urlEditText = (EditText) view.findViewById(R.id.url_edit_text);
        fetchButton = (Button) view.findViewById(R.id.fetch_button);
        clearButton = (Button) view.findViewById(R.id.clear_button);

        fetchButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        presenter =  new AddFeedPresenter();
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter.unregister();
    }

    @Override
    public void onClick(View view) {

        hideKeyboard(rootView);
        hideKeyboard();

        if(view.getId() == R.id.fetch_button){
            onFetchButtonClick();
        } else if (view.getId() == R.id.clear_button){
            onClearButtonClick();
        }
    }

    public void setCallback(AddFeedCallback callback) {
        this.callback = callback;
    }

    private void onClearButtonClick() {
        urlEditText.setText("");
    }

    private void onFetchButtonClick() {
        String urlStr = urlEditText.getText().toString();
        presenter.fetchFeed(urlStr);
    }

    @Override
    public void showError(String message) {
        DialogHelper.showSingleButtonDialog(getContext(), message);
    }

    @Override
    public void showLoading() {
        createProgress(getResources().getString(R.string.progress_loading));
    }

    @Override
    public void hideLoading() {
        closeProgress();
    }

    @Override
    public void openRSSContent(RSSFeed feed) {
        if(callback!= null){
            callback.openAddedFeed(feed);
        }
    }
}

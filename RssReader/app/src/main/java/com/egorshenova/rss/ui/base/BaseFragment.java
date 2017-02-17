package com.egorshenova.rss.ui.base;

import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class BaseFragment extends DialogFragment {

    private ProgressDialog progressDialog;

    public void createProgress(String message) {
        getBaseActivity().createProgress(message);
    }

    public void closeProgress() {
        getBaseActivity().closeProgress();
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected void showKeyboard(View view) {
        getBaseActivity().showKeyboard(view);
    }

    protected void hideKeyboard() {
        getBaseActivity().hideKeyboard();
    }

    protected void hideKeyboard(View view) {
        getBaseActivity().hideKeyboard(view);
    }

}

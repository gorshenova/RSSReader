package com.egorshenova.rss.ui.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.egorshenova.rss.utils.DialogHelper;

public class BaseFragment extends DialogFragment {

    private ProgressDialog progressDialog;

    public void createProgress(String message) {
        safeClose(progressDialog);
        progressDialog = DialogHelper.createAndShowProgressDialogWithMessage(getContext(), message);
    }

    public void closeProgress() {
        safeClose(progressDialog);
    }

    protected void safeClose(Dialog dialog) {
        DialogHelper.safeClose(dialog);
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

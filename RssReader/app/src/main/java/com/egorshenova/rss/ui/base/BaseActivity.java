package com.egorshenova.rss.ui.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.egorshenova.rss.R;
import com.egorshenova.rss.utils.DialogHelper;
import com.egorshenova.rss.utils.Logger;

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {
        Logger.debug(BaseActivity.class, "onBackPressed, getBackStackEntryCount: " + getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                DialogHelper.showDoubleButtonDialog(this, getString(R.string.dialog_exit_message), R.string.dialog_exit_button_yes, R.string.dialog_exit_button_No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int what) {
                        if (what == Dialog.BUTTON_POSITIVE) {
                            finish();
                        }
                    }
                });
            } else {
                getSupportFragmentManager().popBackStackImmediate();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void createProgress(String message) {
        safeClose(progressDialog);
        progressDialog = DialogHelper.createAndShowProgressDialogWithMessage(this, message);
    }

    public void closeProgress() {
        safeClose(progressDialog);
    }

    protected void safeClose(Dialog dialog) {
        DialogHelper.safeClose(dialog);
    }

    public FragmentTransaction getFragmentTransaction() {
        return getSupportFragmentManager().beginTransaction();
    }

    public abstract int getContentHolderId();

    public void add(BaseFragment fragment, int container) {
        getFragmentTransaction()
                .add(container, fragment, fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    public void add(BaseFragment fragment, int container, String tag) {
        getFragmentTransaction()
                .add(container, fragment, tag)
                .commitAllowingStateLoss();
    }

    public void replace(BaseFragment fragment, int container, String tag) {
        getFragmentTransaction()
                .replace(container, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void addToBackStack(BaseFragment fragment, int container, String tag) {
        getFragmentTransaction()
                .add(container, fragment, tag)
                .addToBackStack(tag)
                .commitAllowingStateLoss();
    }

    public void replaceInBackStack(BaseFragment fragment, int container, String tag) {
        getFragmentTransaction()
                .replace(container, fragment, tag)
                .addToBackStack(tag)
                .commitAllowingStateLoss();
    }

    protected void openFragment(BaseFragment fragment, String fragmentTag) {
        if (fragment.isAdded()) {
            replaceInBackStack(fragment, getContentHolderId(), fragmentTag);
        } else {
            addToBackStack(fragment, getContentHolderId(), fragmentTag);
        }
    }

    public void showKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            Logger.getLogger(BaseActivity.class).error(e.getMessage(), e);
        }
    }

    /**
     * Hide keyboard on touch of UI
     */
    public void hideKeyboard(View view) {

        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                hideKeyboard(innerView);
            }
        }
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(v);
                    return false;
                }

            });
        }

    }

    /**
     * Hide keyboard while focus is moved
     */
    public void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                if (android.os.Build.VERSION.SDK_INT < 11) {
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                            0);
                } else {
                    if (this.getCurrentFocus() != null) {
                        inputManager.hideSoftInputFromWindow(this
                                        .getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    view.clearFocus();
                }
                view.clearFocus();
            }
        }
    }

}

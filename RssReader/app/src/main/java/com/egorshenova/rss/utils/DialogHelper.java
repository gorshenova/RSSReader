package com.egorshenova.rss.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

public class DialogHelper {

    public static void showToast(Context context, int message) {
        showToast(context, context.getString(message));
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showSingleButtonDialog(Context context, int message, int title) {
        showSingleButtonDialog(context, context.getString(message), title, null);
    }


    private static void showSingleButtonDialog(Context context, String message, int title, OnClickListener listener) {
        AlertDialog.Builder builder = getAlertDialogBuilder(context);
        builder.setMessage(message).setPositiveButton(android.R.string.ok, listener);
        if (title > 0) {
            builder.setTitle(title);
        }
        showDialog(builder);
    }

    public static void showSingleButtonDialog(Context context, String message) {
        showSingleButtonDialog(context, message, null);
    }

    public static void showSingleButtonDialog(Context context, String message, OnClickListener onClickListener) {
        showSingleButtonDialog(context, message, 0, onClickListener);
    }

    public static void showSingleButtonDialog(Context context, int message) {
        showSingleButtonDialog(context, context.getString(message), null);
    }

    public static void showSingleButtonDialog(Context context, int message, OnClickListener onClickListener) {
        showSingleButtonDialog(context, context.getString(message), onClickListener);
    }


    public static void showDoubleButtonDialog(Context context, String message, int positiveButtonResId, int negativeButtonResId,
                                              OnClickListener negativeListener) {
        showDoubleButtonDialog(context, null, message, positiveButtonResId, negativeButtonResId, negativeListener);
    }

    public static void showDoubleButtonDialog(Context context, String title, String message,
                                              int positiveButtonResId, int negativeButtonResId, OnClickListener clickListener) {
        AlertDialog.Builder builder = getAlertDialogBuilder(context);
        builder.setMessage(message).setTitle(title)
                .setNegativeButton(negativeButtonResId, clickListener)
                .setPositiveButton(positiveButtonResId, clickListener);
        showDialog(builder);
    }

    public static void showTripleButtonDialogs(Context context, String message, int positiveButtonResId, int negativeButtonResId, int neutralButtonResId, OnClickListener clickListener) {
        AlertDialog.Builder builder = getAlertDialogBuilder(context);
        builder.setMessage(message)
                .setNegativeButton(negativeButtonResId, clickListener)
                .setNeutralButton(neutralButtonResId, clickListener)
                .setPositiveButton(positiveButtonResId, clickListener);
        showDialog(builder);
    }

    private static void showDialog(AlertDialog.Builder builder) {
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    private static AlertDialog.Builder getAlertDialogBuilder(Context context) {
        return new AlertDialog.Builder(context);
    }

    public static void safeClose(Dialog dialog) {
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ProgressDialog createAndShowProgressDialogWithMessage(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }
}

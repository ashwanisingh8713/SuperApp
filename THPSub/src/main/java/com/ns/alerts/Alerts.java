package com.ns.alerts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.ns.thpremium.R;


public class Alerts {

    /**
     * Shows a toast with the given text.
     */
    public static void showToast(Context context, String text) {
        //Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        showToastAtCenter(context, text);
    }

    public static void showToastAtTop(Context context, int text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 20);
        toast.show();
    }

    public static void showToastAtCenter(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 300);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_custom_layout, null);
        TextView textView = view.findViewById(R.id.messageToast);
        textView.setText(text);
        toast.setView(view);
        toast.show();
    }

    public static void showToast(Context context, int text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_custom_layout, null);
        TextView textView = view.findViewById(R.id.messageToast);
        textView.setText(text);
        toast.setView(view);
        toast.show();
    }

    public static void showToastAtTop(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 20);
        toast.show();
    }
    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    public static void showSnackbar(Activity activity, final String text) {
        View container = activity.findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    public static Snackbar showSnackbarInfinite(Activity activity, final int mainTextStringId, final int actionStringId,
                                                View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content),
                activity.getString(mainTextStringId),
                Snackbar.LENGTH_LONG)
                .setAction(activity.getString(actionStringId), listener);
        snackbar.show();
        return snackbar;
    }

    public static Snackbar showSnackbar(Activity activity, final int mainTextStringId, final int actionStringId,
                                                View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content),
                activity.getString(mainTextStringId),
                Snackbar.LENGTH_LONG)
                .setAction(activity.getString(actionStringId), listener);
        snackbar.show();
        return snackbar;
    }


    public static void showAlertDialogWithYesNo(final Context context, String message, final int alertID, final AlertDialogClickListener alertDialogClickListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        if(alertDialogClickListener != null) {
                            alertDialogClickListener.alertDialogClickYes(alertID);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        if(alertDialogClickListener != null) {
                            alertDialogClickListener.alertDialogClickNo(alertID);
                        }
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlertDialogNoBtn(final Context context, String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setTitle(title);
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showAlertDialogNoBtnWithCancelable(final Context context, String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(true)
                .setTitle(title);

        final AlertDialog alert = builder.create();
        alert.show();
    }


    public static void showErrorDailog(FragmentManager fm, String title, String message) {
        if(message != null) {
            if(message.contains("Unable to resolve host")) {
                message = "Please check your internet connectivity.";
                title = "Connection Error!";
            }
        }
        ErrorDialog calendarViewDialogFragment = ErrorDialog.newInstance(title, message);
        calendarViewDialogFragment.show(fm, "errorDialog");
    }

    public static void showAlertDialogOKBtn(final Context context, String title, String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    public static void showAlertDialogOKListener(final Context context, String title, String msg, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK", onClickListener);
        alertDialog.show();
    }

    public static void showAlertNoInternetRetry(Context context, String title, String message, DialogInterface.OnClickListener onClickRetryListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(true);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("RELOAD", onClickRetryListener);
        alertDialog.show();
    }


    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage(context.getResources().getString(R.string.please_wait));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        return progress;
    }

    public static void noConnectionSnackBar(View view, AppCompatActivity context) {
        if(view == null) {
            return;
        }
        Snackbar mSnackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout mSnackbarView = (Snackbar.SnackbarLayout) mSnackbar.getView();
        mSnackbarView.setBackgroundColor(Color.BLACK);
        TextView textView = mSnackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        TextView textView1 = mSnackbarView.findViewById(com.google.android.material.R.id.snackbar_action);
        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        View snackView = context.getLayoutInflater().inflate(R.layout.thp_noconnection_snackbar, null);
        snackView.findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
            }
        });
        mSnackbarView.addView(snackView);
        mSnackbar.show();
    }


}

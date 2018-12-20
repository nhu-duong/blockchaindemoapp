package me.nhu_duong.blockchaindemo.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Util {
    public static String username = "";

    public Long getCurrentTimeStamp() {
        return System.currentTimeMillis()/1000;
    }

    public void showMessage(Activity context, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}

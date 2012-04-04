package com.company.comanda.brian;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Constants {

//    public static final String SERVER_LOCATION = "10.0.2.2";
    public static final String SERVER_LOCATION = "peter.comandamobile.com";
    public static final int ORDER_PLACED_RESULT = 345;


    public static void showErrorDialog(int messageId, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(messageId)
        .setCancelable(false)
        .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

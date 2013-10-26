package com.company.comanda.brian.helpers;

import android.app.Dialog;
import android.view.WindowManager;

public class LayoutHelper {

    public static void dialog_fill_parent(Dialog dialog){
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        dialog.getWindow().setAttributes(lp);
    }
}

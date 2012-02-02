package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Button;

public class UIEditMenuItem extends Composite {

    private static UIEditMenuItemUiBinder uiBinder = GWT
            .create(UIEditMenuItemUiBinder.class);
    @UiField TextBox tbName;
    @UiField DoubleBox dbPrice;
    @UiField FileUpload fuImageFile;
    @UiField TextArea taDescription;
    @UiField Button btnSaveChanges;

    interface UIEditMenuItemUiBinder extends UiBinder<Widget, UIEditMenuItem> {
    }

    public UIEditMenuItem() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}

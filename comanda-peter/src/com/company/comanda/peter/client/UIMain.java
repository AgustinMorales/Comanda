package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TabLayoutPanel;

public class UIMain extends Composite {

    private static UIMainUiBinder uiBinder = GWT.create(UIMainUiBinder.class);
    @UiField TabLayoutPanel mainTabPanel;

    interface UIMainUiBinder extends UiBinder<Widget, UIMain> {
    }

    public UIMain() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}

package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UIBackground extends Composite {

    private static UIBackgroundUiBinder uiBinder = GWT
            .create(UIBackgroundUiBinder.class);

    interface UIBackgroundUiBinder extends UiBinder<Widget, UIBackground> {
    }

    public UIBackground() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}

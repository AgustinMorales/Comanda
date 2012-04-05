package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UIEditRestaurants extends Composite {

    private static UIEditRestaurantsUiBinder uiBinder = GWT
            .create(UIEditRestaurantsUiBinder.class);

    interface UIEditRestaurantsUiBinder extends
            UiBinder<Widget, UIEditRestaurants> {
    }

    public UIEditRestaurants() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}

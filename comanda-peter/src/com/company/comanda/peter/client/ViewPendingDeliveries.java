package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ViewPendingDeliveries extends Composite {

    public static final int PAGE_SIZE = 25;
    
    private static ViewPendingDeliveriesUiBinder uiBinder = GWT
            .create(ViewPendingDeliveriesUiBinder.class);

    @UiField CellTable<String[]> odersTable;
    @UiField SimplePager odersPager;
    @UiField Label lblMessage;
    @UiField VerticalPanel ordersTableContainer;
    
    
    interface ViewPendingDeliveriesUiBinder extends
            UiBinder<Widget, ViewPendingDeliveries> {
    }

    public ViewPendingDeliveries() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}

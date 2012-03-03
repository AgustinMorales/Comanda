package com.company.comanda.peter.client;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UIViewDeliveryDetails extends Composite {
    
    @UiField CellTable<String[]> ordersTable;
    @UiField Label lblMessage;
    @UiField VerticalPanel ordersTableContainer;
    @UiField SimplePager ordersPager;
    
    private OrdersTableUpdater ordersTableUpdater;
    
    
    public static final int PAGE_SIZE = 25;
    
    private static UIViewDeliveryDetailsUiBinder uiBinder = GWT
            .create(UIViewDeliveryDetailsUiBinder.class);

    interface UIViewDeliveryDetailsUiBinder extends
            UiBinder<Widget, UIViewDeliveryDetails> {
    }

    public UIViewDeliveryDetails(String billKeyString) {
        initWidget(uiBinder.createAndBindUi(this));
        ordersTableUpdater = new OrdersTableUpdater(ordersTable);
        configureTable();
        
        ordersTableUpdater.setSelectecBillKeyString(billKeyString);
        
        ordersTableUpdater.refreshTable();
    }

    
    private void configureTable(){
        
        
        
     // Add a text column to show the name.
        TextColumn<String[]> tableNameColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[0];
            }
        };
        
        
        ordersTable.addColumn(tableNameColumn, "Item");

        ordersPager.setDisplay(ordersTable);
        ordersPager.setPage(PAGE_SIZE);
        
    }
    
}

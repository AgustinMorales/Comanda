package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.BillState;
import com.company.comanda.peter.shared.BillType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;

public class UIViewPendingDeliveries extends Composite {

    public static final int PAGE_SIZE = 25;
    
    private static ViewPendingDeliveriesUiBinder uiBinder = GWT
            .create(ViewPendingDeliveriesUiBinder.class);

    @UiField CellTable<String[]> odersTable;
    @UiField SimplePager odersPager;
    @UiField Label lblMessage;
    @UiField VerticalPanel ordersTableContainer;
    
    private BillsTableUpdater billsTableUpdater;
    
    private GUIServiceAsync guiService = GWT.create(GUIService.class);
    
    interface ViewPendingDeliveriesUiBinder extends
            UiBinder<Widget, UIViewPendingDeliveries> {
    }

    public UIViewPendingDeliveries() {
        initWidget(uiBinder.createAndBindUi(this));
        
        TextColumn<String[]> tableNameColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[1];
            }
        };
        
        
        odersTable.addColumn(tableNameColumn, "Dirección");

        TextColumn<String[]> orderNameColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[2];
            }
        };
        odersTable.addColumn(orderNameColumn, "Fecha");
        
        billsTableUpdater = new BillsTableUpdater(odersTable);
        billsTableUpdater.setState(BillState.OPEN);
        billsTableUpdater.setType(BillType.DELIVERY);
        
        odersPager.setDisplay(odersTable);
        odersPager.setPageSize(PAGE_SIZE);
        
        odersTable.addCellPreviewHandler(new Handler<String[]>() {

            @Override
            public void onCellPreview(CellPreviewEvent<String[]> event) {
                boolean isClick = "click".equals(event.getNativeEvent().getType());
                if(isClick){
                    String[] object = (String[])event.getValue();
                    DialogBox dialog = new DialogBox();
                    dialog.setWidget(new UIViewDeliveryDetails(object[0]));
                    dialog.center();
                }
                
            }
        });
        
    }
    
    
    public void setAutoUpdate(boolean value){
        billsTableUpdater.setAutoUpdate(value);
    }
}

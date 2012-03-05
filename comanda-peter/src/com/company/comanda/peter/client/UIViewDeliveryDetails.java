package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.BillState;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UIViewDeliveryDetails extends Composite {
    
    @UiField CellTable<String[]> ordersTable;
    @UiField Label lblMessage;
    @UiField VerticalPanel ordersTableContainer;
    @UiField SimplePager ordersPager;
    @UiField Button btnAcceptBill;
    @UiField Button btnReject;
    @UiField Button btnBack;
    
    private OrdersTableUpdater ordersTableUpdater;
    private GUIServiceAsync guiSevice = GWT.create(GUIService.class);
    
    private String billKeyString;
    private DialogBox containerDialog;
    
    
    public static final int PAGE_SIZE = 25;
    
    private static UIViewDeliveryDetailsUiBinder uiBinder = GWT
            .create(UIViewDeliveryDetailsUiBinder.class);

    interface UIViewDeliveryDetailsUiBinder extends
            UiBinder<Widget, UIViewDeliveryDetails> {
    }

    public UIViewDeliveryDetails(String billKeyString, final DialogBox containerDialog) {
        this.billKeyString = billKeyString;
        this.containerDialog = containerDialog;
        initWidget(uiBinder.createAndBindUi(this));
        lblMessage.setVisible(true);
        ordersTableContainer.setVisible(false);
        ordersTableUpdater = new OrdersTableUpdater(ordersTable);
        ordersTableUpdater.setSelectedBillType(null);
        configureTable();
        
        ordersTableUpdater.setSelectecBillKeyString(billKeyString);
        ordersTableUpdater.setUpdateListener(new AbstractTableUpdater.UpdateListener() {
            
            @Override
            public void onUpdate() {
                lblMessage.setVisible(false);
                ordersTableContainer.setVisible(true);
                containerDialog.center();
            }
        });
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
    
    @UiHandler("btnAcceptBill")
    void onButtonClick(ClickEvent event) {
        doChangingStatus();
        guiSevice.changeBillState(billKeyString, BillState.DELIVERED, new AsyncCallback<Void>() {
            
            @Override
            public void onSuccess(Void result) {
                doNotChangingStatus();
                containerDialog.hide();
                
            }
            
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error");
                doNotChangingStatus();
                containerDialog.hide();
            }
        });
    }
    
    @UiHandler("btnBack")
    void onBtnBackClick_1(ClickEvent event) {
        containerDialog.hide();
    }
    
    @UiHandler("btnReject")
    void onBtnRejectClick(ClickEvent event) {
        doChangingStatus();
        guiSevice.changeBillState(billKeyString, BillState.REJECTED, new AsyncCallback<Void>() {
            
            @Override
            public void onSuccess(Void result) {
                doNotChangingStatus();
                containerDialog.hide();
                
            }
            
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error");
                doNotChangingStatus();
                containerDialog.hide();
            }
        });
    }
    
    private void doChangingStatus(){
        btnAcceptBill.setEnabled(false);
        btnReject.setEnabled(false);
        btnBack.setEnabled(false);
    }
 
    private void doNotChangingStatus(){
        btnAcceptBill.setEnabled(true);
        btnReject.setEnabled(true);
        btnBack.setEnabled(true);
    }
}

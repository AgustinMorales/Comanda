package com.company.comanda.peter.client;

import com.company.comanda.peter.client.UIRejectReason.RejectHandler;
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
import com.google.gwt.user.client.ui.ListBox;

public class UIViewDeliveryDetails extends Composite {
    
    
    private static final int[] DELIVERY_TIMES = {10,20,30,40,50,60};
    
    @UiField CellTable<String[]> ordersTable;
    @UiField Label lblMessage;
    @UiField VerticalPanel ordersTableContainer;
    @UiField SimplePager ordersPager;
    @UiField Button btnAcceptBill;
    @UiField Button btnReject;
    @UiField Button btnBack;
    @UiField Label lblAddress;
    @UiField Label lblPhone;
    @UiField Label lblState;
    @UiField Label lblTotalAmount;
    @UiField ListBox lvEstimatedTime;
    
    private OrdersTableUpdater ordersTableUpdater;
    private GUIServiceAsync guiSevice = GWT.create(GUIService.class);
    
    private String billKeyString;
    private DialogBox containerDialog;
    
    private UIRejectReason rejectReason;
    private DialogBox rejectDialog;
    
    public static final int PAGE_SIZE = 25;
    
    private static UIViewDeliveryDetailsUiBinder uiBinder = GWT
            .create(UIViewDeliveryDetailsUiBinder.class);

    interface UIViewDeliveryDetailsUiBinder extends
            UiBinder<Widget, UIViewDeliveryDetails> {
    }

    public UIViewDeliveryDetails(String[] object, final DialogBox containerDialog) {
        this.billKeyString = object[0];
        this.containerDialog = containerDialog;
        initWidget(uiBinder.createAndBindUi(this));
        lblMessage.setVisible(true);
        ordersTableContainer.setVisible(false);
        ordersTableUpdater = new OrdersTableUpdater(ordersTable);
        ordersTableUpdater.setSelectedBillType(null);
        configureTable();
        
        lblAddress.setText(object[1]);
        lblPhone.setText(object[3]);
        lblState.setText(object[5]);
        lblTotalAmount.setText(object[4]);
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
        
        for(int time: DELIVERY_TIMES){
            lvEstimatedTime.addItem(time + " minutos", "" + time);
        }
        
        if(object[5].equals("OPEN") == false){
            btnAcceptBill.setEnabled(false);
            btnReject.setEnabled(false);
        }
        rejectDialog = new DialogBox();
        rejectReason = new UIRejectReason();
        rejectDialog.setWidget(rejectReason);
        
        rejectReason.setRejectHandler(new RejectHandler() {
            
            @Override
            public void onReject(BillState newState) {
                guiSevice.changeBillState(billKeyString, newState, 
                        null, new AsyncCallback<Void>() {
                    
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
            
            @Override
            public void onCancel() {
                rejectDialog.hide();
                doNotChangingStatus();
            }
        });
    }

    
    private void configureTable(){
        
        
        
     // Add a text column to show the name.
        TextColumn<String[]> tableNameColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[0];
            }
        };
        
        
        ordersTable.addColumn(tableNameColumn, "Elemento");
        
        TextColumn<String[]> tableNumberColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[3];
            }
        };
        
        
        ordersTable.addColumn(tableNumberColumn, "Unidades");
        
        TextColumn<String[]> tablePriceColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[4];
            }
        };
        
        
        ordersTable.addColumn(tablePriceColumn, "Precio unitario");

        ordersPager.setDisplay(ordersTable);
        ordersPager.setPage(PAGE_SIZE);
        
    }
    
    @UiHandler("btnAcceptBill")
    void onButtonClick(ClickEvent event) {
        doChangingStatus();
        int delay = Integer.parseInt(lvEstimatedTime.getValue(lvEstimatedTime.getSelectedIndex()));
        guiSevice.changeBillState(billKeyString, BillState.DELIVERED, 
                delay, new AsyncCallback<Void>() {
            
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
        rejectDialog.center();
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

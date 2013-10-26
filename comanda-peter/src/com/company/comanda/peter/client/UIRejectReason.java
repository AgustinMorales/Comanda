package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.BillState;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class UIRejectReason extends Composite {

    private static UIRejectReasonUiBinder uiBinder = GWT
            .create(UIRejectReasonUiBinder.class);
    @UiField Label lblAddress;
    @UiField Label lblTotalAmount;
    @UiField ListBox lvReason;
    @UiField Button btnReject;
    @UiField Button btnBack;

    
    private static final BillState[] status = new BillState[]{
        BillState.REJECTED_NO_DELIVERY_THERE,
        BillState.REJECTED_OFF_DUTY,
        BillState.REJECTED_OUT_OF_SOMETHING,
        BillState.REJECTED_UNKNOWN_ADDRESS,
        BillState.REJECTED_NOT_ENOUGH_FOR_DELIVERY,
        BillState.REJECTED_OVERLOAD,
        BillState.REJECTED,
    };
    
    private static final String[] strings = new String[]{
        "Fuera de zona de reparto",
        "Fuera de horario",
        "Existencias agotadas",
        "Dirección desconocida",
        "Importe insuficiente",
        "Sobrecarga",
        "Otros",
    };
    
    private RejectHandler handler;
    
    public interface RejectHandler{
        void onReject(BillState newState);
        void onCancel();
    }
    
    interface UIRejectReasonUiBinder extends UiBinder<Widget, UIRejectReason> {
    }

    public UIRejectReason() {
        initWidget(uiBinder.createAndBindUi(this));
        for(String text:strings){
            lvReason.addItem(text);
        }
    }

    public void setRejectHandler(RejectHandler handler){
        this.handler = handler;
    }
    
    
    @UiHandler("btnBack")
    void onBtnBackClick(ClickEvent event) {
        handler.onCancel();
    }
    @UiHandler("btnReject")
    void onBtnRejectClick(ClickEvent event) {
        int index = lvReason.getSelectedIndex();
        handler.onReject(status[index]);
    }
    
    public void setData(String address, String totalAmount){
        lblAddress.setText(address);
        lblTotalAmount.setText(totalAmount);
    }
}

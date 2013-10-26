package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TabLayoutPanel;

public class UIMain extends Composite {

    private static UIMainUiBinder uiBinder = GWT.create(UIMainUiBinder.class);
    @UiField TabLayoutPanel mainTabPanel;
    //    @UiField UIViewAllOrders viewAllOrders;
    //    @UiField UISelectAndViewTableOrders selectTableAndViewOrders;
    @UiField UIViewPendingDeliveries viewDeliveryBills;
    @UiField UIViewPendingDeliveries viewAllDeliveryBills;
    @UiField Label lblRestaurantName;
    @UiField Label lblLogout;

    interface UIMainUiBinder extends UiBinder<Widget, UIMain> {
    }

    public UIMain(String restaurantName) {
        initWidget(uiBinder.createAndBindUi(this));
        lblRestaurantName.setText("Restaurante: " + restaurantName);
        viewDeliveryBills.setAutoUpdate(true);
    }

    @UiHandler("mainTabPanel")
    public void onSelection(SelectionEvent<Integer> event) {
        //        viewAllOrders.setAutoUpdate(false);
        //        selectTableAndViewOrders.setAutoUpdate(false);
        viewDeliveryBills.setAutoUpdate(false);
        viewAllDeliveryBills.setAutoUpdate(false);
        switch (event.getSelectedItem()) {
        case 0:
            viewDeliveryBills.setAutoUpdate(true);
            break;
        case 1:
            viewAllDeliveryBills.setAutoUpdate(true);
            break;
            //        case 3:
            //            selectTableAndViewOrders.setAutoUpdate(true);
            //            break;
        default:
            break;
        }
    }

    @UiHandler("lblLogout")
    public void onLblLogoutClick(ClickEvent event){
        Cookies.removeCookie("comanda_peter_login_token");
        Window.Location.reload();
    }
}

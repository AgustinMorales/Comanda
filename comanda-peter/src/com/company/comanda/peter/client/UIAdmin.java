package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.DoubleBox;

public class UIAdmin extends Composite {

    private static UIAdminUiBinder uiBinder = GWT.create(UIAdminUiBinder.class);
    
    private final GUIServiceAsync GUIService = GWT
            .create(GUIService.class);
    
    @UiField TextBox tbRestaurantName;
    @UiField Button btnNewRestaurant;
    @UiField TextBox tbPassword;
    @UiField TextBox tbAddress;

    interface UIAdminUiBinder extends UiBinder<Widget, UIAdmin> {
    }

    public UIAdmin() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("btnNewRestaurant")
    void onBtnNewRestaurantClick(ClickEvent event) {
        GUIService.newRestaurant(tbRestaurantName.getText(), 
                tbPassword.getText(),tbAddress.getValue(),
                new AsyncCallback<Void>() {
                    
                    @Override
                    public void onSuccess(Void result) {
                        tbRestaurantName.setText("");
                        tbPassword.setText("");
                        tbAddress.setText("");
                        Window.alert("Creado con éxito");
                        
                    }
                    
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Error");
                        
                    }
                });
    }
}

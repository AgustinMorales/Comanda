package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.TextArea;

public class UIAdmin extends Composite {

    private static UIAdminUiBinder uiBinder = GWT.create(UIAdminUiBinder.class);
    
    private final GUIServiceAsync GUIService = GWT
            .create(GUIService.class);
    
    @UiField TextBox tbRestaurantName;
    @UiField Button btnNewRestaurant;
    @UiField TextBox tbPassword;
    @UiField TextBox tbAddress;
    @UiField TextBox tbLogin;
    @UiField TextArea taDescription;
    @UiField FormPanel restaurantDataFormPanel;

    interface UIAdminUiBinder extends UiBinder<Widget, UIAdmin> {
    }

    public UIAdmin() {
        initWidget(uiBinder.createAndBindUi(this));
        restaurantDataFormPanel.setEncoding(
                FormPanel.ENCODING_MULTIPART);
        restaurantDataFormPanel.setMethod(
                FormPanel.METHOD_POST);
    }

    @UiHandler("btnNewRestaurant")
    void onBtnNewRestaurantClick(ClickEvent event) {
        if(validate()){
            GUIService.getUploadUrlForNewRestaurant(
                    new AsyncCallback<String>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(
                            "Error while getting upload URL");
                    
                }

                @Override
                public void onSuccess(String result) {
                    restaurantDataFormPanel.setAction(result);
                    restaurantDataFormPanel.submit();
                    
                }
            });
        }
    }
    
    private boolean validate(){
        boolean result = true;
        if(tbRestaurantName.getText().length() == 0){
            Window.alert("Debe introducir un nombre");
            result = false;
        }
        else if(tbLogin.getText().length() == 0){
            Window.alert("Debe introducir un login");
            result = false;
        }
        else if(tbPassword.getText().length() == 0){
            Window.alert("Debe introducir una contrasegna");
            result = false;
        }
        else if(taDescription.getText().length() == 0){
            Window.alert("Debe introducir una descripci-on");
        }
        return result;
    }
}

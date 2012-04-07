package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class UIEditRestaurant extends Composite {

    private static UIEditRestaurantUiBinder uiBinder = 
            GWT.create(UIEditRestaurantUiBinder.class);
    
    private final GUIServiceAsync GUIService = GWT
            .create(GUIService.class);
    
    @UiField TextBox tbRestaurantName;
    @UiField Button btnNewRestaurant;
    @UiField TextBox tbPassword;
    @UiField TextBox tbAddress;
    @UiField TextBox tbLogin;
    @UiField TextArea taDescription;
    @UiField FormPanel restaurantDataFormPanel;
    
    private NewRestaurantHandler handler;

    interface UIEditRestaurantUiBinder extends UiBinder<Widget, UIEditRestaurant> {
    }

    public interface NewRestaurantHandler{
        void onNewRestaurant();
        void onCancel();
    };
    
    public void setNewRestaurantHandler(NewRestaurantHandler handler){
        this.handler = handler;
    }
    
    public UIEditRestaurant() {
        initWidget(uiBinder.createAndBindUi(this));
        restaurantDataFormPanel.setEncoding(
                FormPanel.ENCODING_MULTIPART);
        restaurantDataFormPanel.setMethod(
                FormPanel.METHOD_POST);
    }

    @UiHandler("btnNewRestaurant")
    void onBtnNewRestaurantClick(ClickEvent event) {
        btnNewRestaurant.setText("Procesando...");
        btnNewRestaurant.setEnabled(false);
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
                	if(result.contains("MacBook") == false){
                		restaurantDataFormPanel.setAction(result);
                	}
                	else{
                		restaurantDataFormPanel.setEncoding(FormPanel.ENCODING_URLENCODED);
                		restaurantDataFormPanel.setEncoding(FormPanel.METHOD_GET);
                		restaurantDataFormPanel.setAction("/newRestaurant");
                	}
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
    @UiHandler("restaurantDataFormPanel")
    void onRestaurantDataFormPanelSubmitComplete(SubmitCompleteEvent event) {
        restaurantDataFormPanel.reset();
        String result = event.getResults();
        if(result != null && result.contains("SUCCESS") == true){
            Window.alert("Creado con éxito");
            handler.onNewRestaurant();
        }
        else{
            Window.alert("Error");
        }
        btnNewRestaurant.setText("Aceptar");
        btnNewRestaurant.setEnabled(true);
    }
    
    public void reset(){
        restaurantDataFormPanel.reset();
    }
}

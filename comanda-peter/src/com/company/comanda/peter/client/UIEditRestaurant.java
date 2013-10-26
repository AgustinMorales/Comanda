package com.company.comanda.peter.client;

import com.google.gwt.ajaxloader.client.AjaxLoader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
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
import com.google.gwt.user.client.ui.DoubleBox;

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
    @UiField TextBox tbPhone;
    @UiField TextBox tbRestaurantKeyString;
    @UiField Button btnCancel;
    @UiField DoubleBox dbDeliveryCost;
    @UiField DoubleBox dbMinimumForDelivery;
    @UiField TextBox tbCopyFromRestKeyString;
    @UiField DoubleBox dbMaxDeliveryDistance;
    @UiField DoubleBox dbLatitude;
    @UiField DoubleBox dbLongitude;
    
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
        AjaxLoader.init();
        AjaxLoader.loadApi("maps", "2", new Runnable() { 
            public void run() { 
                //action to perform after api is loaded 
            } 
        }, null);
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
                	Geocoder geocoder = new Geocoder();
                	geocoder.getLatLng(tbAddress.getValue(), new LatLngCallback() {
                        
                        @Override
                        public void onSuccess(LatLng point) {
                            double latitude = point.getLatitude();
                            double longitude = point.getLongitude();
                            dbLatitude.setValue(latitude);
                            dbLongitude.setValue(longitude);
                            restaurantDataFormPanel.submit();
                        }
                        
                        @Override
                        public void onFailure() {
                            Window.alert("Error while geocoding");
                            btnNewRestaurant.setText("Aceptar");
                            btnNewRestaurant.setEnabled(true);
                        }
                    });
                    
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
        else if( (tbRestaurantKeyString.getValue() == null || 
                tbRestaurantKeyString.getValue().length() == 0) &&
                tbPassword.getText().length() == 0){
            Window.alert("Debe introducir una contraseña");
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
    
    public void setData(String[] data){
        tbRestaurantKeyString.setValue(data[0]);
        tbRestaurantName.setValue(data[1]);
        tbLogin.setValue(data[2]);
        tbAddress.setValue(data[3]);
        tbPhone.setValue(data[4]);
        taDescription.setValue(data[5]);
        float deliveryCost = 0;
        if(data[6] != null && data[6].length() > 0){
            deliveryCost = Float.parseFloat(data[6]);
        }
        float minimumForDelivery = 0;
        if(data[7] != null && data[7].length() > 0){
            minimumForDelivery = Float.parseFloat(data[7]);
        }
        double maxDeliveryDistance = 0.0;
        if(data[8] != null && data[8].length() > 0){
            maxDeliveryDistance = Double.parseDouble(data[8]);
        }
        dbDeliveryCost.setValue((double)deliveryCost);
        dbMinimumForDelivery.setValue((double)minimumForDelivery);
        dbMaxDeliveryDistance.setValue(maxDeliveryDistance);
    }
    
    public void reset(){
        restaurantDataFormPanel.reset();
    }
    @UiHandler("btnCancel")
    void onBtnCancelClick(ClickEvent event) {
        handler.onCancel();
    }
    
    public void setCopy(){
        tbCopyFromRestKeyString.setText(tbRestaurantKeyString.getText());
        tbRestaurantKeyString.setText("");
    }
}

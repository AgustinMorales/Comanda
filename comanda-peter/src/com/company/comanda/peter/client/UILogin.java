package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;

public class UILogin extends Composite {

    private final GUIServiceAsync GUIService = GWT
            .create(GUIService.class);
    
    private static UILoginUiBinder uiBinder = GWT.create(UILoginUiBinder.class);
    @UiField TextBox tbUsename;
    @UiField PasswordTextBox tbPassword;
    @UiField Button btnLogin;
    
    private DialogBox containerDB;

    interface UILoginUiBinder extends UiBinder<Widget, UILogin> {
    }

    public UILogin(DialogBox containerDB) {
        initWidget(uiBinder.createAndBindUi(this));
        this.containerDB = containerDB;
    }

    @UiHandler("btnLogin")
    void onBtnLoginClick(ClickEvent event) {
        doLogin();
    }
    
    
    private boolean validate(){
        return tbUsename.getText().length() >0 && tbPassword.getText().length() > 0;
    }
    
    private void doLogin(){
        if(validate()){
            doIndicateLogin();
            GUIService.login(tbUsename.getText(), tbPassword.getText(), new AsyncCallback<Boolean>() {
                
                @Override
                public void onSuccess(Boolean result) {
                    if(result){
                        RootLayoutPanel.get().clear();
                        RootLayoutPanel.get().add(new UIMain(tbUsename.getText()));
                        doNotIndicateLogin();
                        containerDB.hide();
                    }
                    else{
                        doNotIndicateLogin();
                        Window.alert("Login failed");
                    }
                    
                }
                
                @Override
                public void onFailure(Throwable caught) {
                    doNotIndicateLogin();
                    Window.alert("Error");
                    
                }
            });
        }
        else{
            Window.alert("Debe introducir nombre de usuario y contrasegna");
        }
    }
    
    private void doIndicateLogin(){
        btnLogin.setEnabled(false);
        btnLogin.setText("Accediendo...");
    }
    
    private void doNotIndicateLogin(){
        btnLogin.setText("Acceder");
        btnLogin.setEnabled(true);
    }
    @UiHandler("tbUsename")
    void onTbUsenameKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
            doLogin();
        }
    }
    @UiHandler("tbPassword")
    void onTbPasswordKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
            doLogin();
        }
    }
    
    public void focus(){
    	tbUsename.setFocus(true);
    }
}

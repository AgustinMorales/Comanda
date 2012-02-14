package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class UILogin extends Composite {

    private final GUIServiceAsync GUIService = GWT
            .create(GUIService.class);
    
    private static UILoginUiBinder uiBinder = GWT.create(UILoginUiBinder.class);
    @UiField TextBox tbUsename;
    @UiField PasswordTextBox tbPassword;
    @UiField Button btnLogin;
    @UiField Button btnAdmin;

    interface UILoginUiBinder extends UiBinder<Widget, UILogin> {
    }

    public UILogin() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("btnLogin")
    void onBtnLoginClick(ClickEvent event) {
        GUIService.login(tbUsename.getText(), tbPassword.getText(), new AsyncCallback<Boolean>() {
            
            @Override
            public void onSuccess(Boolean result) {
                if(result){
                    RootLayoutPanel.get().clear();
                    RootLayoutPanel.get().add(new UIMain());
                }
                else{
                    Window.alert("Login failed");
                }
                
            }
            
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error");
                
            }
        });
    }
    @UiHandler("btnAdmin")
    void onBtnAdminClick(ClickEvent event) {
        RootLayoutPanel.get().clear();
        RootLayoutPanel.get().add(new UIAdmin());
    }
}

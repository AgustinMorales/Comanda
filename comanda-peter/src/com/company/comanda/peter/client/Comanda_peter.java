package com.company.comanda.peter.client;

import com.company.comanda.peter.client.resources.Resources;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Comanda_peter implements EntryPoint {


    private final GUIServiceAsync GUIService = GWT
            .create(GUIService.class);


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        Resources.INSTANCE.comanda_css().ensureInjected();
        RootLayoutPanel.get().clear();
        String loginToken = Cookies.getCookie("comanda_peter_login_token");
        if(loginToken != null && loginToken.length() > 0){
            GUIService.login(loginToken, new AsyncCallback<String>() {
                
                @Override
                public void onSuccess(String result) {
                    if(result != null){
                        RootLayoutPanel.get().add(new UIMain(result));
                    }
                    else{
                        prepareForLogin();
                    }
                }
                
                @Override
                public void onFailure(Throwable caught) {
                    prepareForLogin();
                }
            });
        }
        else{
            prepareForLogin();
        }
    }


    private void prepareForLogin(){
        RootLayoutPanel.get().add(new UIBackground());
        final DialogBox dialogBox = new DialogBox();
        UILogin login = new UILogin(dialogBox);
        dialogBox.setWidget(login);
        dialogBox.center();
        login.focus();
        final HandlerRegistration registration = 
                com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {

                    @Override
                    public void onResize(ResizeEvent event) {
                        dialogBox.center();

                    }
                });
        dialogBox.addCloseHandler(new CloseHandler<PopupPanel>() {

            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                registration.removeHandler();

            }
        });
    }

}

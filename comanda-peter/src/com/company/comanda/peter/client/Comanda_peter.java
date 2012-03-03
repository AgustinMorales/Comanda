package com.company.comanda.peter.client;

import com.company.comanda.peter.client.resources.Resources;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Comanda_peter implements EntryPoint {


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        Resources.INSTANCE.comanda_css().ensureInjected();
        final DialogBox dialogBox = new DialogBox();
        UILogin login = new UILogin(dialogBox);
        dialogBox.setWidget(login);
        dialogBox.center();
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

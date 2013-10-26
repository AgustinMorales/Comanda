package com.company.comanda.peter.client;

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
public class Comanda_peterAdmin implements EntryPoint {


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        RootLayoutPanel.get().clear();
        RootLayoutPanel.get().add(new UIEditRestaurants());
    }
    
    
    
}

package com.company.comanda.peter.client;

import java.io.Serializable;

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

	public static class MenuItemVariant implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 4990857567588018658L;
		private String name;
		private float price;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public float getPrice() {
			return price;
		}
		public void setPrice(float price) {
			this.price = price;
		}
		
		
	}

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        Resources.INSTANCE.comanda_css().ensureInjected();
        RootLayoutPanel.get().clear();
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

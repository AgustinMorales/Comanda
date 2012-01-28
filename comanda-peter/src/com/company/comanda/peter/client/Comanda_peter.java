package com.company.comanda.peter.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Comanda_peter implements EntryPoint {


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        TabPanel tabPanel = new TabPanel();
        
        tabPanel.add(new EditMenuPanel(), "editMenu");
        tabPanel.add(new ViewOrdersPanel(), "viewOrders");
        
        tabPanel.selectTab(1);
        
        RootPanel.get().add(tabPanel);
    }
}

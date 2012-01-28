package com.company.comanda.peter.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Comanda_peter implements EntryPoint {


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        TabLayoutPanel tabPanel = new TabLayoutPanel(1.5, Unit.EM);
        tabPanel.add(new EditMenuPanel(), "editMenu");
        tabPanel.add(new ViewOrdersPanel(), "viewOrders");
        
        tabPanel.selectTab(0);
        
        RootLayoutPanel.get().add(tabPanel);
    }
}

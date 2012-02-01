package com.company.comanda.peter.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Comanda_peter implements EntryPoint {

//    private ViewOrdersPanel viewOrdersPanel

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
//        TabLayoutPanel tabPanel = new TabLayoutPanel(1.5, Unit.EM);
//        tabPanel.add(new EditMenuPanel(), "editMenu");
//        final ViewOrdersPanel viewOrdersPanel = new ViewOrdersPanel();
//        tabPanel.add(viewOrdersPanel, "viewOrders");
//        
//        tabPanel.selectTab(0);
//        
//        tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
//            
//            @Override
//            public void onSelection(SelectionEvent<Integer> event) {
//                if(event.getSelectedItem() == 1){
//                    viewOrdersPanel.setAutoUpdate(true);
//                }
//                else{
//                    viewOrdersPanel.setAutoUpdate(false);
//                }
//            }
//        });
//        RootLayoutPanel.get().add(tabPanel);
        
        
        RootLayoutPanel.get().add(new UIMain());
    }
    
    
}

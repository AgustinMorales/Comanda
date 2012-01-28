package com.company.comanda.peter.client;

import com.google.gwt.user.client.ui.VerticalPanel;

public class ViewOrdersPanel extends VerticalPanel 
implements Autoupdatable{

    private AllOrdersTable ordersTable;
    
    public ViewOrdersPanel(){
        ordersTable = new AllOrdersTable();
        add(ordersTable);
    }

    @Override
    public void setAutoUpdate(boolean value) {
        ordersTable.setAutoUpdate(value);
        
    }
    
    
}

package com.company.comanda.peter.client;

import com.google.gwt.user.client.ui.VerticalPanel;

public class ViewOrdersPanel extends VerticalPanel {

    
    public ViewOrdersPanel(){
        add(new AllOrdersTable());
    }
}

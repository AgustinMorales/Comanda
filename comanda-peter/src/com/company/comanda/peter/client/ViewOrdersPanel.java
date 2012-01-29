package com.company.comanda.peter.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ViewOrdersPanel extends HorizontalPanel 
implements Autoupdatable, TableSelectorListener{

    private AllOrdersTable ordersTable;
    private TableMapPanel tableMap;
    private VerticalPanel singleTablePanel;
    private SingleTableOrdersTable currentOrdersTable;
    private Label tableNameLabel;
    
    public ViewOrdersPanel(){
        tableMap = new TableMapPanel();
        tableMap.setTableSelectorListener(this);
        add(tableMap);
        singleTablePanel = new VerticalPanel();
        tableNameLabel = new Label("Please select a table on the map");
        singleTablePanel.add(tableNameLabel);
        singleTablePanel.setWidth("300px");
        add(singleTablePanel);
        ordersTable = new AllOrdersTable();
        add(ordersTable);
        ordersTable.setAutoUpdate(true);
    }

    @Override
    public void setAutoUpdate(boolean value) {
        ordersTable.setAutoUpdate(value);
        currentOrdersTable.setAutoUpdate(value);
    }

    @Override
    public void onNewTableSelected(String tableName) {
        if(currentOrdersTable != null){
            currentOrdersTable.setAutoUpdate(false);
            singleTablePanel.remove(currentOrdersTable);
        }
        tableNameLabel.setText("Table: " + tableName);
        currentOrdersTable = new SingleTableOrdersTable();
        currentOrdersTable.setTableName(tableName);
        singleTablePanel.add(currentOrdersTable);
        currentOrdersTable.setAutoUpdate(true);
    }
    
    
}

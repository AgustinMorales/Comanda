package com.company.comanda.peter.client;

import com.company.comanda.peter.client.AbstractTableUpdater.UpdateListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UIViewTableOrders extends Composite 
implements TableSelectorListener{

    public static final int PAGE_SIZE = 25;

	@UiField CellTable<String[]> odersTable;
	@UiField SimplePager odersPager;
	@UiField VerticalPanel ordersTableContainer;
	@UiField Label lblMessage;

	private OrdersTableUpdater ordersTableUpdater;
	private String selectedTable;
	

	@UiTemplate("UIViewAllOrders.ui.xml")
	interface UIViewAllOrders extends UiBinder<Widget, UIViewTableOrders> {}
	private static UIViewAllOrders uiBinder = GWT.create(UIViewAllOrders.class);


	public UIViewTableOrders() {
		initWidget(uiBinder.createAndBindUi(this));

		ordersTableUpdater = new OrdersTableUpdater(odersTable);
		
		ordersTableUpdater.setUpdateListener(new UpdateListener() {
            
            @Override
            public void onUpdate() {
                lblMessage.setVisible(false);
                ordersTableContainer.setVisible(true);
                
            }
        });

		odersTable.addColumn(new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[0];
            }
        }, "Pedido");
		
		odersPager.setDisplay(odersTable);
        odersPager.setPageSize(PAGE_SIZE);
        
        ordersTableContainer.setVisible(false);
        lblMessage.setText("Seleccione una mesa y los pedidos aparecerán aquí");
        lblMessage.setVisible(true);
	}

	public void setSelectedTable(String tableName){
	    this.selectedTable = tableName;
		ordersTableUpdater.setSelectedTable(selectedTable);
		ordersTableUpdater.setAutoUpdate(true);
	}
	
	public void setAutoUpdate(boolean value){
	    if(selectedTable != null){
	        ordersTableUpdater.setAutoUpdate(value);
	    }
	}

    @Override
    public void onNewTableSelected(String tableName) {
        ordersTableContainer.setVisible(false);
        lblMessage.setText("Cargando mesa " + tableName + "...");
        lblMessage.setVisible(true);
        setSelectedTable(tableName);
        
    }
}

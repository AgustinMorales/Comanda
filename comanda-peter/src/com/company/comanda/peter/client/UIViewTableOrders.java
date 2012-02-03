package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UIViewTableOrders extends Composite {

    public static final int PAGE_SIZE = 25;

	@UiField CellTable<String[]> odersTable;
	@UiField SimplePager odersPager;

	private OrdersTableUpdater ordersTableUpdater;
	

	@UiTemplate("UIViewAllOrders.ui.xml")
	interface UIViewAllOrders extends UiBinder<Widget, UIViewTableOrders> {}
	private static UIViewAllOrders uiBinder = GWT.create(UIViewAllOrders.class);


	public UIViewTableOrders() {
		initWidget(uiBinder.createAndBindUi(this));

		ordersTableUpdater = new OrdersTableUpdater(odersTable);

		odersTable.addColumn(new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[0];
            }
        }, "Pedido");
		
		odersPager.setDisplay(odersTable);
        odersPager.setPageSize(PAGE_SIZE);

	}

	public void setSelectedTable(String tableName){
		ordersTableUpdater.setSelectedTable(tableName);
		
	}
	
	public void setAutoUpdate(boolean value){
		ordersTableUpdater.setAutoUpdate(value);
	}
}

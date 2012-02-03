package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.OrderState;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;

public class UIViewAllOrders extends Composite {

	private static UIViewAllOrdersUiBinder uiBinder = GWT
			.create(UIViewAllOrdersUiBinder.class);
	@UiField CellTable<String[]> odersTable;
	@UiField SimplePager odersPager;
	
	private OrdersTableUpdater ordersTableUpdater;

	interface UIViewAllOrdersUiBinder extends UiBinder<Widget, UIViewAllOrders> {
	}

	public UIViewAllOrders() {
		initWidget(uiBinder.createAndBindUi(this));
		ordersTableUpdater = new OrdersTableUpdater(odersTable, 
				OrderState.ORDERED, null);
	}

	public void setAutoUpdate(boolean value){
		ordersTableUpdater.setAutoUpdate(value);
	}
}

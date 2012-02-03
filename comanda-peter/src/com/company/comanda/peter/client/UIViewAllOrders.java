package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.OrderState;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UIViewAllOrders extends Composite {

    
    public static final int PAGE_SIZE = 25;
    
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	private static UIViewAllOrdersUiBinder uiBinder = GWT
			.create(UIViewAllOrdersUiBinder.class);
	@UiField CellTable<String[]> odersTable;
	@UiField SimplePager odersPager;

	private OrdersTableUpdater ordersTableUpdater;

	interface UIViewAllOrdersUiBinder extends UiBinder<Widget, UIViewAllOrders> {
	}

	public UIViewAllOrders() {
		initWidget(uiBinder.createAndBindUi(this));

		// Add a text column to show the name.
		TextColumn<String[]> tableNameColumn = new TextColumn<String[]>() {
			@Override
			public String getValue(String[] object) {
				return object[1];
			}
		};
		odersTable.addColumn(tableNameColumn, "Mesa");

		TextColumn<String[]> orderNameColumn = new TextColumn<String[]>() {
			@Override
			public String getValue(String[] object) {
				return object[0];
			}
		};
		odersTable.addColumn(orderNameColumn, "Pedido");

		ButtonCell buttonCell = new ButtonCell(); 
		Column<String[], String> buttonColumn = new Column<String[], String>(buttonCell) { 
			@Override 
			public String getValue(String[] object) { 
				// The value to display in the button. 
				return "Accept"; 
			} 

		};

		buttonColumn.setFieldUpdater(new FieldUpdater<String[], String>(){ 
			public void update(int index, String[] object, String value) { 
				greetingService.acceptOrder(object[2], new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						ordersTableUpdater.refreshTable();

					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error");
					}
				});

			} 
		}); 

		odersTable.addColumn(buttonColumn, "Acciones");

		ordersTableUpdater = new OrdersTableUpdater(odersTable);
		ordersTableUpdater.setSelectedState(OrderState.ORDERED);

		odersPager.setDisplay(odersTable);
		odersPager.setPageSize(PAGE_SIZE);

	}

	public void setAutoUpdate(boolean value){
		ordersTableUpdater.setAutoUpdate(value);
	}
}

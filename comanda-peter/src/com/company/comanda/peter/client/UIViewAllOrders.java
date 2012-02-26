package com.company.comanda.peter.client;

import com.company.comanda.peter.client.resources.CellTableResource;
import com.company.comanda.peter.shared.OrderState;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UIViewAllOrders extends Composite {

    private static final CellTableResource resource = 
            GWT.create(CellTableResource.class); 
    
    public static final int PAGE_SIZE = 25;
    
	private final GUIServiceAsync greetingService = GWT
			.create(GUIService.class);

	private static UIViewAllOrdersUiBinder uiBinder = GWT
			.create(UIViewAllOrdersUiBinder.class);
	@UiField CellTable<String[]> odersTable;
	@UiField SimplePager odersPager;
	@UiField Label lblMessage;
	@UiField VerticalPanel ordersTableContainer;

	private OrdersTableUpdater ordersTableUpdater;

	interface UIViewAllOrdersUiBinder extends UiBinder<Widget, UIViewAllOrders> {
	}

	public UIViewAllOrders() {
		initWidget(uiBinder.createAndBindUi(this));

		ordersTableUpdater = new OrdersTableUpdater(odersTable);
        ordersTableUpdater.setSelectedState(OrderState.ORDERED);

        odersPager.setDisplay(odersTable);
        odersPager.setPageSize(PAGE_SIZE);

	}

	public void setAutoUpdate(boolean value){
		ordersTableUpdater.setAutoUpdate(value);
	}
	
	@UiFactory
	public CellTable makeCellTable(){
	    CellTable<String[]> result = new CellTable<String[]>(PAGE_SIZE, resource);
	    
	 // Add a text column to show the name.
        TextColumn<String[]> tableNameColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[1];
            }
        };
        
        
        result.addColumn(tableNameColumn, "Mesa");

        TextColumn<String[]> orderNameColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[0];
            }
        };
        result.addColumn(orderNameColumn, "Direccion");

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

        result.addColumn(buttonColumn, "Acciones");
        
        return result;
        
	}
}

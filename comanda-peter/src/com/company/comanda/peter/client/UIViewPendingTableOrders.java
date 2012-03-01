package com.company.comanda.peter.client;

import com.company.comanda.peter.client.AbstractTableUpdater.UpdateListener;
import com.company.comanda.peter.shared.OrderState;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UIViewPendingTableOrders extends Composite 
implements TableSelectorListener{

    public static final int PAGE_SIZE = 25;

	@UiField CellTable<String[]> odersTable;
	@UiField SimplePager odersPager;
	@UiField VerticalPanel ordersTableContainer;
	@UiField Label lblMessage;

	private OrdersTableUpdater ordersTableUpdater;
	private String selectedTable;
	
	private final GUIServiceAsync guiService = GWT
            .create(GUIService.class);

	@UiTemplate("UIViewAllOrders.ui.xml")
	interface UIViewAllOrders extends UiBinder<Widget, UIViewPendingTableOrders> {}
	private static UIViewAllOrders uiBinder = GWT.create(UIViewAllOrders.class);


	public UIViewPendingTableOrders() {
		initWidget(uiBinder.createAndBindUi(this));

		ordersTableUpdater = new OrdersTableUpdater(odersTable);
		
		ordersTableUpdater.setUpdateListener(new UpdateListener() {
            
            @Override
            public void onUpdate() {
                lblMessage.setVisible(false);
                ordersTableContainer.setVisible(true);
                
            }
        });

		ordersTableUpdater.setSelectedState(OrderState.ORDERED);
		
		odersTable.addColumn(new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[0];
            }
        }, "Pedido");
		
		
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
                guiService.acceptOrder(object[2], new AsyncCallback<Void>() {

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
    
    public void refresh(){
        ordersTableUpdater.refreshTable();
    }
}

package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OrdersTableUpdater extends AbstractTableUpdater{

	public OrdersTableUpdater(CellTable<String[]> ordersTable) {
        super(ordersTable);
    }

    private OrderState selectedState;
	private String selectedTable;

	
	public void setSelectedTable(String table){
		this.selectedTable = table;
	}
	
	public void setSelectedState(OrderState state){
		this.selectedState = state;
	}

    @Override
    protected void update(GUIServiceAsync service, int start, 
            int length,
            AsyncCallback<PagedResult<String[]>> callback) {
        service.getOrders(start, length, 
                selectedState, selectedTable, 
                callback);
        
    }
	
	
}

package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.BillType;
import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OrdersTableUpdater extends AbstractTableUpdater{

	public OrdersTableUpdater(CellTable<String[]> ordersTable) {
        super(ordersTable);
        selectedBillType = BillType.IN_RESTAURANT;
    }

    private OrderState selectedState;
	private String selectedTable;
	private String selectecBillKeyString;
	private BillType selectedBillType;

	
	public void setSelectedTable(String table){
		this.selectedTable = table;
	}
	
	public void setSelectedState(OrderState state){
		this.selectedState = state;
	}

	
    public void setSelectecBillKeyString(String selectecBillKeyString) {
        this.selectecBillKeyString = selectecBillKeyString;
    }

    
    
    public void setSelectedBillType(BillType selectedBillType) {
        this.selectedBillType = selectedBillType;
    }

    @Override
    protected void update(GUIServiceAsync service, int start, 
            int length,
            AsyncCallback<PagedResult<String[]>> callback) {
        service.getOrders(start, length, 
                selectedBillType,
                selectedState, selectedTable, 
                selectecBillKeyString,
                callback);
        
    }
	
	
}

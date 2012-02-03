package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.Constants;
import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

public class OrdersTableUpdater {
	
	private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);
	private CellTable<String[]> ordersTable;
	private AsyncDataProvider<String[]> ordersProvider;
	private OrderState selectedState;
	private String selectedTable;

	private MyTimer autoUpdateTimer;
    
	
	class MyTimer extends Timer{

        public void run(){
            refreshTable();
        }
    }
	
	public OrdersTableUpdater(CellTable<String[]> ordersTable,
			OrderState selectedState, String selectedTable){
		this.ordersTable = ordersTable;
		this.selectedState = selectedState;
		this.selectedTable = selectedTable;
	}
	
	public void setAutoUpdate(boolean value){
        if(ordersProvider == null){
            ordersProvider = new AsyncDataProvider<String[]>() {
                @Override
                protected void onRangeChanged(HasData<String[]> display) {
                    final int start = display.getVisibleRange().getStart();
                    int length = display.getVisibleRange().getLength();
                    AsyncCallback<PagedResult<String[]>> callback = new AsyncCallback<PagedResult<String[]>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(caught.getMessage());
                        }
                        @Override
                        public void onSuccess(PagedResult<String[]> result) {
                            updateRowData(start, result.getList());
                            updateRowCount(result.getTotal(), true);
                        }
                    };
                    // The remote service that should be implemented
                    greetingService.getOrders(start, length, 
                            selectedState, selectedTable, 
                            callback);
                }
            };
            ordersProvider.addDataDisplay(ordersTable);
        }
        if(autoUpdateTimer == null){
        	autoUpdateTimer = new MyTimer();
        }
        else{
        	autoUpdateTimer.cancel();
        }
        if(value){
            autoUpdateTimer.scheduleRepeating(Constants.AUTOUPDATE_PERIOD);
        }
    }
	
	protected void refreshTable(){
        Range range = ordersTable.getVisibleRange();
        RangeChangeEvent.fire(ordersTable, range);
    }
}

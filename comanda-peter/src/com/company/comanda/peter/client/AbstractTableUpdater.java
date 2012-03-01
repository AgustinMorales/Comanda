package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.Constants;
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

public abstract class AbstractTableUpdater {
	
    public interface UpdateListener{
        void onUpdate();
    }
	private final GUIServiceAsync greetingService = GWT
            .create(GUIService.class);
	private CellTable<String[]> ordersTable;
	private AsyncDataProvider<String[]> ordersProvider;

	private MyTimer autoUpdateTimer;
	
	private UpdateListener updateListener;
    
	
	class MyTimer extends Timer{

        public void run(){
            doRefresh();
        }
    }
	
	public AbstractTableUpdater(CellTable<String[]> ordersTable){
		this.ordersTable = ordersTable;
	}
	
	private void initProvider(){
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
                    update(greetingService, start, length, callback);
                }
            };
            ordersProvider.addDataDisplay(ordersTable);
        }
	}
	
	public void setAutoUpdate(boolean value){
        initProvider();
        if(autoUpdateTimer == null){
        	autoUpdateTimer = new MyTimer();
        }
        else{
        	autoUpdateTimer.cancel();
        }
        if(value){
            autoUpdateTimer.run();
            autoUpdateTimer.scheduleRepeating(Constants.AUTOUPDATE_PERIOD);
        }
    }
	
	public synchronized void refreshTable(){
        initProvider();
        doRefresh();
    }
	
	private void doRefresh(){
	    Range range = ordersTable.getVisibleRange();
        RangeChangeEvent.fire(ordersTable, range);
        if(updateListener != null){
            updateListener.onUpdate();
        }
	}
	public synchronized void setUpdateListener(UpdateListener listener){
	    this.updateListener = listener;
	}
	
	protected abstract void update(GUIServiceAsync service,
	        int start, int length, AsyncCallback<PagedResult<String[]>> callback);
}

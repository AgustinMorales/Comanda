package com.company.comanda.peter.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

public class ComandaPeterViewOrders implements EntryPoint {

	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	
	@Override
	public void onModuleLoad() {
		
		VerticalPanel panel = new VerticalPanel();
		
		RootPanel rootPanel = RootPanel.get();
		rootPanel.add(panel);
		
		final CellTable<String> cellTable = new CellTable<String>();
		panel.add(cellTable);
		cellTable.setSize("213px", "150px");

		// Add a text column to show the name.
	    TextColumn<String> nameColumn = new TextColumn<String>() {
	      @Override
	      public String getValue(String object) {
	        return object;
	      }
	    };
	    cellTable.addColumn(nameColumn, "Name");
		
	    final AsyncDataProvider<String> provider = new AsyncDataProvider<String>() {
	        @Override
	        protected void onRangeChanged(HasData<String> display) {
	          final int start = display.getVisibleRange().getStart();
	          int length = display.getVisibleRange().getLength();
//	          AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
//	              @Override
//	              public void onFailure(Throwable caught) {
//	                Window.alert(caught.getMessage());
//	              }
//	              @Override
//	              public void onSuccess(List<String> result) {
//	                updateRowData(start, result);
//	              }
//	            };
//	            // The remote service that should be implemented
//	            greetingService.getOrders(start, length, callback);
	        }
	      };
	      provider.addDataDisplay(cellTable);
	      provider.updateRowCount(200, false);
	      AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
	      
	      class MyTimer extends Timer{
	    	  
	    	  public void run(){
	    		  Range range = cellTable.getVisibleRange();
	    		  RangeChangeEvent.fire(cellTable, range);
	    	  }
	      }
	      
	      Timer timer = new MyTimer();
	      
	     timer.scheduleRepeating(3000);
	}
}

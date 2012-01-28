package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

public class ViewOrdersPanel extends VerticalPanel {

    private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);
    
    public ViewOrdersPanel(){
        final CellTable<String> ordersTable = new CellTable<String>();
        add(ordersTable);
        ordersTable.setSize("213px", "300px");

        SimplePager ordersPager = new SimplePager();
        ordersPager.setDisplay(ordersTable);
        add(ordersPager);
        ordersPager.setPageSize(8);

        // Add a text column to show the name.
        TextColumn<String> orderNameColumn = new TextColumn<String>() {
            @Override
            public String getValue(String object) {
                return object;
            }
        };
        ordersTable.addColumn(orderNameColumn, "Pending orders");

        final AsyncDataProvider<String> ordersProvider = new AsyncDataProvider<String>() {
            @Override
            protected void onRangeChanged(HasData<String> display) {
                final int start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                AsyncCallback<PagedResult<String>> callback = new AsyncCallback<PagedResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }
                    @Override
                    public void onSuccess(PagedResult<String> result) {
                        updateRowData(start, result.getList());
                        updateRowCount(result.getTotal(), true);
                    }
                };
                // The remote service that should be implemented
                greetingService.getOrders(start, length, callback);
            }
        };
        ordersProvider.addDataDisplay(ordersTable);
        AsyncHandler ordersColumnSortHandler = new AsyncHandler(ordersTable);

        class MyTimer extends Timer{

            public void run(){
                Range range = ordersTable.getVisibleRange();
                RangeChangeEvent.fire(ordersTable, range);
            }
        }

        Timer timer = new MyTimer();

        timer.scheduleRepeating(1000);
    }
}

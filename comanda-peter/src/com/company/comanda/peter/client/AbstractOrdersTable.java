package com.company.comanda.peter.client;

import java.util.List;

import com.company.comanda.peter.shared.Constants;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

public abstract class AbstractOrdersTable extends VerticalPanel {

    private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);
    
    private MyTimer autoUpdateTimer = new MyTimer();
    private CellTable<String[]> ordersTable;
    
    public class ColumnDefinition{
        
        private String name;
        private TextColumn<String[]> textColumn;
        
        public ColumnDefinition(String name, TextColumn<String[]> textColumn){
            this.name = name;
            this.textColumn = textColumn;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public TextColumn<String[]> getTextColumn() {
            return textColumn;
        }

        public void setTextColumn(TextColumn<String[]> textColumn) {
            this.textColumn = textColumn;
        }
        
        
    }
    
    public AbstractOrdersTable(){
        ordersTable = new CellTable<String[]>();
        add(ordersTable);
        ordersTable.setSize("213px", "300px");

        SimplePager ordersPager = new SimplePager();
        ordersPager.setDisplay(ordersTable);
        add(ordersPager);
        ordersPager.setPageSize(8);

        List<ColumnDefinition> columns = getColumns();
        
//        // Add a text column to show the name.
//        TextColumn<String> orderNameColumn = new TextColumn<String>() {
//            @Override
//            public String getValue(String object) {
//                return object;
//            }
//        };
        
        for(ColumnDefinition columnDefinition : columns){
            ordersTable.addColumn(columnDefinition.getTextColumn(), columnDefinition.getName());
        }

        final AsyncDataProvider<String[]> ordersProvider = new AsyncDataProvider<String[]>() {
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
                greetingService.getOrders(start, length, callback);
            }
        };
        ordersProvider.addDataDisplay(ordersTable);
        AsyncHandler ordersColumnSortHandler = new AsyncHandler(ordersTable);

        
    }
    
    class MyTimer extends Timer{

        public void run(){
            Range range = ordersTable.getVisibleRange();
            RangeChangeEvent.fire(ordersTable, range);
        }
    }
    
    protected abstract List<ColumnDefinition> getColumns();
    
    public void setAutoUpdate(boolean value){
        autoUpdateTimer.cancel();
        if(value){
            autoUpdateTimer.scheduleRepeating(Constants.AUTOUPDATE_PERIOD);
        }
    }
}

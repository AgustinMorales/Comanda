package com.company.comanda.peter.client;

import com.company.comanda.peter.client.NewMenuItemPanel.NewMenuItemHandler;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

public class EditMenuPanel extends VerticalPanel {

    private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);
    
    public EditMenuPanel(){
        final Button addMenuItemButton = new Button("Add menu item");
        

        // We can add style names to widgets
        addMenuItemButton.addStyleName("sendButton");

        add(addMenuItemButton);
        
        final CellTable<String[]> cellTable = new CellTable<String[]>();
        add(cellTable);
        cellTable.setSize("213px", "300px");

        SimplePager menuItemsPager = new SimplePager();
        menuItemsPager.setDisplay(cellTable);
        add(menuItemsPager);
        menuItemsPager.setPageSize(8);
        
        // Add a text column to show the name.
        TextColumn<String[]> nameColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[1];
            }
        };
        cellTable.addColumn(nameColumn, "Name");
        
        TextColumn<String[]> priceColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[2];
            }
        };
        cellTable.addColumn(priceColumn, "Price");

        AsyncDataProvider<String[]> provider = new AsyncDataProvider<String[]>() {
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
                greetingService.getMenuItems(start, length, callback);
            }
        };
        provider.addDataDisplay(cellTable);
        AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
        cellTable.addColumnSortHandler(columnSortHandler);


        // Create the popup dialog box
        
        final DialogBox dialogBox = new DialogBox();
        
        NewMenuItemPanel newMenuItemPanel = new NewMenuItemPanel();
        dialogBox.setWidget(newMenuItemPanel);
        newMenuItemPanel.setNewMenuItemHandler(new NewMenuItemHandler() {
            
            @Override
            public void onNewMenuItem() {
                Range range = cellTable.getVisibleRange();
                RangeChangeEvent.fire(cellTable, range);
                dialogBox.hide();
                
            }

            @Override
            public void onCancel() {
                dialogBox.hide();
                
            }
        });



        

        // Add a handler to send the name to the server
        addMenuItemButton.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                dialogBox.show();
                dialogBox.center();
                
            }
        });
    }
}

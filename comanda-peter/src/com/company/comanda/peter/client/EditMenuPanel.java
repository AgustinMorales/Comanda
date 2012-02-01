package com.company.comanda.peter.client;

import java.util.Set;

import com.company.comanda.peter.client.NewMenuItemPanel.NewMenuItemHandler;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class EditMenuPanel extends VerticalPanel {

    private CellTable<String[]> cellTable;
    
    private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);

    public EditMenuPanel(){
        final DialogBox dialogBox = new DialogBox();

        final NewMenuItemPanel newMenuItemPanel = new NewMenuItemPanel();
        dialogBox.setWidget(newMenuItemPanel);
        cellTable = new CellTable<String[]>();
        
        cellTable.setSize("213px", "300px");

        
        
        final MultiSelectionModel<String[]> selectionModel = new MultiSelectionModel<String[]>();
        cellTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<String[]> createCheckboxManager());
        Column<String[], Boolean> checkColumn = new Column<String[], Boolean>(
                new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(String[] object) {
                // Get the value from the selection model.
                return selectionModel.isSelected(object);
            }
        };
        
        cellTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        cellTable.setColumnWidth(checkColumn, 40, Unit.PX);
        
        
        // Add a text column to show the name.
        TextColumn<String[]> nameColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[2];
            }
        };
        cellTable.addColumn(nameColumn, "Name");
        
        TextColumn<String[]> priceColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[3];
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

        newMenuItemPanel.setNewMenuItemHandler(new NewMenuItemHandler() {

            @Override
            public void onNewMenuItem() {
                selectionModel.clear();
                refreshTable();
                dialogBox.hide();

            }

            @Override
            public void onCancel() {
                dialogBox.hide();

            }
        });

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        horizontalPanel.setSpacing(5);
        add(horizontalPanel);
        final Button addMenuItemButton = new Button("Add menu item");
        horizontalPanel.add(addMenuItemButton);


        // We can add style names to widgets

        final Button btnEdit = new Button("Edit");
        horizontalPanel.add(btnEdit);
        
        btnEdit.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                Set<String[]> selectedSet = selectionModel.getSelectedSet();
                if(selectedSet.size() == 1){
                    newMenuItemPanel.reset();
                    newMenuItemPanel.setData(selectedSet.iterator().next());
                    dialogBox.show();
                    dialogBox.center();
                }
            }
        });

        final Button btnDelete = new Button("Delete");
        horizontalPanel.add(btnDelete);
        
        btnDelete.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                Set<String[]> selectedSet = selectionModel.getSelectedSet();
                int size = selectedSet.size();
                long[] keysToDelete = new long[size];
                int counter = 0;
                for(String[] item : selectedSet){
                    keysToDelete[counter] = Long.parseLong(item[0]);
                    counter++;
                };
                greetingService.deleteMenuItems(keysToDelete, new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Error while deleting");
                        
                    }

                    @Override
                    public void onSuccess(Void result) {
                        refreshTable();
                        
                    }
                });
            }
        });

        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);



        // Add a handler to send the name to the server
        addMenuItemButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newMenuItemPanel.reset();
                dialogBox.show();
                dialogBox.center();

            }
        });


        add(cellTable);
        
        SimplePager menuItemsPager = new SimplePager();
        menuItemsPager.setDisplay(cellTable);
        add(menuItemsPager);
        menuItemsPager.setPageSize(8);
        // Create the popup dialog box

        selectionModel.addSelectionChangeHandler(new Handler() {
            
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                int size = selectionModel.getSelectedSet().size();
                switch (size) {
                case 0:
                    btnDelete.setEnabled(false);
                    btnEdit.setEnabled(false);
                    break;
                case 1:
                    btnDelete.setEnabled(true);
                    btnEdit.setEnabled(true);
                    break;
                default:
                    btnDelete.setEnabled(true);
                    btnEdit.setEnabled(false);
                    break;
                }
                
            }
        });
    }

    
    public void refreshTable(){
        Range range = cellTable.getVisibleRange();
        RangeChangeEvent.fire(cellTable, range);
    }
}

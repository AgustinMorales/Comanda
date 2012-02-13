package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

public class UIMenuTable extends Composite {

    public static final int PAGE_SIZE = 20;
    
    @UiField SimplePager menuItemsPager;
    @UiField CellTable<String[]> menuItemsTable;
    private MultiSelectionModel<String[]> selectionModel;
    
    
    private static UIMenuTableUiBinder uiBinder = GWT
            .create(UIMenuTableUiBinder.class);

    interface UIMenuTableUiBinder extends UiBinder<Widget, UIMenuTable> {
    }

    public UIMenuTable() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    void setSelectionModel(MultiSelectionModel<String[]> selectionModel) {
        this.selectionModel = selectionModel;
    }
    
    void setProvider(AsyncDataProvider<String[]> provider){
        provider.addDataDisplay(menuItemsTable);
    }
    
    void configureMenuItemsTable(){
        menuItemsTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<String[]> createCheckboxManager());
        Column<String[], Boolean> checkColumn = new Column<String[], Boolean>(
                new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(String[] object) {
                // Get the value from the selection model.
                return selectionModel.isSelected(object);
            }
        };
        
        menuItemsTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        menuItemsTable.setColumnWidth(checkColumn, 40, Unit.PX);
        
        
        // Add a text column to show the name.
        TextColumn<String[]> nameColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[2];
            }
        };
        menuItemsTable.addColumn(nameColumn, "Name");
        
        TextColumn<String[]> priceColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[3];
            }
        };
        menuItemsTable.addColumn(priceColumn, "Price");

        
        
        
        menuItemsPager.setDisplay(menuItemsTable);
        menuItemsPager.setPageSize(PAGE_SIZE);
        
    }
    
}

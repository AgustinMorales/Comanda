package com.company.comanda.peter.client;

import java.util.List;

import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

public class UIEditTables extends Composite {

    private static UIEditTablesUiBinder uiBinder = GWT
            .create(UIEditTablesUiBinder.class);
    
    private final GUIServiceAsync GUIService = GWT
            .create(GUIService.class);
    
    
    @UiField Button btnAddTable;
    @UiField CellTable<String[]> tablesCellTable = new CellTable<String[]>();
    @UiField TextBox tbNewTableName;

    interface UIEditTablesUiBinder extends UiBinder<Widget, UIEditTables> {
    }

    public UIEditTables() {
        initWidget(uiBinder.createAndBindUi(this));
        configureTable();
    }

    void configureTable(){
        
        
        // Add a text column to show the name.
        TextColumn<String[]> nameColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[1];
            }
        };
        tablesCellTable.addColumn(nameColumn, "Nombre");
        
        // Add a text column to show the code.
        TextColumn<String[]> codeColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[2];
            }
        };
        tablesCellTable.addColumn(codeColumn, "QRCode");
        
        AsyncDataProvider<String[]> provider = new AsyncDataProvider<String[]>() {
            @Override
            protected void onRangeChanged(HasData<String[]> display) {
                final int start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }
                    @Override
                    public void onSuccess(List<String[]> result) {
                        updateRowData(start, result);
                        updateRowCount(result.size(), true);
                    }
                };
                // The remote service that should be implemented
                GUIService.getTables(callback);
            }
        };
        
        provider.addDataDisplay(tablesCellTable);
        
    }
    
    @UiHandler("btnAddTable")
    void onBtnAddTableClick(ClickEvent event) {
        btnAddTable.setEnabled(false);
        GUIService.addTable(tbNewTableName.getText(), new AsyncCallback<Void>() {
            
            @Override
            public void onSuccess(Void result) {
                refreshTable();
                btnAddTable.setEnabled(true);
            }
            
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error");
                btnAddTable.setEnabled(true);
            }
        });
    }
    
    public void refreshTable(){
        Range range = tablesCellTable.getVisibleRange();
        RangeChangeEvent.fire(tablesCellTable, range);
    }
}

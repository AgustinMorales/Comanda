package com.company.comanda.peter.client;

import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class TableSelectionList extends Composite implements TableSelector{

    private static TableSelectionListUiBinder uiBinder = GWT
            .create(TableSelectionListUiBinder.class);
    @UiField(provided=true) CellTable<String[]> cellTable = new CellTable<String[]>();

    private TableSelectorListener tableSelectorListener;

    private final GUIServiceAsync GUIService = GWT
            .create(GUIService.class);

    interface TableSelectionListUiBinder extends
    UiBinder<Widget, TableSelectionList> {
    }

    public TableSelectionList() {
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
        cellTable.addColumn(nameColumn, "Nombre");


        ButtonCell buttonCell = new ButtonCell(); 
        Column<String[], String> buttonColumn = new Column<String[], String>(buttonCell) { 
            @Override 
            public String getValue(String[] object) { 
                // The value to display in the button. 
                return "Seleccionar"; 
            } 

        };

        buttonColumn.setFieldUpdater(new FieldUpdater<String[], String>(){ 
            public void update(int index, String[] object, String value) { 
                tableSelectorListener.onNewTableSelected(object[1]);

            } 
        }); 

        cellTable.addColumn(buttonColumn, "Acciones");

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


        provider.addDataDisplay(cellTable);

    }

    @Override
    public void setTableSelectorListener(TableSelectorListener listener) {
        this.tableSelectorListener = listener;
    }

}

package com.company.comanda.peter.client;

import java.util.ArrayList;
import java.util.List;

import com.company.comanda.peter.shared.OrderState;
import com.google.gwt.user.cellview.client.TextColumn;

public class SingleTableOrdersTable extends AbstractOrdersTable {

    private String table;
    
    
    
    public void setTableName(String table){
        this.table = table;
    }
    
    @Override
    protected List<ColumnDefinition> getColumns() {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>(2);
        
        
        TextColumn<String[]> orderNameColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[0];
            }
        };
        result.add(new ColumnDefinition("Item", orderNameColumn));
        
        
        return result;
    }

    @Override
    protected OrderState getSelectedState() {
        return null;
    }

    @Override
    protected String getSelectedTable() {
        return this.table;
    }

}

package com.company.comanda.peter.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;

public class AllOrdersTable extends AbstractOrdersTable 
implements Autoupdatable{

    @Override
    protected List<ColumnDefinition> getColumns() {
      List<ColumnDefinition> result = new ArrayList<ColumnDefinition>(2);
      // Add a text column to show the name.
      TextColumn<String[]> tableNameColumn = new TextColumn<String[]>() {
          @Override
          public String getValue(String[] object) {
              return object[1];
          }
      };
      result.add(new ColumnDefinition("Table", tableNameColumn));
      
      TextColumn<String[]> orderNameColumn = new TextColumn<String[]>() {
          @Override
          public String getValue(String[] object) {
              return object[0];
          }
      };
      result.add(new ColumnDefinition("Item", orderNameColumn));

      return result;
    }

}

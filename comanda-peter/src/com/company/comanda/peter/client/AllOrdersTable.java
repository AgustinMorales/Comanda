package com.company.comanda.peter.client;

import java.util.ArrayList;
import java.util.List;

import com.company.comanda.peter.shared.OrderState;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AllOrdersTable extends AbstractOrdersTable 
implements Autoupdatable{

    private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);
    
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
      
      ButtonCell buttonCell = new ButtonCell(); 
      Column<String[], String> buttonColumn = new Column<String[], String>(buttonCell) { 
        @Override 
        public String getValue(String[] object) { 
          // The value to display in the button. 
          return "Accept"; 
        } 

      };

      buttonColumn.setFieldUpdater(new FieldUpdater<String[], String>(){ 
          public void update(int index, String[] object, String value) { 
              greetingService.acceptOrder(object[2], new AsyncCallback<Void>() {

                  @Override
                  public void onSuccess(Void result) {
                      refreshTable();

                  }

                  @Override
                  public void onFailure(Throwable caught) {
                      Window.alert("Error");
                  }
              });
              
          } 
      }); 

      result.add(new ColumnDefinition("Actions", buttonColumn));
      return result;
    }

    protected OrderState getSelectedState(){
        return OrderState.ORDERED;
    }
    
    protected String getSelectedTable(){
        return null;
    }
}

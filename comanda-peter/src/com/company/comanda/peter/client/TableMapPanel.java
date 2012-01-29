package com.company.comanda.peter.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TableMapPanel extends VerticalPanel implements TableSelector{
    
    private TableSelectorListener tableSelectorListener;
    
    public TableMapPanel() {
        
        AbsolutePanel panel = new AbsolutePanel();
        panel.setSize("400px", "800px");
        Button btnNewButton = new Button("1");
        panel.add(btnNewButton, 22, 25);
        btnNewButton.setSize("64px", "52px");
        
        Button btnTable = new Button("2");
        panel.add(btnTable, 22, 83);
        btnTable.setSize("64px", "59px");
        
        Button btnTable_1 = new Button("3");
        panel.add(btnTable_1, 22, 148);
        btnTable_1.setSize("64px", "59px");
        
        Button btnTable_2 = new Button("4");
        panel.add(btnTable_2, 22, 213);
        btnTable_2.setSize("64px", "59px");
        
        Button btnTable_3 = new Button("5");
        panel.add(btnTable_3, 22, 278);
        btnTable_3.setSize("64px", "59px");
        
        Button btnTable_4 = new Button("6");
        panel.add(btnTable_4, 22, 343);
        btnTable_4.setSize("64px", "59px");
        
        Button btnTable_5 = new Button("7");
        panel.add(btnTable_5, 110, 343);
        btnTable_5.setSize("64px", "59px");
        
        Button btnTable_6 = new Button("8");
        panel.add(btnTable_6, 206, 278);
        btnTable_6.setSize("64px", "59px");
        
        Button button_6 = new Button("9");
        panel.add(button_6, 121, 213);
        button_6.setSize("64px", "59px");
        
        Button button_7 = new Button("10");
        panel.add(button_7, 206, 148);
        button_7.setSize("64px", "59px");
        
        Button button_8 = new Button("11");
        panel.add(button_8, 206, 83);
        button_8.setSize("64px", "59px");
        
        Button[] buttons = new Button[]{
                btnNewButton,
                btnTable,
                btnTable_1,
                btnTable_2,
                btnTable_3,
                btnTable_4,
                btnTable_5,
                btnTable_6,
                button_6,
                button_7,
                button_8,
        };
        
        for(int i=0;i< buttons.length;i++){
            Button button = buttons[i];
            final int index = i;
            button.addClickHandler(new ClickHandler() {
                
                @Override
                public void onClick(ClickEvent event) {
                    tableSelectorListener.onNewTableSelected("" + (index+1));
                    
                }
            });
        }
        add(panel);
    }

    @Override
    public void setTableSelectorListener(
            TableSelectorListener listener) {
        this.tableSelectorListener = listener;
    }
}

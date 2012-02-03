package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;

public class UISelectAndViewTableOrders extends Composite {

	private static UISelectAndViewTableOrdersUiBinder uiBinder = GWT
			.create(UISelectAndViewTableOrdersUiBinder.class);
	@UiField UITableMap tableMap;
	@UiField UIViewTableOrders tableOrders;
	private boolean autoupdate;

	interface UISelectAndViewTableOrdersUiBinder extends
			UiBinder<Widget, UISelectAndViewTableOrders> {
	}

	public UISelectAndViewTableOrders() {
		initWidget(uiBinder.createAndBindUi(this));
		tableMap.setTableSelectorListener(new TableSelectorListener() {
            
            @Override
            public void onNewTableSelected(String tableName) {
                tableOrders.setSelectedTable(tableName);
                tableOrders.setAutoUpdate(autoupdate);
                
            }
        });
	}

	public void setAutoUpdate(boolean value){
	    this.autoupdate = value;
	    tableOrders.setAutoUpdate(value);
	}

}

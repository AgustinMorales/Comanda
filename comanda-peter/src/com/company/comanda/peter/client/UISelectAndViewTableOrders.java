package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UISelectAndViewTableOrders extends Composite {

	private static UISelectAndViewTableOrdersUiBinder uiBinder = GWT
			.create(UISelectAndViewTableOrdersUiBinder.class);
	@UiField UITableMap tableMap;
	@UiField UIViewPendingTableOrders tableOrders;
	@UiField UIViewAcceptedTableOrders tableAcceptedOrders;
	

	interface UISelectAndViewTableOrdersUiBinder extends
			UiBinder<Widget, UISelectAndViewTableOrders> {
	}

	public UISelectAndViewTableOrders() {
		initWidget(uiBinder.createAndBindUi(this));
		tableMap.setTableSelectorListener(new TableSelectorListener() {
            
            @Override
            public void onNewTableSelected(String tableName) {
                tableOrders.onNewTableSelected(tableName);
                tableAcceptedOrders.onNewTableSelected(tableName);
            }
        });
	}

	public void setAutoUpdate(boolean value){
	    tableOrders.setAutoUpdate(value);
	}

}

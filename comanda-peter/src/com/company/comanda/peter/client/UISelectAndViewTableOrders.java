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
	@UiField UIViewTableOrders tableOrders;

	interface UISelectAndViewTableOrdersUiBinder extends
			UiBinder<Widget, UISelectAndViewTableOrders> {
	}

	public UISelectAndViewTableOrders() {
		initWidget(uiBinder.createAndBindUi(this));
		tableMap.setTableSelectorListener(tableOrders);
	}

	public void setAutoUpdate(boolean value){
	    tableOrders.setAutoUpdate(value);
	}

}

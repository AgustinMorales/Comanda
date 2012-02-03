package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;

public class UITableMap extends Composite implements TableSelector{

	private static UITableMapUiBinder uiBinder = GWT
			.create(UITableMapUiBinder.class);
	@UiField Button btnTable1;
	@UiField Button btnTable2;
	@UiField Button btnTable3;
	@UiField Button btnTable4;
	@UiField Button btnTable5;
	@UiField Button btnTable6;
	@UiField Button btnTable7;
	@UiField Button btnTable8;
	@UiField Button btnTable9;
	
	private TableSelectorListener tableSelectorListener;

	interface UITableMapUiBinder extends UiBinder<Widget, UITableMap> {
	}

	public UITableMap() {
		initWidget(uiBinder.createAndBindUi(this));
		Button[] buttons = new Button[]{
				btnTable1,
				btnTable2,
				btnTable3,
				btnTable4,
				btnTable5,
				btnTable6,
				btnTable7,
				btnTable8,
				btnTable9,
		};
		for(int i=0;i<buttons.length;i++){
			final String tableName = "" + i;
			buttons[i].addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					tableSelectorListener.onNewTableSelected(tableName);
					
				}
			});
		}
	}

	@Override
	public void setTableSelectorListener(TableSelectorListener listener) {
		this.tableSelectorListener = listener;
	}

}

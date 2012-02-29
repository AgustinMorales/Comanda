package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BillsTableUpdater extends AbstractTableUpdater {

    
    public BillsTableUpdater(CellTable<String[]> ordersTable) {
        super(ordersTable);
    }

    @Override
    protected void update(GUIServiceAsync service, int start, int length,
            AsyncCallback<PagedResult<String[]>> callback) {

    }

}

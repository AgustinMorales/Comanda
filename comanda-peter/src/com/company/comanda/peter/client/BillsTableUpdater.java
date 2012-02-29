package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.BillState;
import com.company.comanda.peter.shared.BillType;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BillsTableUpdater extends AbstractTableUpdater {

    
    private BillState state;
    private BillType type;
    
    public BillsTableUpdater(CellTable<String[]> ordersTable) {
        super(ordersTable);
    }

    @Override
    protected void update(GUIServiceAsync service, int start, int length,
            AsyncCallback<PagedResult<String[]>> callback) {
        service.getBills(start, length, state, type, callback);
    }

    public BillState getState() {
        return state;
    }

    public void setState(BillState state) {
        this.state = state;
    }

    public BillType getType() {
        return type;
    }

    public void setType(BillType type) {
        this.type = type;
    }

    
}

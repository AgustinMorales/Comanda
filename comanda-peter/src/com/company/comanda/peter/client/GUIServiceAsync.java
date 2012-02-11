package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GUIServiceAsync {
	void getMenuItems(int start, int length, AsyncCallback<PagedResult<String[]>> callback);
	void getOrders(int start, int length, OrderState state, String tableName,
	        AsyncCallback<PagedResult<String[]>> callback);
	void getUploadUrl(AsyncCallback<String> callback);
	void acceptOrder(String orderKey, AsyncCallback<Void> callback);
	void deleteMenuItems(long[] keyIds, AsyncCallback<Void> callback);
	void login(String username, String password, AsyncCallback<Boolean> callback);
}

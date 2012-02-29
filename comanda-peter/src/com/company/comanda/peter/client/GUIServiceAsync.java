package com.company.comanda.peter.client;

import java.util.List;

import com.company.comanda.peter.shared.BillState;
import com.company.comanda.peter.shared.BillType;
import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GUIServiceAsync {
	void getMenuItems(int start, int length, Long categoryId,
	        AsyncCallback<PagedResult<String[]>> callback);
	void getOrders(int start, int length, OrderState state, String tableName,
	        AsyncCallback<PagedResult<String[]>> callback);
	void getUploadUrl(AsyncCallback<String> callback);
	void acceptOrder(String orderKey, AsyncCallback<Void> callback);
	void deleteMenuItems(long[] keyIds, AsyncCallback<Void> callback);
	void login(String username, String password, AsyncCallback<Boolean> callback);
	void newRestaurant(String name, String password,
	        double latitude, double longitude, AsyncCallback<Void> callback);
	void addTable(String tablename, AsyncCallback<Void> callback);
	void getTables(AsyncCallback<List<String[]>> callback);
	void getCategories(AsyncCallback<List<String[]>> callback);
	void getBills(int start, int length, 
	        BillState state, BillType type, AsyncCallback<PagedResult<String[]>> callback);
}

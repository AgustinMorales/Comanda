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
	void getOrders(int start, int length, BillType billType, OrderState state, 
	        String tableKeyString,
	        String billKeyString,
	        AsyncCallback<PagedResult<String[]>> callback);
	void getUploadUrl(AsyncCallback<String> callback);
	void acceptOrder(String orderKey, AsyncCallback<Void> callback);
	void deleteMenuItems(long[] keyIds, AsyncCallback<Void> callback);
	void login(String username, String password, AsyncCallback<Boolean> callback);
	void addTable(String tablename, AsyncCallback<Void> callback);
	void getTables(AsyncCallback<List<String[]>> callback);
	void getCategories(AsyncCallback<List<String[]>> callback);
	void addOrModifyCategory(Long id, String name, AsyncCallback<Void> callback);
	void removeCategory(long id, AsyncCallback<Void> callback);
	void getBills(int start, int length, 
	        BillState state, BillType type, AsyncCallback<PagedResult<String[]>> callback);
	void changeBillState(String billKeyString, BillState newState, 
	        Integer deliveryDelay, AsyncCallback<Void> callback);
	void getUploadUrlForNewRestaurant(AsyncCallback<String> callback);
}

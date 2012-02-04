package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	void greetServer(String name) throws IllegalArgumentException;
	PagedResult<String[]> getMenuItems(int start, int length);
	PagedResult<String[]> getOrders(int start, int length, OrderState state, 
	        String tableName);
	public String getUploadUrl();
	void acceptOrder(String orderKey);
	void deleteMenuItems(long[] keyIds);
}

package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void getMenuItemNames(int start, int length, AsyncCallback<PagedResult<String>> callback);
	void getOrders(int start, int length, AsyncCallback<PagedResult<String>> callback);
	void placeOrder(String itemName, AsyncCallback<Void> callback);
}

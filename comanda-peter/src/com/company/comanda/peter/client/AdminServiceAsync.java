package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>AdminService</code>.
 */
public interface AdminServiceAsync {
	void getRestaurants(int start, int length,
	        AsyncCallback<PagedResult<String[]>> callback);
	void deleteRestaurant(String restaurantKeyString, AsyncCallback<Void> callback);
}

package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("administration")
public interface AdminService extends RemoteService {
	PagedResult<String[]> getRestaurants(int start, int length);
	void deleteRestaurant(String restaurantKeyString);
}

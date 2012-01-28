package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	void greetServer(String name) throws IllegalArgumentException;
	PagedResult<String> getMenuItemNames(int start, int length);
	PagedResult<String[]> getOrders(int start, int length);
	public String getUploadUrl();
}

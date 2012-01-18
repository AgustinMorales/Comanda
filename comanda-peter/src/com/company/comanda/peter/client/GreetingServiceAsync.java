package com.company.comanda.peter.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<Void> callback)
			throws IllegalArgumentException;
	void getMenuItemNames(int start, int length, AsyncCallback<List<String>> callback);
}

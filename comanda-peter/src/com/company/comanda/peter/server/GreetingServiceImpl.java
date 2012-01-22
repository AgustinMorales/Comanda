package com.company.comanda.peter.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.company.comanda.peter.client.GreetingService;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.shared.FieldVerifier;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	//TODO: This should be done with dependency injection or something similar
	private ItemsManager itemsManager = new ItemsManager();
	
	public void greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
				if (!FieldVerifier.isValidName(input)) {
					// If the input is not valid, throw an IllegalArgumentException back to
					// the client.
					throw new IllegalArgumentException(
							"Name must be at least 4 characters long");
				}

				// Escape data from the client to avoid cross-site script vulnerabilities.
				input = escapeHtml(input);
				
				PersistenceManager pm = PMF.get().getPersistenceManager();   
			      MenuItem item = new MenuItem(input, "");
			   //persist
			      try{ pm.makePersistent(item); }
			      finally{ pm.close(); }
	}
	
	public PagedResult<String> getOrders(int start, int length){
		PersistenceManager pm = null;
		ArrayList<String> resultList = new ArrayList<String>();
		int total;
	    try{
	    	pm = PMF.get().getPersistenceManager();
		    Query query = pm.newQuery("select from " + Order.class.getName());
		    @SuppressWarnings("unchecked")
			List<Order> orders = (List<Order>) query.execute();
		    total = orders.size();
		    orders = cutList(orders, start, length);
		    resultList.ensureCapacity(orders.size());
		    
		    for(Order orderElement: orders){
		        resultList.add(orderElement.getName());
		    }
	    }
	    finally{
	    	if(pm != null){
	    		pm.close();
	    	}
	    }
		return new PagedResult<String>(resultList,total);
	}
	
	protected List cutList(List list, int start, int length){
	    int size = list.size();
        start = Math.min(start, size - 1);
        length = Math.min(length, size - start);
        
        list = list.subList(start, start + length);
        return list;
	}
	public void placeOrder(String menuItemName){
		itemsManager.placeOrder(menuItemName);
	}
	
	public PagedResult<String> getMenuItemNames(int start, int length){
		ArrayList<String> resultList = new ArrayList<String>();
		int total;

		List<MenuItem> items = itemsManager.getMenuItems();
		total = items.size();
		//Implement the paging inside ItemsManager
        items = cutList(items, start, length);
        resultList.ensureCapacity(items.size());
		
		for(MenuItem item: items){
		    resultList.add(item.getName());
		}
		return new PagedResult<String>(resultList, total);
	}
	
	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}

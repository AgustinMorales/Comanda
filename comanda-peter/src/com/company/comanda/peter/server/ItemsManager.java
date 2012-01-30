package com.company.comanda.peter.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.FetchPlan;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.shared.OrderState;

public class ItemsManager {

    private static final Logger log = Logger.getLogger(ItemsManager.class.getName());
    
	@SuppressWarnings("unchecked")
	public List<MenuItem> getMenuItems(){
		List<MenuItem> result = null;
		PersistenceManager pm = null;
	    try{
	    	pm = PMF.get().getPersistenceManager();
	    	Transaction tx = pm.currentTransaction();
	    	tx.begin();
		    Query query = pm.newQuery("select from " + MenuItem.class.getName());
		    query.getFetchPlan().setGroup(FetchPlan.ALL);
		    result = (List<MenuItem>) query.execute();
		    tx.commit();
	    }
	    finally{
	    	if(pm != null){
	    		pm.close();
	    	}
	    }
	    return result;
	}
	
	public void placeOrder(long keyId, String table){
        PersistenceManager pm = null;
        try{
            pm = PMF.get().getPersistenceManager();
            MenuItem menuItem = pm.getObjectById(MenuItem.class, keyId);
            if(menuItem == null){
                String errorMsg = String.format("Could not place order for item ID: %s",keyId);
                log.warning(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            Order newOrder = new Order(menuItem.getName(), table, new Date(), OrderState.ORDERED);
            pm.makePersistent(newOrder);
        }
        finally{
            if (pm != null){
                pm.close();
            }
        }
    }
	
	public void modifyOrder(long keyId, OrderState newState){
	    PersistenceManager pm = null;
        try{
            pm = PMF.get().getPersistenceManager();
            Order order = pm.getObjectById(Order.class, keyId);
            if(order == null){
                String errorMsg = String.format("Could not order with ID: %s",keyId);
                log.warning(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            order.setState(newState);
        }
        finally{
            if (pm != null){
                pm.close();
            }
        }
	}
	
	public void deleteMenuItems(long[] keyIds){
	    PersistenceManager pm = null;
        try{
            pm = PMF.get().getPersistenceManager();
            List<MenuItem> itemsToDelete = new ArrayList<MenuItem>(keyIds.length);
            for(long currentId : keyIds){
                itemsToDelete.add((MenuItem)pm.getObjectById(MenuItem.class,currentId));
            }
            pm.deletePersistentAll(itemsToDelete);
        }
        finally{
            if (pm != null){
                pm.close();
            }
        }
	}
}

package com.company.comanda.peter.server;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.FetchPlan;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;

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
            Order newOrder = new Order(menuItem.getName(), table);
            pm.makePersistent(newOrder);
        }
        finally{
            if (pm != null){
                pm.close();
            }
        }
    }
}

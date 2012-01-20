package com.company.comanda.peter.server;

import java.util.List;

import javax.jdo.FetchPlan;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;

public class ItemsManager {

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
	
	public void placeOrder(String menuItemName){
        PersistenceManager pm = null;
        try{
            pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery("select from " + MenuItem.class.getName() +
                    " where name == nameParam " +
                    "parameters String nameParam ");
            @SuppressWarnings("unchecked")
            List<MenuItem> menuItems = (List<MenuItem>)query.execute(menuItemName);
            if(menuItems.size() !=1 ){
                throw new IllegalArgumentException("!= 1 while placing order");
            }
            MenuItem menuItem = menuItems.get(0);
            
            Order newOrder = new Order(menuItem.getName());
            pm.makePersistent(newOrder);
        }
        finally{
            if (pm != null){
                pm.close();
            }
        }
    }
}

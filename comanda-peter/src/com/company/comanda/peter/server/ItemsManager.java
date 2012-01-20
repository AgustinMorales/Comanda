package com.company.comanda.peter.server;

import java.util.List;

import javax.jdo.FetchPlan;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.company.comanda.peter.server.model.MenuItem;

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
}

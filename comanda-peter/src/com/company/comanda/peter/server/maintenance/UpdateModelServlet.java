package com.company.comanda.peter.server.maintenance;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.ModelVersion;
import com.company.comanda.peter.shared.Qualifiers;
import com.google.appengine.api.datastore.Entity;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

@Singleton
public class UpdateModelServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -3405213982076166631L;

    private Objectify objectify;

    @Inject
    public UpdateModelServlet(Objectify ofy){
        this.objectify = ofy;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int currentVersion = 0;
        List<ModelVersion> versions = objectify.query(ModelVersion.class).list();
        if(versions.size() > 1){
            throw new IllegalStateException("More than one ModelVersion instance");
        }
        if(versions.size() == 0){
            //Then we have version 0
            currentVersion = 0;
        }
        else{
            currentVersion = versions.get(0).getVersionNumber();
        }
        if(currentVersion == 0){
            update0to1(resp.getWriter());
        }
    }

    private void update0to1(PrintWriter out){
        Objectify ofy = ObjectifyService.beginTransaction();
        try{
            List<Key<MenuItem>> menuItems = ofy.query(MenuItem.class).listKeys();
            out.println("Got " + menuItems.size() + " MenuItems to update...");
            for(Key<MenuItem> menuItemKey : menuItems){
                MenuItem menuItem = ofy.get(menuItemKey);
                if(menuItem.getPrices() == null){
                    com.google.appengine.api.datastore.Key rawKey = menuItemKey.getRaw();
                    Entity entity = ofy.getDatastore().get(rawKey);
                    Object property = entity.getProperty("price");
                    float price;
                    if(property instanceof Float){
                        price = (Float)property;
                    }
                    else if (property instanceof Double){
                        price = ( (Double) property).floatValue();
                    }
                    else if(property instanceof String){
                        price = Float.parseFloat((String)property);
                    }
                    else{
                        throw new IllegalStateException(
                                "Price poperty is of type: " + property.getClass());
                    }
                    ArrayList<Float> prices = new ArrayList<Float>(1);
                    prices.add(price);
                    ArrayList<String> qualifiers = new ArrayList<String>(1);
                    qualifiers.add(Qualifiers.SINGLE.toString());
                    menuItem.setPrices(prices);
                    menuItem.setQualifiers(qualifiers);
                    ofy.put(menuItem);
                }
            }
            ofy.getTxn().commit();
        }
        catch(Exception e){
            out.println("Exception while performing update form 0 to 1");
            out.println(e);
            out.println(e.getStackTrace());
            ofy.getTxn().rollback();
            out.println("Update has been rolled back");
        }
        finally{

        }
    }

}

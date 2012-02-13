package com.company.comanda.peter.server;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.company.comanda.peter.client.GUIService;
import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.shared.Constants;
import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.PagedResult;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
@Singleton
public class GUIServiceImpl extends RemoteServiceServlet implements
GUIService {

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    private RestaurantManager restaurantManager;
    private ComandaAdmin admin;
    
    @Inject
    public GUIServiceImpl(RestaurantManager restaurantManager,
            ComandaAdmin admin){
        this.restaurantManager = restaurantManager;
        this.admin = admin;
    }

    @SuppressWarnings("unchecked")
    public PagedResult<String[]> getOrders(int start, int length, 
            OrderState state, String tableName){
        ArrayList<String[]> resultList = new ArrayList<String[]>();
        int total;
        List<Order> orders = restaurantManager.getAgent().getOrders( 
                state, tableName);

        total = orders.size();
        orders = cutList(orders, start, length);
        resultList.ensureCapacity(orders.size());

        for(Order orderElement: orders){
            Table table = restaurantManager.getAgent().
                    getTable(orderElement.getTable());
            MenuItem menuItem = restaurantManager.getAgent().
                    getMenuItem(orderElement.getMenuItem());
            //FIXME: menuItem might be null!!!!
            resultList.add(new String[]{menuItem.getName(), 
                    table.getName(), 
                    "" + orderElement.getId()});
        }
        return new PagedResult<String[]>(resultList,total);
    }

    protected List cutList(List list, int start, int length){
        int size = list.size();
        start = Math.max(0, Math.min(start, size - 1));
        length = Math.min(length, size - start);

        list = list.subList(start, start + length);
        return list;
    }

    public PagedResult<String[]> getMenuItems(int start, int length){
        ArrayList<String[]> resultList = new ArrayList<String[]>();
        int total;

        List<MenuItem> items = restaurantManager.getAgent().getMenuItems();
        total = items.size();
        //Implement the paging inside ItemsManager
        items = cutList(items, start, length);
        resultList.ensureCapacity(items.size());

        for(MenuItem item: items){
            resultList.add(new String[]{
                    item.getId().toString(),
                    item.getImageString(), 
                    item.getName(), 
                    (new Integer(item.getPrice())).toString(),
                    item.getDescription()});
        }
        return new PagedResult<String[]>(resultList, total);
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

    public String getUploadUrl(){
        return blobstoreService.createUploadUrl("/newMenuItem");
    }

    @Override
    public void acceptOrder(String orderKey) {
        long keyId = Long.parseLong(orderKey);
        restaurantManager.getAgent().changeOrderState(
                keyId, OrderState.ACCEPTED);
    }

    @Override
    public void deleteMenuItems(long[] keyIds) {
        restaurantManager.getAgent().deleteMenuItems(keyIds);

    }

    @Override
    public boolean login(String username, String password) {
        boolean result = false;
        try{
            result = restaurantManager.login(username, password);
        }
        catch (IllegalStateException e) {
            //Try to log in again
            getThreadLocalRequest().getSession().invalidate();
            result = restaurantManager.login(username, password);
        }
        return result;
    }

    @Override
    public void newRestaurant(String name, String password) {
        admin.createRestaurant(name, password);
        
    }

    @Override
    public void addTable(String tablename) {
        restaurantManager.getAgent().addTable(tablename);
        
    }

    @Override
    public List<String[]> getTables() {
        List<Table> tables = restaurantManager.getAgent().getTables();
        List<String[]> result = new ArrayList<String[]>(tables.size());
        for(Table table : tables){
            result.add(new String[]{"" + table.getId(), 
                    table.getName(), 
                    restaurantManager.getAgent().getFullCode(
                            table.getCode())});
        }
        return result;
    }
}

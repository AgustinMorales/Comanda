package com.company.comanda.peter.server;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.company.comanda.peter.client.GUIService;
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
    
    @Inject
    public GUIServiceImpl(RestaurantManager restaurantManager){
        this.restaurantManager = restaurantManager;
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

        RestaurantAgent restaurantAgent = (RestaurantAgent)
                getThreadLocalRequest().getSession().
                getAttribute(Constants.RESTAURANT_AGENT);
        
        if(restaurantAgent == null){
            throw new IllegalStateException("No RestaurantAgent found");
        }
        List<MenuItem> items = restaurantAgent.getMenuItems();
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
        return restaurantManager.login(username, password);
    }
}

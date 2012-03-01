package com.company.comanda.peter.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.peter.client.GUIService;
import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.company.comanda.peter.server.model.Bill;
import com.company.comanda.peter.server.model.MenuCategory;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.shared.BillState;
import com.company.comanda.peter.shared.BillType;
import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.PagedResult;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
@Singleton
public class GUIServiceImpl extends RemoteServiceServlet implements
GUIService {

    private static final Logger log = LoggerFactory.getLogger(GUIServiceImpl.class);
    
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
        
        int total;
        List<Order> orders = restaurantManager.getAgent().getOrders( 
                state, tableName);

        total = orders.size();
        orders = cutList(orders, start, length);
        ArrayList<String[]> resultList = new ArrayList<String[]>(orders.size());
        Map<Key<Bill>, Bill> billMap = new HashMap<Key<Bill>, Bill>();
        for(Order currentOrder: orders){
            Bill currentBill = null;
            Key<Bill> billKey = currentOrder.getBill();
            if(billMap.containsKey(billKey)){
                currentBill = billMap.get(billKey);
            }
            else{
                currentBill = restaurantManager.getAgent().getBill(billKey);
                billMap.put(billKey, currentBill);
            }
            resultList.add(new String[]{currentOrder.getMenuItemName(), 
                    currentBill.getTableName(), 
                    currentOrder.getKeyString()});
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

    public PagedResult<String[]> getMenuItems(int start, int length, 
            Long categoryId){
        ArrayList<String[]> resultList = new ArrayList<String[]>();
        int total;

        List<MenuItem> items = restaurantManager.getAgent().
                getMenuItems(categoryId);
        total = items.size();
        //Implement the paging inside ItemsManager
        items = cutList(items, start, length);
        resultList.ensureCapacity(items.size());

        for(MenuItem item: items){
            resultList.add(new String[]{
                    item.getId().toString(),
                    item.getImageString(), 
                    item.getName(), 
                    (new Float(item.getPrice())).toString(),
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
        restaurantManager.getAgent().changeOrderState(
                orderKey, OrderState.ACCEPTED);
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
    public void newRestaurant(String name, String password, String address) {
        
        admin.createRestaurant(name, password, address);
        
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
                    table.getCode()});
        }
        return result;
    }

    @Override
    public List<String[]> getCategories() {
        List<MenuCategory> categories = 
                restaurantManager.getAgent().getCategories();
        List<String[]> result = 
                new ArrayList<String[]>(categories.size());
        for(MenuCategory category : categories){
            result.add(new String[]{
                    "" + category.getId(),
                    category.getName()
            });
        }
        return result;
    }

    @Override
    public PagedResult<String[]> getBills(int start,
            int length, BillState state, 
            BillType type) {
        List<Bill> bills = 
                restaurantManager.getAgent().getBills(state, type);
        final int total = bills.size();
        bills = cutList(bills, start, length);
        List<String[]> result = new ArrayList<String[]>(bills.size());
        
        for(Bill bill: bills){
            result.add(new String[]{
                    bill.getKeyString(),
                    bill.getAddress(),
                    bill.getOpenDate().toString(),
            });
        }
        return new PagedResult<String[]>(result, total);
    }
}

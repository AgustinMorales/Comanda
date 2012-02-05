package com.company.comanda.peter.server;

import java.util.ArrayList;
import java.util.List;

import com.company.comanda.peter.client.GreetingService;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.PagedResult;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
GreetingService {

    //TODO: This should be done with dependency injection or something similar
    private ItemsManager itemsManager = ItemsManager.me();

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();


    public void greetServer(String input) throws IllegalArgumentException {
        // Verify that the input is valid.
        throw new UnsupportedOperationException();
        //				if (!FieldVerifier.isValidName(input)) {
        //					// If the input is not valid, throw an IllegalArgumentException back to
        //					// the client.
        //					throw new IllegalArgumentException(
        //							"Name must be at least 4 characters long");
        //				}
        //
        //				// Escape data from the client to avoid cross-site script vulnerabilities.
        //				input = escapeHtml(input);
        //				
        //				PersistenceManager pm = PMF.get().getPersistenceManager();   
        //			      MenuItem item = new MenuItem(input, "");
        //			   //persist
        //			      try{ pm.makePersistent(item); }
        //			      finally{ pm.close(); }
    }

    @SuppressWarnings("unchecked")
    public PagedResult<String[]> getOrders(int start, int length, 
            OrderState state, String tableName){
        ArrayList<String[]> resultList = new ArrayList<String[]>();
        int total;
        List<Order> orders = itemsManager.getOrders(state, tableName);

        total = orders.size();
        orders = cutList(orders, start, length);
        resultList.ensureCapacity(orders.size());

        for(Order orderElement: orders){
            resultList.add(new String[]{orderElement.getName(), orderElement.getTable(), "" + orderElement.getId()});
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

        //FIXME
        List<MenuItem> items = itemsManager.getMenuItems(0);
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
        itemsManager.modifyOrder(keyId, OrderState.ACCEPTED);
    }

    @Override
    public void deleteMenuItems(long[] keyIds) {
        //FIXME
        itemsManager.deleteMenuItems(0, keyIds);

    }
}

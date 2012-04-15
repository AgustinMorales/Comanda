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
import com.company.comanda.peter.server.helper.ListHelper;
import com.company.comanda.peter.server.helper.QualifierTranslator;
import com.company.comanda.peter.server.helper.ServerFormatter;
import com.company.comanda.peter.server.model.Bill;
import com.company.comanda.peter.server.model.MenuCategory;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.shared.BillState;
import com.company.comanda.peter.shared.BillType;
import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.PagedResult;
import com.company.comanda.peter.shared.Qualifiers;
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

    @Inject
    public GUIServiceImpl(RestaurantManager restaurantManager){
        this.restaurantManager = restaurantManager;
    }

    @SuppressWarnings("unchecked")
    public PagedResult<String[]> getOrders(int start, int length, 
            BillType billType, OrderState state, String tableKeyString, String billKeyString){

        int total;
        List<Order> orders = restaurantManager.getAgent().getOrders(
                billType,
                state, tableKeyString,
                billKeyString);

        total = orders.size();
        orders = ListHelper.cutList(orders, start, length);
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
            resultList.add(new String[]{currentOrder.getMenuItemName() + 
                    ServerFormatter.getExtras(currentOrder), 
                    currentBill.getTableName(), 
                    currentOrder.getKeyString(),
                    new Integer(currentOrder.getNoOfItems()).toString(),
                    ServerFormatter.money(currentOrder.getTotalPrice())});
        }
        log.info("Found {} orders",total);
        return new PagedResult<String[]>(resultList,total);
    }

    public PagedResult<String[]> getMenuItems(int start, int length, 
            Long categoryId){
        ArrayList<String[]> resultList = new ArrayList<String[]>();
        int total;

        List<MenuItem> items = restaurantManager.getAgent().
                getMenuItems(categoryId);
        total = items.size();
        //Implement the paging inside ItemsManager
        items = ListHelper.cutList(items, start, length);
        resultList.ensureCapacity(items.size());

        for(MenuItem item: items){
            Float[] priceValues = new Float[Qualifiers.values().length];
            List<Float> prices = item.getPrices();
            List<String> qualifiers = item.getQualifiers();
            int pricesSize = prices.size();
            for(int i = 0;i<pricesSize;i++){
                if(qualifiers.get(i).equals(Qualifiers.SINGLE.toString())){
                    priceValues[0] = prices.get(i);
                }
                else if(qualifiers.get(i).equals(Qualifiers.SMALL.toString())){
                    priceValues[1] = prices.get(i);
                }
                else if(qualifiers.get(i).equals(Qualifiers.MEDIUM.toString())){
                    priceValues[2] = prices.get(i);
                }
                else if(qualifiers.get(i).equals(Qualifiers.LARGE.toString())){
                    priceValues[3] = prices.get(i);
                }
                else if(qualifiers.get(i).equals(Qualifiers.TAPA.toString())){
                    priceValues[4] = prices.get(i);
                }
                else if(qualifiers.get(i).equals(Qualifiers.HALF.toString())){
                    priceValues[5] = prices.get(i);
                }
                else if(qualifiers.get(i).equals(Qualifiers.FULL.toString())){
                    priceValues[6] = prices.get(i);
                }
            }
            String[] extras = new String[9];
            int noOfExtras = 0;
            if(item.getExtras() != null){
                noOfExtras = item.getExtras().size();
            }
            float[] extrasPrices = new float[9];
            for(int i=0;i<noOfExtras;i++){
                extras[i] = item.getExtras().get(i);
                extrasPrices[i] = item.getExtrasPrice().get(i);
            }
            resultList.add(new String[]{
                    item.getId().toString(),
                    item.getImageString(), 
                    item.getName(), 
                    item.getDescription(),
                    (priceValues[0]!=null?(ServerFormatter.money(priceValues[0])):null),
                    (priceValues[1]!=null?(ServerFormatter.money(priceValues[1])
                            + QualifierTranslator.translate(Qualifiers.SMALL)):null),
                            (priceValues[2]!=null?(ServerFormatter.money(priceValues[2])
                                    + QualifierTranslator.translate(Qualifiers.MEDIUM)):null),
                                    (priceValues[3]!=null?(ServerFormatter.money(priceValues[3])
                                            + QualifierTranslator.translate(Qualifiers.LARGE)):null),
                                            (priceValues[4]!=null?(ServerFormatter.money(priceValues[4])
                                                    + QualifierTranslator.translate(Qualifiers.TAPA)):null),
                                                    (priceValues[5]!=null?(ServerFormatter.money(priceValues[5])
                                                            + QualifierTranslator.translate(Qualifiers.HALF)):null),
                                                            (priceValues[6]!=null?(ServerFormatter.money(priceValues[6])
                                                                    + QualifierTranslator.translate(Qualifiers.FULL)):null),
                    item.getExtrasName(),
                    extras[0],
                    extras[1],
                    extras[2],
                    extras[3],
                    extras[4],
                    extras[5],
                    extras[6],
                    extras[7],
                    extras[8],
                    "" + extrasPrices[0],
                    "" + extrasPrices[1],
                    "" + extrasPrices[2],
                    "" + extrasPrices[3],
                    "" + extrasPrices[4],
                    "" + extrasPrices[5],
                    "" + extrasPrices[6],
                    "" + extrasPrices[7],
                    "" + extrasPrices[8],
                    "" + priceValues[0],
                    "" + priceValues[1],
                    "" + priceValues[2],
                    "" + priceValues[3],
                    "" + priceValues[4],
                    "" + priceValues[5],
                    "" + priceValues[6],
                    
                    
            });
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

    public String getUploadUrlForNewRestaurant(){
        String uploadUrl = blobstoreService.createUploadUrl("/newRestaurant");
        log.info("Upload URL for new restaurant: " + uploadUrl);
        return uploadUrl;
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
    public String login(String username, String password) {
        String result = null;
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
    public String login(String token) {
        String result = null;
        try{
            result = restaurantManager.login(token);
        }
        catch (IllegalStateException e) {
            //Try to log in again
            getThreadLocalRequest().getSession().invalidate();
            result = restaurantManager.login(token);
        }
        return result;
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
            result.add(new String[]{table.getKeyString(), 
                    table.getName(), 
                    table.getCode()});
        }
        return result;
    }

    @Override
    public PagedResult<String[]> getCategories(int start, int length) {
        List<MenuCategory> categories = 
                restaurantManager.getAgent().getCategories();
        final int total = categories.size();
        categories = ListHelper.cutList(categories, start, length);
        List<String[]> result = 
                new ArrayList<String[]>(categories.size());
                for(MenuCategory category : categories){
                    result.add(new String[]{
                            "" + category.getId(),
                            category.getName()
                    });
                }
                return new PagedResult<String[]>(result, total);
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
        bills = ListHelper.cutList(bills, start, length);
        List<String[]> result = new ArrayList<String[]>(bills.size());

        for(Bill bill: bills){
            result.add(new String[]{
                    bill.getKeyString(),
                    bill.getAddress(),
                    ServerFormatter.formatToYesterdayOrToday(bill.getOpenDate()),
                    bill.getPhoneNumber(),
                    ServerFormatter.money(bill.getTotalAmount()),
                    bill.getState().toString(),
            });
        }
        return new PagedResult<String[]>(result, total);
    }

    @Override
    public void changeBillState(String billKeyString, BillState newState, 
            Integer deliveryDelay) {
        restaurantManager.getAgent().changeBillState(billKeyString, newState, deliveryDelay);

    }

    @Override
    public void addOrModifyCategory(Long id, String name) {
        restaurantManager.getAgent().
        addOrModifyMenuCategory(id, name);

    }

    @Override
    public void removeCategories(long[] ids) {
        restaurantManager.getAgent().deleteCategories(ids);
    }
}

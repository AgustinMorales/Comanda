package com.company.comanda.peter.server;

import java.util.List;

import com.company.comanda.peter.server.model.Bill;
import com.company.comanda.peter.server.model.MenuCategory;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.shared.BillState;
import com.company.comanda.peter.shared.BillType;
import com.company.comanda.peter.shared.OrderState;
import com.googlecode.objectify.Key;

public interface RestaurantAgent {

    public List<MenuItem> getMenuItems();
    public List<MenuItem> getMenuItems(Long categoryId);
    
    public void addOrModifyMenuItem(Long itemId,
            String itemName,
            String description,
            List<Float> prices,
            List<String> qualifiers,
            String imageBlobkey,
            Long categoryId);
    
    public void deleteMenuItems(long[] keyIds);
    
    public List<Order> getOrders(BillType billType,
            OrderState state, String tableKeyString,
            String billKeyString);
    
    public List<Order> getOrdersByTableName(BillType billType,
            OrderState state, 
            String tableName);
    
    public void changeOrderState(String orderKeyString,
            OrderState newState);
    
    public Table getTable(Key<Table> tableKey);
    
    public MenuItem getMenuItem(long menuItemId);
    
    public long addTable(String name);
    
    public List<Table> getTables();
    
    public long addOrModifyMenuCategory(Long categoryId, String name);
    
    public List<MenuCategory> getCategories();
    
    public void deleteCategories(long[] categoryIds);
    
    public long getDeliveryTableId();
    
    public Bill getBill(Key<Bill> billKey);
    
    public List<Bill> getBills(BillState state,
            BillType billType);
    
    public void changeBillState(String billKeyString,
            BillState newState, Integer deliveryDelay);
}

package com.company.comanda.peter.server;

import java.util.List;

import com.company.comanda.peter.server.model.MenuCategory;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.shared.OrderState;
import com.googlecode.objectify.Key;

public interface RestaurantAgent {

    public List<MenuItem> getMenuItems();
    
    public void addOrModifyMenuItem(Long itemId,
            String itemName,
            String description,
            String priceString,
            String imageBlobkey,
            Long categoryId);
    
    public void deleteMenuItems(long[] keyIds);
    
    public List<Order> getOrders(
            OrderState state, Long tableId);
    
    public List<Order> getOrders(OrderState state, 
            String tableName);
    
    public void changeOrderState(long orderId,
            OrderState newState);
    
    public Table getTable(Key<Table> tableKey);
    
    public MenuItem getMenuItem(Key<MenuItem> menuItemKey);
    
    public long addTable(String name);
    
    public List<Table> getTables();
    
    public String getFullCode(String tableCode);
    
    public String getRestaurantCode();
    
    public long addOrModifyMenuCategory(Long categoryId, String name);
    
    public List<MenuCategory> getCategories();
}

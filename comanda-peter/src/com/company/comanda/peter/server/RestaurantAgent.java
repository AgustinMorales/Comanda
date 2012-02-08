package com.company.comanda.peter.server;

import java.util.List;

import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.shared.OrderState;

public interface RestaurantAgent {

    public List<MenuItem> getMenuItems();
    
    public void addOrModifyMenuItem(Long itemId,
            String itemName,
            String description,
            String priceString,
            String imageBlobkey);
    
    public void deleteMenuItems(long[] keyIds);
    
    public List<Order> getOrders(
            OrderState state, String tableName);
    
    public void changeOrderState(long userId, long orderId,
            OrderState newState);
    
}

package com.company.comanda.peter.server;

import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.model.Table;

public interface UserManager {

    public static class CodifiedData{
        public Restaurant restaurant;
        public Table table;
        
    }
    
    void placeOrder(long userId, String password, 
            long restaurantId,
            long menuItemId, long tableId);
    
    CodifiedData getData(String code);
}

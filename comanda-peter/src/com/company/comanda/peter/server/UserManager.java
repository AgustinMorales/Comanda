package com.company.comanda.peter.server;

public interface UserManager {

    void placeOrder(long userId, String password, 
            long restaurantId,
            long menuItemId, long tableId);
}

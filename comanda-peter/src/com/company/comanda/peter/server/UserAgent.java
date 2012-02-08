package com.company.comanda.peter.server;

public interface UserAgent {

    public void placeOrder(long restaurantId,
            long menuItemId, String table);
}

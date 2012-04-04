package com.company.comanda.peter.server.notification;

public interface NotificationManager {

    void notifyIfNecessary(String restaurantKeyString);
    
    void nofiticationEnded(String phone, boolean success);
    
    void scheduleNotification(String restaurantKeyString);
}

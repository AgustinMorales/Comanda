package com.company.comanda.peter.server.notification;

public interface NotificationManager {

    void notifyIfNecessary(String restaurantKeyString, String billKeyString);
    
    void nofiticationEnded(String phone, boolean success);
    
    void scheduleNotification(String restaurantKeyString, String billKeyString);
}

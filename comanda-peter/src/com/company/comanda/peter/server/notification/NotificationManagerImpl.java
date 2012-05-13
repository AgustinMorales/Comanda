package com.company.comanda.peter.server.notification;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.common.HttpParams;
import com.company.comanda.peter.server.model.Bill;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.shared.BillState;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

public class NotificationManagerImpl implements NotificationManager {

    private static final int NOTIFICATION_DELAY = 5*1000;
    private static final int REPEAT_NOTIFICATION_DELAY = 3*60*1000;
    private static final int WARNING_NOTIFICATION_DURATION = 5*60*1000;
    private static final int NOTIFICATION_DURATION_SANITY = 10*60*1000;

    private PhoneNotifier phoneNotifier;
    private Objectify ofy;
    private Queue queue;
    private static final Logger log = 
            LoggerFactory.getLogger(NotificationManagerImpl.class);

    @Inject
    public NotificationManagerImpl(PhoneNotifier phoneNotifier,
            Objectify ofy,
            Queue queue){
        super();
        this.phoneNotifier = phoneNotifier;
        this.ofy = ofy;
        this.queue = queue;
    }

    @Override
    public void notifyIfNecessary(String restaurantKeyString, String billKeyString) {
        log.info("notifyIfNecessary");
        final Key<Restaurant> restaurantKey = new Key<Restaurant>(restaurantKeyString);
        Restaurant restaurant = ofy.get(restaurantKey);
        String phone = restaurant.getPhone();
        if(phone != null){
            log.info("phone != null");
            if(restaurant.isNotifying() == true){
                log.info("Restaurant is notifying");
                Date latestDate = restaurant.getLatestSuccessfulNotification();
                boolean failure = false;
                boolean sanity = true;
                if(latestDate != null){
                    long latest = latestDate.getTime();
                    long ellapsed = System.currentTimeMillis() - latest;
                    if(ellapsed > NOTIFICATION_DURATION_SANITY){
                        sanity = false;
                        log.info("Notification exceeded duration sanity check. " +
                        		"Notifying anyway. Restaurant: {}", restaurant.getName());
                        restaurant.setNotifying(false);
                        ofy.put(restaurant);
                    }
                    if(ellapsed > WARNING_NOTIFICATION_DURATION){
                        log.info("WARNING_NOTIFICATION_DURATION");
                        failure = true;
                    }
                }
                else{
                    sanity = false;
                    log.info("Notifying anyway. Restaurant: {}", restaurant.getName());
                    restaurant.setNotifying(false);
                    ofy.put(restaurant);
                }
                if(sanity){
                    if(failure){
                        log.warn("Notifying {} since {}. It's taking too long. " +
                                "Possible error.",
                                restaurant.getName(), latestDate);
                    }
                    else{
                        log.info("Restaurant {} is already being notified, skipping...", 
                                restaurant.getName());
                    }
                    scheduleNotification(restaurantKeyString,
                            billKeyString, REPEAT_NOTIFICATION_DELAY);
                }
            }
            if(restaurant.isNotifying() == false){
                log.info("Restaurant is not notifying");
                final BillState billState  = ofy.get(new Key<Bill>(billKeyString)).getState();
                log.info("Bill state: {}", billState);
                if(billState == BillState.OPEN){
                    log.info("Calling {} on phone {}", restaurant.getName(), phone);
                    restaurant.setNotifying(true);
                    phoneNotifier.call(phone, billKeyString);
                    scheduleNotification(restaurantKeyString,
                            billKeyString, REPEAT_NOTIFICATION_DELAY);
                    ofy.put(restaurant);
                }
                else{
                    log.info("No pending bills for {}, not calling.", 
                            restaurant.getName());
                }
            }
        }
        else{
            log.info("Cannot notify restaurant {}. Phone is null", restaurant);
        }
    }

    @Override
    public void nofiticationEnded(String phone, boolean success) {
        List<Restaurant> restaurants = 
                ofy.query(Restaurant.class).filter("phone", phone).list();
        if(restaurants.size() != 1){
            log.error("Wrong number ({}) of restaurants found for phone {}",
                    restaurants.size(), phone);
            throw new IllegalStateException("Wrong " +
            		"number of restaurants for phone " + phone);
        }
        Restaurant restaurant = restaurants.get(0);
        String restaurantKeyString = restaurant.getKeyString();
        if(restaurant.isNotifying() == false){
            log.error("Got notification ending " +
            		"for restaurant not being notified: {}", restaurant.getName());
        }
        else{
            restaurant.setNotifying(false);
            if(success){
                log.info("Notification for {} ended successfully", 
                        restaurant.getName());
                restaurant.setLatestSuccessfulNotification(new Date());
                
            }
            ofy.put(restaurant);
            if(success == false){
                log.warn("Notification for {} failed. Trying to notify again...");
            }
        }
    }

    //FIXME: Should add notification if there is already a task for it
    @Override
    public void scheduleNotification(String restaurantKeyString, String billKeyString) {
        scheduleNotification(restaurantKeyString, billKeyString, NOTIFICATION_DELAY);
    }
    
    protected void scheduleNotification(String restaurantKeyString, String billKeyString, int delay) {
        TaskOptions options = TaskOptions.Builder.withUrl(
                HttpParams.NotifyPendingBills.SERVICE_NAME).param(
                        HttpParams.NotifyPendingBills.
                        PARAM_RESTAURANT_KEY_STRING, 
                        restaurantKeyString).param(
                                HttpParams.NotifyPendingBills.
                                PARAM_BILL_KEY_STRING, billKeyString);
        options.countdownMillis(delay);
        queue.add(options);
    }
}

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

    private static final int NOTIFICATION_DELAY = 3*60*1000;
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
    public void notifyIfNecessary(String restaurantKeyString) {
        log.info("notifyIfNecessary");
        final Key<Restaurant> restaurantKey = new Key<Restaurant>(restaurantKeyString);
        Restaurant restaurant = ofy.get(restaurantKey);
        String phone = restaurant.getPhone();
        if(phone != null){
            if(restaurant.isNotifying() == true){
                Date latestDate = restaurant.getLatestSuccessfulNotification();
                boolean failure = false;
                boolean sanity = true;
                if(latestDate != null){
                    long latest = latestDate.getTime();
                    long ellapsed = System.currentTimeMillis() - latest;
                    if(ellapsed > NOTIFICATION_DURATION_SANITY){
                        sanity = false;
                        log.error("Notification exceeded duration sanity check. " +
                        		"Notifying anyway. Restaurant: {}", restaurant.getName());
                        restaurant.setNotifying(false);
                        ofy.put(restaurant);
                    }
                    if(ellapsed > WARNING_NOTIFICATION_DURATION){
                        failure = true;
                    }
                }
                if(sanity){
                    if(failure){
                        log.warn("Notifying {} since {}. It's taking too long. " +
                                "Possible error.",
                                restaurant.getName(), latestDate);
                    }
                    else{
                        log.debug("Restaurant {} is already being notified, skipping...", 
                                restaurant.getName());
                    }
                    scheduleNotification(restaurantKeyString);
                }
            }
            if(restaurant.isNotifying() == false){
                final int pendingQueries = ofy.query(Bill.class).filter(
                        "state", BillState.OPEN).ancestor(
                                restaurantKey).count();
                if(pendingQueries > 0){
                    log.info("Calling {} on phone {}", restaurant.getName(), phone);
                    restaurant.setNotifying(true);
                    phoneNotifier.call(phone);
                    scheduleNotification(restaurantKeyString);
                    ofy.put(restaurant);
                }
                else{
                    log.info("No pending bills for {}, not calling.", 
                            restaurant.getName());
                }
            }
        }
        else{
            log.error("Cannot notify restaurant {}. Phone is null", restaurant);
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
    public void scheduleNotification(String restaurantKeyString) {
        TaskOptions options = TaskOptions.Builder.withUrl(
                HttpParams.NotifyPendingBills.SERVICE_NAME).param(
                        HttpParams.NotifyPendingBills.
                        PARAM_RESTAURANT_KEY_STRING, 
                        restaurantKeyString);
        options.countdownMillis(NOTIFICATION_DELAY);
        queue.add(options);
    }
    
}

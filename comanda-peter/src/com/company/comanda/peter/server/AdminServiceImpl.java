package com.company.comanda.peter.server;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.company.comanda.peter.client.AdminService;
import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.company.comanda.peter.server.helper.ListHelper;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.shared.PagedResult;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Singleton
public class AdminServiceImpl extends RemoteServiceServlet 
implements AdminService {

    /**
     * 
     */
    private static final long serialVersionUID = 4463471686971490574L;
    private ComandaAdmin admin;
    private UserService userService;
    
    @Inject
    public AdminServiceImpl(ComandaAdmin admin){
        this.admin = admin;
        userService = UserServiceFactory.getUserService();
    }
    
    private void login(){
//        if(userService.isUserAdmin() == false){
//            throw new IllegalStateException("User is not admin");
//        }
    }
    
    @Override
    public PagedResult<String[]> getRestaurants(int start, int length) {
        login();
        List<Restaurant> restaurants = 
                admin.getRestaurants();
        final int total = restaurants.size();
        restaurants = ListHelper.cutList(restaurants, start, length);
        List<String[]> result = new ArrayList<String[]>(restaurants.size());

        for(Restaurant restaurant: restaurants){
            result.add(new String[]{
                    restaurant.getKeyString(),
                    restaurant.getName(),
                    restaurant.getLogin(),
                    restaurant.getAddress(),
                    restaurant.getPhone(),
                    restaurant.getDescription(),
                    "" + restaurant.getDeliveryCost(),
                    "" + restaurant.getMinimumForDelivery(),
                    "" + restaurant.getMaxDeliveryDistance()
            });
        }
        return new PagedResult<String[]>(result, total);
    }

    @Override
    public void deleteRestaurant(String restaurantKeyString) {
        login();
        throw new UnsupportedOperationException("Restaurant deletion not implemented");

    }

}

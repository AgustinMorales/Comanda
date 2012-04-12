package com.company.comanda.peter.server;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.company.comanda.peter.client.AdminService;
import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.company.comanda.peter.server.helper.ListHelper;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Singleton
public class AdminServiceImpl extends RemoteServiceServlet 
implements AdminService {

    /**
     * 
     */
    private static final long serialVersionUID = 4463471686971490574L;
    private ComandaAdmin admin;
    
    @Inject
    public AdminServiceImpl(ComandaAdmin admin){
        this.admin = admin;
    }
    
    @Override
    public PagedResult<String[]> getRestaurants(int start, int length) {
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
                    "" + restaurant.getMinimumForDelivery()
            });
        }
        return new PagedResult<String[]>(result, total);
    }

    @Override
    public void deleteRestaurant(String restaurantKeyString) {
        throw new UnsupportedOperationException("Restaurant deletion not implemented");

    }

}

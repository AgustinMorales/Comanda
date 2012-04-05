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

@Singleton
public class AdminServiceImpl implements AdminService {

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
            });
        }
        return new PagedResult<String[]>(result, total);
    }

    @Override
    public void deleteRestaurant(String restaurantKeyString) {
        throw new UnsupportedOperationException("Restaurant deletion not implemented");

    }

}

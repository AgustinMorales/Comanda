package com.company.comanda.peter.server.model;


import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Parent;

public class Table {
    
    
    @Id
    private Long id;
    private String name;
    @Parent
    private Key<Restaurant> restaurant;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Key<Restaurant> getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(Key<Restaurant> restaurant) {
        this.restaurant = restaurant;
    }
    public String getKeyString(){
        return (new Key<Table>(
                restaurant, Table.class, 
                id)).getString();
    }
    
    public String getCode(){
        String restaurantString = "" + restaurant.getId();
        return String.format("%02d%s%d", restaurantString.length(), restaurantString, id);
    }
    
    public static Key<Table> parseCode(String code){
        int restaurant_length = Integer.parseInt(code.substring(0, 2));
        long restaurantId = Long.parseLong(code.substring(2,2 + restaurant_length));
        long tableId = Long.parseLong(code.substring(2 + restaurant_length));
        
        return new Key<Table>(
                new Key<Restaurant>(Restaurant.class, restaurantId), 
                Table.class, tableId);
    }
}

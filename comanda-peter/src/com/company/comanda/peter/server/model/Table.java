package com.company.comanda.peter.server.model;


import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Parent;

public class Table {

    public static final int TABLE_CODE_RANDOM_PART_MAX_VALUE = 9999;
    public static final int TABLE_CODE_ID_PART_WIDTH = 4;
    public static final int TABLE_CODE_RANDOM_PART_WIDTH = 4;
    
    
    @Id
    private Long id;
    private String name;
    private String code;
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
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Key<Restaurant> getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(Key<Restaurant> restaurant) {
        this.restaurant = restaurant;
    }
    
    
}

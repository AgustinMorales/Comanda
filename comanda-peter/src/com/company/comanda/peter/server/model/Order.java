package com.company.comanda.peter.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.OrderType;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Parent;

public class Order
{
    
    @Id
    private Long id;
    private Date date;
    private OrderState state;
    @Parent
    private Key<User> user;
    private Key<Table> table;
    private List<Key<MenuItem>> menuItemsIds;
    private List<String> menuItemComments;
    private Key<Restaurant> restaurant;
    private OrderType type;
    private String address;
    private String comments;
    
    //FIXME: This is here just for objectify (and might not be needed)
    public Order(){
        super();
        menuItemsIds = new ArrayList<Key<MenuItem>>();
        menuItemComments = new ArrayList<String>();
    }

    @Deprecated
    public Order(Date date, OrderState state,
           Key<Table> table, Key<MenuItem> menuItem, 
           OrderType type,
           Key<Restaurant> restaurant)
    {
        this();
        this.date = date;
        this.state = state;
        this.table = table;
        this.type = type;
        this.menuItemsIds.add(menuItem);
        this.menuItemComments.add("");
    }
    
    public Order(Date date, OrderState state,
            Key<Table> table, List<Key<MenuItem>> menuItems,
            List<String> menuItemComments, OrderType type,
            Key<Restaurant> restaurant)
     {
         this();
         if(menuItems.size() != menuItemComments.size()){
             throw new IllegalArgumentException("Wrong number of comments");
         }
         this.date = date;
         this.state = state;
         this.table = table;
         this.type = type;
         this.menuItemsIds = menuItems;
         this.menuItemComments = menuItemComments;
         this.restaurant = restaurant;
     }
    
    public Long getId() 
    {
        return id;
    }
    public Key<Table> getTable() {
        return table;
    }
    public void setTable(Key<Table> table) {
        this.table = table;
    }
    protected synchronized Date getDate() {
        return date;
    }
    protected synchronized void setDate(Date date) {
        this.date = date;
    }
    public OrderState getState() {
        return state;
    }
    public void setState(OrderState state) {
        this.state = state;
    }

    public Key<User> getUser() {
        return user;
    }

    public void setUser(Key<User> user) {
        this.user = user;
    }

    public List<Key<MenuItem>> getMenuItemsIds() {
        return menuItemsIds;
    }

    public void setMenuItemsIds(List<Key<MenuItem>> menuItemsIds) {
        this.menuItemsIds = menuItemsIds;
    }

    public List<String> getMenuItemComments() {
        return menuItemComments;
    }

    public void setMenuItemComments(List<String> menuItemComments) {
        this.menuItemComments = menuItemComments;
    }

    public Key<Restaurant> getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Key<Restaurant> restaurant) {
        this.restaurant = restaurant;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getKeyString(){
        return (new Key<Order>(user,Order.class,id)).getString();
    }
    
}
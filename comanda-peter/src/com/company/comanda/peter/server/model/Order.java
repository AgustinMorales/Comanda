package com.company.comanda.peter.server.model;

import java.io.Serializable;
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
    
    public static class OrderElement implements Serializable{
        
        /**
         * 
         */
        private static final long serialVersionUID = -2358778499156688102L;
        private Key<MenuItem> menuItem;
        private String comments;
        
        public OrderElement(){
            super();
        }
        
        public OrderElement(Key<MenuItem> item, String comments){
            this();
            this.menuItem = item;
            this.comments = comments;
        }
        
        public Key<MenuItem> getMenuItem() {
            return menuItem;
        }
        public void setMenuItem(Key<MenuItem> menuItem) {
            this.menuItem = menuItem;
        }
        public String getComments() {
            return comments;
        }
        public void setComments(String comments) {
            this.comments = comments;
        }
        
        
    }
    
    @Id
    private Long id;
    private Date date;
    private OrderState state;
    @Parent
    private Key<User> user;
    private Key<Table> table;
    private List<OrderElement> menuItems;
    private Key<Restaurant> restaurant;
    private OrderType type;
    private String address;
    private String comments;
    
    //FIXME: This is here just for objectify (and might not be needed)
    public Order(){
        super();
        menuItems = new ArrayList<Order.OrderElement>();
    }

    @Deprecated
    public Order(Date date, OrderState state,
           Key<Table> table, Key<MenuItem> menuItem, OrderType type)
    {
        this();
        this.date = date;
        this.state = state;
        this.table = table;
        this.type = type;
        this.menuItems.add(new OrderElement(menuItem,""));
    }
    
    public Order(Date date, OrderState state,
            Key<Table> table, List<OrderElement> orderElements, OrderType type)
     {
         this();
         this.date = date;
         this.state = state;
         this.table = table;
         this.type = type;
         this.menuItems = orderElements;
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

    public List<OrderElement> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<OrderElement> menuItems) {
        this.menuItems = menuItems;
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


    
}
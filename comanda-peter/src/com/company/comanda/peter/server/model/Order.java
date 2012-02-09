package com.company.comanda.peter.server.model;

import java.util.Date;

import javax.persistence.Id;

import com.company.comanda.peter.shared.OrderState;
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
    private Key<MenuItem> menuItem;
    
    public Order(){
        super();
    }

    public Order(Date date, OrderState state,
           Key<Table> table, Key<MenuItem> menuItem)
    {
        this.date = date;
        this.state = state;
        this.table = table;
        this.menuItem = menuItem;
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

    public Key<MenuItem> getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(Key<MenuItem> menuItem) {
        this.menuItem = menuItem;
    }

    
}
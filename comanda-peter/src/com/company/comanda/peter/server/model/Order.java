package com.company.comanda.peter.server.model;

import java.util.Date;

import com.company.comanda.peter.shared.OrderState;
import com.google.appengine.api.datastore.Key;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Order
{
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    @Persistent(defaultFetchGroup = "true")
    private String name;
    @Persistent
    private String table;
    @Persistent
    private Date date;
    @Persistent
    private OrderState state;
    

    public Order(String name, String table, Date date, OrderState state)
    {
        this.name = name;
        this.table = table;
        this.date = date;
        this.state = state;
    }
    public Key getKey() 
    {
        return key;
    }
    public String getName() 
    {
        return name;
    }
    public void setName(String t)
    {
     this.name = t;
    }
    public String getTable() {
        return table;
    }
    public void setTable(String table) {
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
    
}
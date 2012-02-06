package com.company.comanda.peter.server.model;

import java.util.Date;

import javax.persistence.Id;

import com.company.comanda.peter.shared.OrderState;

//FIXME: Should create a parent User class
public class Order
{
    @Id
    private Long id;
    private String name;
    private String table;
    private Date date;
    private OrderState state;
    
    public Order(){
        super();
    }

    public Order(String name, String table, Date date, OrderState state)
    {
        this.name = name;
        this.table = table;
        this.date = date;
        this.state = state;
    }
    public Long getId() 
    {
        return id;
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
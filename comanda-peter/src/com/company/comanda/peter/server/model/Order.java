package com.company.comanda.peter.server.model;

import java.util.Date;

import javax.persistence.Id;

import com.company.comanda.peter.shared.BillType;
import com.company.comanda.peter.shared.OrderState;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Parent;

public class Order
{
    
    @Id
    private Long id;
    private Date date;
    private OrderState state;
    private String menuItemName;
    private float price;
    private String comments;
    private int noOfItems;
    @Parent
    private Key<Bill> bill;
    private Key<MenuItem> menuItemKey;
    private Key<Table> table;
    private BillType billType;
    
    //FIXME: This is here just for objectify (and might not be needed)
    public Order(){
        super();
    }

    
    public Order(Date date, OrderState state, 
            String menuItemName,
            float price,
            Key<MenuItem> menuItemKey,
            String comments,
            Key<Bill> bill,
            BillType billType)
     {
         this();
         this.date = date;
         this.state = state;
         this.menuItemName = menuItemName;
         this.price = price;
         this.comments = comments;
         this.bill = bill;
         this.menuItemKey = menuItemKey;
         this.billType = billType;
     }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }


    public OrderState getState() {
        return state;
    }


    public void setState(OrderState state) {
        this.state = state;
    }

    public String getComments() {
        return comments;
    }


    public void setComments(String comments) {
        this.comments = comments;
    }


    public Key<Bill> getBill() {
        return bill;
    }


    public void setBill(Key<Bill> bill) {
        this.bill = bill;
    }


    public String getMenuItemName() {
        return menuItemName;
    }


    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }


    public float getPrice() {
        return price;
    }


    public void setPrice(float price) {
        this.price = price;
    }


    public Key<MenuItem> getMenuItemKey() {
        return menuItemKey;
    }


    public void setMenuItemKey(Key<MenuItem> menuItemKey) {
        this.menuItemKey = menuItemKey;
    }
    
    public String getKeyString(){
        return (new Key<Order>(bill,Order.class,id)).getString();
    }


    public Key<Table> getTable() {
        return table;
    }


    public void setTable(Key<Table> table) {
        this.table = table;
    }


    public BillType getBillType() {
        return billType;
    }


    public void setBillType(BillType billType) {
        this.billType = billType;
    }


    public int getNoOfItems() {
        return noOfItems;
    }


    public void setNoOfItems(int noOfItems) {
        this.noOfItems = noOfItems;
    }
    
    
}
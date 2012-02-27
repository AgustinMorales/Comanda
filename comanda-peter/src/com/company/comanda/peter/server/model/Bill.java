package com.company.comanda.peter.server.model;

import java.util.Date;

import javax.persistence.Id;

import com.company.comanda.peter.shared.BillType;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Parent;

public class Bill {

    @Id
    private Long id;
    private Date openDate;
    private Date closeDate;
    private Key<User> user;
    private Key<Table> table;
    private String tableName;
    @Parent
    private Key<Restaurant> restaurant;
    private BillType type;
    private String address;
    private String comments;
   
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getOpenDate() {
        return openDate;
    }
    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }
    public Date getCloseDate() {
        return closeDate;
    }
    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }
    public Key<User> getUser() {
        return user;
    }
    public void setUser(Key<User> user) {
        this.user = user;
    }
    public Key<Table> getTable() {
        return table;
    }
    public void setTable(Key<Table> table) {
        this.table = table;
    }
    public Key<Restaurant> getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(Key<Restaurant> restaurant) {
        this.restaurant = restaurant;
    }
    public BillType getType() {
        return type;
    }
    public void setType(BillType type) {
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
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getKeyString(){
        return (new Key<Bill>(user,Bill.class,id)).getString();
    }
    
}

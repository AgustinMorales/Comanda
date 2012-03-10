package com.company.comanda.peter.server.model;

import java.util.Date;

import javax.persistence.Id;

import com.company.comanda.peter.shared.BillState;
import com.company.comanda.peter.shared.BillType;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Parent;

public class Bill {

    @Id
    private Long id;
    private Date openDate;
    private Date closeDate;
    private Date estimatedDeliveryDate;
    private Key<User> user;
    private Key<Table> table;
    private String tableName;
    @Parent
    private Key<Restaurant> restaurant;
    private String restaurantName;
    private BillType type;
    private BillState state;
    private String address;
    private String comments;
    private float totalAmount;
    private String phoneNumber;
   
    
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
        return (new Key<Bill>(restaurant,Bill.class,id)).getString();
    }
    public BillState getState() {
        return state;
    }
    public void setState(BillState state) {
        this.state = state;
    }
    public String getRestaurantName() {
        return restaurantName;
    }
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
    public float getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Date getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }
    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }
    
}

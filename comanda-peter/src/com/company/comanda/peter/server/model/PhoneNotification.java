package com.company.comanda.peter.server.model;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Parent;

public class PhoneNotification {

    @Id
    private Long id;
    private String callSid;
    @Parent
    private Key<Bill> bill;
   
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCallSid() {
        return callSid;
    }
    public void setCallSid(String callSid) {
        this.callSid = callSid;
    }
    public Key<Bill> getBill() {
        return bill;
    }
    public void setBill(Key<Bill> bill) {
        this.bill = bill;
    }
    
}
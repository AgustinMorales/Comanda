package com.company.comanda.peter.server.model;

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
    

    public Order(String name)
    {
        this.name = name;
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
}
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
    @Persistent
    private String table;
    

    public Order(String name, String table)
    {
        this.name = name;
        this.table = table;
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
    
}
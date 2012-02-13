package com.company.comanda.peter.server.model;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Parent;

public class MenuItem
{
    @Id
    private Long id;
    
    private String name;
    private String description;
    private String imageString;
    private int price;
    @Parent
    private Key<Restaurant> parent;
    private Key<MenuCategory> category;
    

    public MenuItem(){
    }
    
    public MenuItem(String name, String desc, String imageString, 
            int price)
    {
        this.name = name;
        this.description = desc;
        this.imageString = imageString;
        this.price = price;
    }
    public Long getId() 
    {
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getName() 
    {
        return name;
    }
    public String getDescription() 
    {
        return description;
    }
    public void setName(String t)
    {
     this.name = t;
    }    
    public void setDescription(String d)
    {
     this.description = d;
    }
    public String getImageString() {
        return imageString;
    }
    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public Key<Restaurant> getParent() {
        return parent;
    }

    public void setParent(Key<Restaurant> parent) {
        this.parent = parent;
    }

    public Key<MenuCategory> getCategory() {
        return category;
    }

    public void setCategory(Key<MenuCategory> category) {
        this.category = category;
    } 
    
    
}
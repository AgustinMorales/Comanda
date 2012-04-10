package com.company.comanda.peter.server.model;

import java.util.ArrayList;
import java.util.List;

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
    @Parent
    private Key<Restaurant> parent;
    private Key<MenuCategory> category;
    private List<Float> prices;
    private List<String> qualifiers;
    private String extrasName;
    private List<String> extras;
    private List<Float> extrasPrice;


    public MenuItem(){
    	super();
    	prices = new ArrayList<Float>(1);
    	qualifiers = new ArrayList<String>(1);
    }
    
    public MenuItem(String name, String desc, String imageString, 
            float price)
    {
        this.name = name;
        this.description = desc;
        this.imageString = imageString;
        this.prices.add(price);
        this.qualifiers.add("");
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

	public List<Float> getPrices() {
		return prices;
	}

	public void setPrices(List<Float> prices) {
		this.prices = prices;
	}

	public List<String> getQualifiers() {
		return qualifiers;
	}

	public void setQualifiers(List<String> qualifiers) {
		this.qualifiers = qualifiers;
	}

    public String getExtrasName() {
        return extrasName;
    }

    public void setExtrasName(String extrasName) {
        this.extrasName = extrasName;
    }

    public List<String> getExtras() {
        return extras;
    }

    public void setExtras(List<String> extras) {
        this.extras = extras;
    }

    public List<Float> getExtrasPrice() {
        return extrasPrice;
    }

    public void setExtrasPrice(List<Float> extrasPrice) {
        this.extrasPrice = extrasPrice;
    }

	
    
    
}
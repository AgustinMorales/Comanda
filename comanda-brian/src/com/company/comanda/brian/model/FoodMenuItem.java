package com.company.comanda.brian.model;

import java.util.List;

public class FoodMenuItem 
{
    
    private String name;
    private String imageString;
    private String keyId;
    private String description;
    private long categoryId;
    private List<Float> prices;
    private List<String> qualifiers;
    private String extrasName;
    private List<String> extras;
    private List<Float> extrasPrice;
    private boolean extrasMultipleChoice;
    
    public FoodMenuItem()
    {
     this.name = "";
     this.imageString = "";
    }
    public String getName() 
    {
        return this.name;
    }
    public void setName(String t) 
    {
        this.name = t;
    }  
    public String toString()
    {
     return "MenuItem (Name:" + this.name + ")";
    }
    public String getImageString() {
        return imageString;
    }
    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
    public String getKeyId() {
        return keyId;
    }
    public void setKeyId(String keyID) {
        this.keyId = keyID;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
    public List<Float> getPrices() {
        return prices;
    }
	public List<String> getQualifiers() {
		return qualifiers;
	}
	public void setPrices(List<Float> prices) {
		this.prices = prices;
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
    public boolean isExtrasMultipleChoice() {
        return extrasMultipleChoice;
    }
    public void setExtrasMultipleChoice(boolean extrasMultipleChoice) {
        this.extrasMultipleChoice = extrasMultipleChoice;
    }
    public List<Float> getExtrasPrice() {
        return extrasPrice;
    }
    public void setExtrasPrice(List<Float> extrasPrice) {
        this.extrasPrice = extrasPrice;
    }
    
    
    
}

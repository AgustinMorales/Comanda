package com.company.comanda.brian.model;

public class FoodMenuItem 
{
    
    private String name;
    
    public FoodMenuItem()
    {
     this.name = "";
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
}

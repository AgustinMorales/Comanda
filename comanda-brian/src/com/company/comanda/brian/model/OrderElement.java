package com.company.comanda.brian.model;

import java.util.List;

public class OrderElement {

    public FoodMenuItem menuItem;
    public int qualifierIndex;
    public List<Integer> extras;
    public float toalPrice;

    public OrderElement(FoodMenuItem menuItem, int qualifierIndex){
        this.menuItem = menuItem;
        this.qualifierIndex = qualifierIndex;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if(o instanceof OrderElement){
            OrderElement other = (OrderElement)o;
            if(menuItem != null && other.menuItem != null && menuItem.equals(other.menuItem)
                    && qualifierIndex == other.qualifierIndex){
                if(extras != null){
                    if(extras.equals(other.extras)){
                        result = true;
                    }
                }
                else{
                    if(other.extras == null){
                        result = true;
                    }
                }
            }
        }
        return result;
    }
    @Override
    public int hashCode() {
        int result;
        result = (menuItem!=null?menuItem.hashCode():0) ^ 
                qualifierIndex ^ (extras!=null?extras.hashCode():0);
        return result;
    }


}

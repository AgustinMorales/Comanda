package com.company.comanda.brian.model;

public class OrderElement {

	public FoodMenuItem menuItem;
	public int qualifierIndex;
	
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
				result = true;
			}
		}
		return result;
	}
	@Override
	public int hashCode() {
		int result;
		if(menuItem != null){
			result = menuItem.hashCode() ^ qualifierIndex;
		}
		else{
			result = qualifierIndex;
		}
		return result;
	}
	
	
}

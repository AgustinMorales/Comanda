package com.company.comanda.peter.server.model;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

public class Restaurant {

    @Id
    private Long id;
    
    private List<MenuItem> menuItems = new ArrayList<MenuItem>();
    

    public Long getId() {
        return id;
    }

    public void setKey(Long id) {
        this.id = id;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
    
    
}

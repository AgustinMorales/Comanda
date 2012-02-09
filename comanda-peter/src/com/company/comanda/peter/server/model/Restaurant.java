package com.company.comanda.peter.server.model;



import javax.persistence.Id;

public class Restaurant {

    @Id
    private Long id;
    private String name;
    private String hashedPassword;
    

    public Long getId() {
        return id;
    }

    public void setKey(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    
    
}

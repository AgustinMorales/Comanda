package com.company.comanda.peter.server.model;



import java.util.List;

import javax.persistence.Id;

import com.beoui.geocell.model.LocationCapable;
import com.beoui.geocell.model.Point;
import com.googlecode.objectify.Key;

public class Restaurant implements LocationCapable{

    @Id
    private Long id;
    private String name;
    private String login;
    private String imageUrl;
    private String hashedPassword;
    private String description;
    private double latitude;
    private double longitude;
    private List<String> geocells;
    

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setGeoCells(List<String> cells) {
        this.geocells = cells;
    }

    @Override
    public Point getLocation() {
        return new Point(latitude, longitude);
    }

    @Override
    public String getKeyString() {
        return (new Key<Restaurant>(Restaurant.class,id)).getString();
    }

    @Override
    public List<String> getGeocells() {
        return this.geocells;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}

package com.company.comanda.peter.server.model;

import javax.persistence.Id;

public class LoginToken {

    @Id
    private Long id;
    private String login;
    private String token;
   
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    
    
}

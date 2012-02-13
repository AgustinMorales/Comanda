package com.company.comanda.peter.server;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

public class SessionAttributesHttp implements SessionAttributes {

    private HttpSession session;
    
    @Inject
    public SessionAttributesHttp(HttpSession session){
        this.session = session;
    }
    @Override
    public Object getAttribute(String name) {
        return session.getAttribute(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        session.setAttribute(name, value);

    }

}

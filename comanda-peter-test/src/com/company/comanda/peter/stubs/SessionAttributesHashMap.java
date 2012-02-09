package com.company.comanda.peter.stubs;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.company.comanda.peter.server.SessionAttributes;

@Singleton
public class SessionAttributesHashMap implements SessionAttributes {

    private Map<String, Object> map;
    
    @Inject
    public SessionAttributesHashMap(){
        map = new HashMap<String, Object>();
    }
    
    @Override
    public Object getAttribute(String name) {
        return map.get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        map.put(name, value);
    }

    public void clear(){
        map.clear();
    }
}

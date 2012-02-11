package com.company.comanda.peter.stubs;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.company.comanda.peter.server.SessionAttributes;

@Singleton
public class SessionAttributesHashMap implements SessionAttributes {

    private static ThreadLocal<Map<String, Object>> map = 
            new ThreadLocal<Map<String,Object>>();
    
    @Inject
    public SessionAttributesHashMap(){
        if(map.get() == null){
            map.set(new HashMap<String, Object>());
        }
    }
    
    @Override
    public Object getAttribute(String name) {
        return map.get().get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        map.get().put(name, value);
    }

    public void clear(){
        map.get().clear();
    }
}

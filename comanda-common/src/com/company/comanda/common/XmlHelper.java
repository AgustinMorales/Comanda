package com.company.comanda.common;

import java.util.Map;
import java.util.Set;

public class XmlHelper {
    public static final String open(String tag){
        return "<" + tag + ">";
    }
    
    public static final String open(String tag, Map<String,String> atts){
        StringBuilder result = new StringBuilder("<");
        result.append(tag);
        result.append(" ");
        Set<String> keys = atts.keySet();
        for(String key: keys){
            String value = atts.get(key);
            result.append(key);
            result.append("=");
            result.append("\"");
            result.append(value);
            result.append("\"");
            result.append(" ");
        }
        if(keys.size() > 0){
            result.deleteCharAt(result.length() - 1);
        }
        result.append(">");
        return result.toString();
    }
    
    public static final String close(String tag){
        return "</" + tag + ">";
    }
    
    public static final String enclose(String tag, String contents){
        return open(tag) + contents + close(tag);
    }
    
    public static final String enclose(String tag, Map<String, String> atts, String contents){
        return open(tag, atts) + contents + close(tag);
    }
}

package com.company.comanda.common;

public class XmlHelper {
    public static final String open(String tag){
        return "<" + tag + ">";
    }
    
    public static final String close(String tag){
        return "</" + tag + ">";
    }
    
    public static final String enclose(String tag, String contents){
        return open(tag) + contents + close(tag);
    }
}

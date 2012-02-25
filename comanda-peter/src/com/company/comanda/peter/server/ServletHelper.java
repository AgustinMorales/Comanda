package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.company.comanda.common.XmlTags;

public class ServletHelper {
    public static PrintWriter getXmlWriter(HttpServletResponse resp) throws IOException{
        resp.setContentType("text/xml; charset=ISO-8859-1");
        PrintWriter out = resp.getWriter();
        out.println(XmlTags.HEADER);
        
        return out;
    }
    
    public static void logParameters(HttpServletRequest req, Logger log){
        if(log.isInfoEnabled()){
            @SuppressWarnings("unchecked")
            Map<String,String[]> parameterMap = req.getParameterMap();
            Set<String> keys = parameterMap.keySet();
            StringBuffer out = new StringBuffer();
            out.append("{");
            for(String key : keys){
                out.append(key);
                out.append("= ");
                String[] values = parameterMap.get(key);
                out.append("[");
                for(String value : values){
                    out.append(value);
                    if(values.length>1){
                        out.append(",");
                    }
                }
                if(values.length > 1){
                    out.deleteCharAt(out.length()-1);
                }
                out.append("]");
                out.append(", ");
            }
            out.deleteCharAt(out.length() -1);
            out.append("}");
            log.info("Parameters: {}", out );
        }
    }
}

package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.company.comanda.common.XmlTags;

public class ServletHelper {
    public static PrintWriter getXmlWriter(HttpServletResponse resp) throws IOException{
        resp.setContentType("text/xml; charset=ISO-8859-1");
        PrintWriter out = resp.getWriter();
        out.println(XmlTags.HEADER);
        
        return out;
    }
}

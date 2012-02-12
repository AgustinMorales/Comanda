package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ServletHelper {
    public static PrintWriter getXmlWriter(HttpServletResponse resp) throws IOException{
        resp.setContentType("text/xml; charset=ISO-8859-1");
        PrintWriter out = resp.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        
        return out;
    }
}

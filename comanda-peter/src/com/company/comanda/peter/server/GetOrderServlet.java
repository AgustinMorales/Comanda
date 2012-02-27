package com.company.comanda.peter.server;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class GetOrderServlet extends HttpServlet{
    
    /**
     * 
     */
    private static final long serialVersionUID = -3850715977073789224L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        doPost(req, resp);
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        
    }
    
}

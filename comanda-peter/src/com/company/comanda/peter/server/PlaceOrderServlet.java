package com.company.comanda.peter.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PlaceOrderServlet extends HttpServlet  
{

    /**
     * 
     */
    private static final long serialVersionUID = 7406683513326724866L;
    
    private ItemsManager itemsManager = ItemsManager.me();

    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        doPost(req, resp);
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String keyId = req.getParameter("keyId");
        String table = req.getParameter("table");
        itemsManager.placeOrder(itemsManager.getRestaurantId(), 
                itemsManager.getUserId(),
                Long.parseLong(keyId), table);
    }
}

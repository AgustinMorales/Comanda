package com.company.comanda.peter.server;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class PlaceOrderServlet extends HttpServlet  
{

    /**
     * 
     */
    private static final long serialVersionUID = 7406683513326724866L;
    
    private UserManager userManager;

    @Inject
    public PlaceOrderServlet(UserManager manager){
        this.userManager = manager;
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        doPost(req, resp);
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        long menuItemId = Long.parseLong(
                req.getParameter("keyId"));
        long tableId = Long.parseLong(req.getParameter("table"));
        long userId = Long.parseLong(req.getParameter("userId"));
        String password = req.getParameter("password");
        long restaurantId = Long.parseLong(
                req.getParameter("restaurantId"));
        
        //FIXME: What happens in case of error?
        userManager.placeOrder(userId, password, 
                restaurantId, menuItemId, tableId);
    }
}

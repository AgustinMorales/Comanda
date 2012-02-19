package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.company.comanda.common.HttpParams.PlaceOrder.*;
import static com.company.comanda.common.XmlHelper.*;
import static com.company.comanda.common.XmlTags.BooleanResult.*;

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
        String menuItemIds = req.getParameter(PARAM_ITEM_IDS);
        long tableId = Long.parseLong(req.getParameter(PARAM_TABLE_ID));
        long userId = Long.parseLong(req.getParameter(PARAM_USER_ID));
        String password = req.getParameter(PARAM_PASSWORD);
        long restaurantId = Long.parseLong(
                req.getParameter(PARAM_RESTAURANT_ID));
        
        //FIXME: What happens in case of error?
        String[] items = menuItemIds.split("|");
        for (String item : items){
            long menuItemId = Long.parseLong(item);
            userManager.placeOrder(userId, password, 
                    restaurantId, menuItemId, tableId);
        }
        PrintWriter out = ServletHelper.getXmlWriter(resp);
        out.println(enclose(RESULT, "" + true));
    }
}

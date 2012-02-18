package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.comanda.peter.server.model.MenuItem;
import static com.company.comanda.common.XmlHelper.*;
import static com.company.comanda.common.XmlTags.MenuItemList.*;
import static com.company.comanda.common.HttpParams.GetMenuItems.*;

@Singleton
public class GetItemsServlet extends HttpServlet  
{ 

    /**
     * 
     */
    private static final long serialVersionUID = 5142871744485848351L;
    
    private UserManager userManager;
    
    @Inject
    public GetItemsServlet(UserManager userManager){
        this.userManager = userManager;
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }



    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String restaurantId = req.getParameter(PARAM_RESTAURANT_ID);
        List<MenuItem> items = userManager.getMenuItems(Long.parseLong(restaurantId));
        
        PrintWriter out = ServletHelper.getXmlWriter(resp);
        out.println(open(ITEM_LIST));
        //loop through items list and print each item
        for (MenuItem i : items) 
        {
            out.println(open(ITEM));
            out.println(enclose(ID, "" + i.getId()));
            out.println(enclose(NAME, i.getName()));
            out.println(enclose(DESCRIPTION, i.getDescription()));
            out.println(enclose(IMAGE_STRING, i.getImageString()));
            out.println(enclose(CATEGORY_ID, "" + i.getCategory().getId()));
            out.println(close(ITEM));
        }
        out.println(close(ITEM_LIST));
        // Flush writer
        out.flush();


    }
}

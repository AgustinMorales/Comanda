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

import com.company.comanda.peter.server.model.MenuCategory;

import static com.company.comanda.common.HttpParams.GetCategories.*;
import static com.company.comanda.common.XmlTags.CategoryList.*;
import static com.company.comanda.common.XmlHelper.*;

@Singleton
public class GetCategoriesServlet extends HttpServlet  
{ 

    /**
     * 
     */
    private static final long serialVersionUID = 5142871744485848351L;
    
    private UserManager userManager;
    
    @Inject
    public GetCategoriesServlet(UserManager userManager){
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
        List<MenuCategory> categories = 
                userManager.getMenuCategories(Long.parseLong(restaurantId));
        
        PrintWriter out = ServletHelper.getXmlWriter(resp);
        out.println(open(CATEGORY_LIST));
        //loop through items list and print each item
        for (MenuCategory i : categories) 
        {
            out.println(open(CATEGORY));
            out.println(enclose(ID, "" + i.getId()));
            out.println(enclose(NAME, i.getName()));
            out.println(close(CATEGORY));
        }
        out.println(close(CATEGORY_LIST));
        // Flush writer
        out.flush();


    }
}

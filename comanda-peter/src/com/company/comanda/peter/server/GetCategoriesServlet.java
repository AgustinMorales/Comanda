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
        String restaurantId = req.getParameter("restaurantId");
        List<MenuCategory> categories = 
                userManager.getMenuCategories(Long.parseLong(restaurantId));
        
        resp.setContentType("text/xml; charset=ISO-8859-1");
        PrintWriter out = resp.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        out.println("<CategoryList>");
        //loop through items list and print each item
        for (MenuCategory i : categories) 
        {
            out.println("\n\t<Category>");
            out.println("\n\t\t<Id>" + i.getId() + "</Id>");
            out.println("\n\t\t<Name>" + i.getName() + "</Name>");
            out.println("\n\t</Category>");
        }
        out.println("\n</CategoryList>");
        // Flush writer
        out.flush();


    }
}

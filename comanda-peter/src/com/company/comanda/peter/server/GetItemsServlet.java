package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.comanda.peter.server.model.MenuItem;

public class GetItemsServlet extends HttpServlet  
{ 

    /**
     * 
     */
    private static final long serialVersionUID = 5142871744485848351L;
    private ItemsManager itemsManager = ItemsManager.me();

    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }



    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        //FIXME
        List<MenuItem> items = itemsManager.getMenuItems(0);
        PrintWriter out = resp.getWriter();
        out.println("<ItemList>");
        //loop through items list and print each item
        for (MenuItem i : items) 
        {
            out.println("\n\t<Item>");
            out.println("\n\t\t<KeyId>" + i.getId() + "</KeyId>");
            out.println("\n\t\t<Name>" + i.getName() + "</Name>");
            out.println("\n\t\t<Description>" + i.getDescription() + "</Description>");
            out.println("\n\t\t<ImageString>" + i.getImageString() + "</ImageString>");
            out.println("\n\t</Item>");
        }
        out.println("\n</ItemList>");
        // Flush writer
        out.flush();


    }
}

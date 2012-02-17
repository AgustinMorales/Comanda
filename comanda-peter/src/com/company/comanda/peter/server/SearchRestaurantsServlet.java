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

import com.company.comanda.peter.server.model.Restaurant;

@Singleton
public class SearchRestaurantsServlet extends HttpServlet  
{ 

    /**
     * 
     */
    private static final long serialVersionUID = 5142871744485848351L;
    
    private static final String PARAM_LATITUDE = "latitude";
    private static final String PARAM_LONGITUDE = "longitude";
    
    
    private static final int defaultMaxResults = 50;
    private static final double defaultRadius = 6000;
    
    private UserManager userManager;
    
    @Inject
    public SearchRestaurantsServlet(UserManager userManager){
        this.userManager = userManager;
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }



    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        final double latitude = Double.parseDouble(req.getParameter(PARAM_LATITUDE));
        final double longitude = Double.parseDouble(req.getParameter(PARAM_LONGITUDE));
        final int maxResults = defaultMaxResults;
        final double radius = defaultRadius;
        List<Restaurant> items = userManager.searchRestaurant(latitude, longitude, maxResults, radius);
        
        resp.setContentType("text/xml; charset=ISO-8859-1");
        PrintWriter out = resp.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        out.println("<RestaurantList>");
        //loop through items list and print each item
        for (Restaurant i : items) 
        {
            out.println("\n\t<Restaurant>");
            out.println("\n\t\t<Id>" + i.getId() + "</Id>");
            out.println("\n\t\t<Name>" + i.getName() + "</Name>");
            out.println("\n\t</Restaurant>");
        }
        out.println("\n</RestaurantList>");
        // Flush writer
        out.flush();


    }
}

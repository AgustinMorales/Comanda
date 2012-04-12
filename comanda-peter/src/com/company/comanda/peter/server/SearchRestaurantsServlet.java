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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.company.comanda.common.XmlHelper.*;
import static com.company.comanda.common.XmlTags.Restaurantlist.*;
import static com.company.comanda.common.HttpParams.SearchRestaurants.*;

import com.company.comanda.peter.server.model.Restaurant;

@Singleton
public class SearchRestaurantsServlet extends HttpServlet  
{ 

    /**
     * 
     */
    private static final long serialVersionUID = 5142871744485848351L;
    
    private static final Logger log = 
            LoggerFactory.getLogger(SearchRestaurantsServlet.class);
    
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
        log.info("Parameters: {}", req.getParameterMap());
        final double latitude = Double.parseDouble(req.getParameter(PARAM_LATITUDE));
        final double longitude = Double.parseDouble(req.getParameter(PARAM_LONGITUDE));
        final int maxResults = defaultMaxResults;
        final double radius = defaultRadius;
        List<Restaurant> items = userManager.searchRestaurant(latitude, longitude, maxResults, radius);
        
        resp.setContentType("text/xml; charset=ISO-8859-1");
        PrintWriter out = resp.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        out.println(open(RESTAURANT_LIST));
        //loop through items list and print each item
        for (Restaurant i : items) 
        {
            out.println(open(RESTAURANT));
            out.println(enclose(ID, "" + i.getId()));
            out.println(enclose(DESCRIPTION, i.getDescription()));
            String imageURL = i.getImageUrl();
            if(imageURL == null){
                imageURL = "";
            }
            out.println(enclose(IMAGE_URL, imageURL));
            out.println(enclose(NAME, i.getName()));
            out.println(enclose(DELIVERY_COST, new Float(i.getDeliveryCost()).toString()));
            out.println(enclose(MINIMUM_FOR_DELIVERY, 
                    new Float(i.getMinimumForDelivery()).toString()));
            out.println(close(RESTAURANT));
            
        }
        out.println(close(RESTAURANT_LIST));
        // Flush writer
        out.flush();


    }
}

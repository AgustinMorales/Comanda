package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@Singleton
public class NewRestaurantServlet extends HttpServlet{

    /**
     * 
     */
    private static final long serialVersionUID = -5754007697909915549L;
    
    private static final Logger log = LoggerFactory.getLogger(NewRestaurantServlet.class);

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        
    private ComandaAdmin admin;
    
    @Inject
    public NewRestaurantServlet(ComandaAdmin admin){
        this.admin = admin;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletHelper.logParameters(req, log);
        String name = req.getParameter("name");
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String address = req.getParameter("address");
        String description = req.getParameter("description");
        String phone = req.getParameter("phone");
        String restaurantKeyString = req.getParameter("restaurantKeyString");
        String deliveryCostString = req.getParameter("deliveryCost");
        String minimumForDeliveryString = req.getParameter("minimumForDelivery");
        String copyFromRestKeyString = req.getParameter("copyFromRestKeyString");
        String imageBlobKey = null;
        List<BlobKey> blobKeyList = null;
        try{
        	Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
            blobKeyList = blobs.get("restaurantImage");
        }
        catch(IllegalStateException e){
        	log.warn("Looks like we have no upload framework. Will use null image", e);
        }
        if(blobKeyList != null){
            assert blobKeyList.size() == 1;
            imageBlobKey = blobKeyList.get(0).getKeyString();
        }
        if(password != null && password.length() == 0){
            password = null;
        }
        if(restaurantKeyString != null && restaurantKeyString.length() == 0){
            restaurantKeyString = null;
        }
        if(copyFromRestKeyString != null && copyFromRestKeyString.length() == 0){
            copyFromRestKeyString = null;
        }
        float deliveryCost = Float.parseFloat(deliveryCostString);
        float minimumForDelivery = Float.parseFloat(minimumForDeliveryString);
        admin.createOrModifyRestaurant(restaurantKeyString,
                name, login, 
                password, address, 
                description, imageBlobKey,
                phone,
                deliveryCost,
                minimumForDelivery,
                copyFromRestKeyString);
        
        PrintWriter out = resp.getWriter();
        out.print("SUCCESS");
    }



}

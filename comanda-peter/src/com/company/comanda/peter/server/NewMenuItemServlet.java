package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

import com.company.comanda.peter.shared.Constants;
import com.company.comanda.peter.shared.Qualifiers;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@Singleton
public class NewMenuItemServlet extends HttpServlet{

    /**
     * 
     */
    private static final long serialVersionUID = -5754007697909915549L;
    
    private static final Logger log = LoggerFactory.getLogger(NewMenuItemServlet.class);

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        
    private RestaurantManager manager;
    
    @Inject
    public NewMenuItemServlet(RestaurantManager manager){
        this.manager = manager;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletHelper.logParameters(req, log);
        String keyId = req.getParameter("keyId");
        String itemName = req.getParameter("itemName");
        String description = req.getParameter("description");
        String pricingScheme = req.getParameter("priceScheme");
        String extrasName = req.getParameter("extrasName");
        List<String> extras = new ArrayList<String>();
        List<Float> extrasPrices = new ArrayList<Float>();
        for(int i=0;i<Constants.NO_OF_EXTRAS_IN_UI; i++){
            String currentExtraName = req.getParameter("extra" + (i+1));
            if(currentExtraName != null && currentExtraName.length() == 0){
                currentExtraName = null;
            }
            if(currentExtraName != null){
                extras.add(currentExtraName);
                extrasPrices.add(price(req, "extraPrice" + (i+1)));
            }
        }
        Long categoryId = Long.parseLong(req.getParameter("categoryId"));
        List<BlobKey> blobKeyList = null;
        String imageBlobKey = null;
        
        try{
        	Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
            blobKeyList = blobs.get("itemImage");
        }
        catch(IllegalStateException e){
        	log.warn("Looks like we have no upload framework. Will use null image", e);
        }
        if(blobKeyList != null){
            assert blobKeyList.size() == 1;
            imageBlobKey = blobKeyList.get(0).getKeyString();
        }
        //persist
        Long itemId = null;
        if(keyId != null && keyId.length() > 0){
            itemId = Long.parseLong(keyId);
        }
        
        List<Float> prices = new ArrayList<Float>();
        List<String> qualifiers = new ArrayList<String>();
        
        if(pricingScheme.equals("single")){
        	prices.add(Float.parseFloat(req.getParameter("singlePrice")));
        	qualifiers.add(Qualifiers.SINGLE.toString());
        }
        else{
        	boolean cbSmall = cb(req,"cbSmall");
        	boolean cbMedium = cb(req,"cbMedium");
        	boolean cbLarge = cb(req,"cbLarge");
        	boolean cbTapa = cb(req, "cbTapa");
        	boolean cbHalf = cb(req, "cbHalf");
        	boolean cbFull = cb(req, "cbFull");
        	if(cbSmall){
        		qualifiers.add(Qualifiers.SMALL.toString());
        		prices.add(price(req,"smallPrice"));
        	}
        	if(cbMedium){
        		qualifiers.add(Qualifiers.MEDIUM.toString());
        		prices.add(price(req,"mediumPrice"));
        	}
        	if(cbLarge){
        		qualifiers.add(Qualifiers.LARGE.toString());
        		prices.add(price(req,"largePrice"));
        	}
        	if(cbTapa){
        		qualifiers.add(Qualifiers.TAPA.toString());
        		prices.add(price(req,"tapaPrice"));
        	}
        	if(cbHalf){
        		qualifiers.add(Qualifiers.HALF.toString());
        		prices.add(price(req,"halfPrice"));
        	}
        	if(cbFull){
        		qualifiers.add(Qualifiers.FULL.toString());
        		prices.add(price(req,"fullPrice"));
        	}
        }
        if(extrasName != null && extrasName.length() == 0){
            extrasName = null;
        }
        manager.getAgent().addOrModifyMenuItem(itemId, 
                itemName, description, prices, qualifiers, 
                imageBlobKey, categoryId, extras, extrasPrices, extrasName);
        PrintWriter out = resp.getWriter();
        out.println("SUCCESS");
    }


    private boolean cb(HttpServletRequest req, String name){
    	String parameter = req.getParameter(name);
    	return ( parameter != null && parameter.equals("on"));
    }

    private float price(HttpServletRequest req, String name){
        String priceString = req.getParameter(name);
        float price = 0;
        if(priceString != null && priceString.length() > 0){
            price = Float.parseFloat(req.getParameter(name));
        }
    	return price;
    }
}

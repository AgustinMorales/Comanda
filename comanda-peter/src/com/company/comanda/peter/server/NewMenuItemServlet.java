package com.company.comanda.peter.server;

import java.io.IOException;
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
        log.debug("Parameters: {}", req.getParameterMap());
        String keyId = req.getParameter("keyId");
        String itemName = req.getParameter("itemName");
        String priceString = req.getParameter("price");
        String description = req.getParameter("description");
        Long categoryId = Long.parseLong(req.getParameter("categoryId"));
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeyList = blobs.get("itemImage");
        String imageBlobKey = null;
        if(blobKeyList != null){
            assert blobKeyList.size() == 1;
            imageBlobKey = blobKeyList.get(0).getKeyString();
        }
        //persist
        Long itemId = null;
        if(keyId != null && keyId.length() > 0){
            itemId = Long.parseLong(keyId);
        }
        manager.getAgent().addOrModifyMenuItem(itemId, 
                itemName, description, priceString, 
                imageBlobKey, categoryId);
    }



}

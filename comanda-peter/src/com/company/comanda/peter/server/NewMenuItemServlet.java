package com.company.comanda.peter.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@Singleton
public class NewMenuItemServlet extends HttpServlet{

    /**
     * 
     */
    private static final long serialVersionUID = -5754007697909915549L;

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    
    private ItemsManager itemsManager = ItemsManager.me();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String keyId = req.getParameter("keyId");
        String itemName = req.getParameter("itemName");
        String priceString = req.getParameter("price");
        String description = req.getParameter("description");
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
        itemsManager.addOrModifyMenuItem(itemId, 
                itemName, description, 
                priceString, imageBlobKey,
                itemsManager.getRestaurantId());
    }



}

package com.company.comanda.peter.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.comanda.peter.server.model.MenuItem;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class NewMenuItemServlet extends HttpServlet{

    /**
     * 
     */
    private static final long serialVersionUID = -5754007697909915549L;

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        MenuItem item = null;
        String keyId = req.getParameter("keyId");
        PersistenceManager pm = PMF.get().getPersistenceManager();
        String itemName = req.getParameter("itemName");
        String priceString = req.getParameter("price");
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeyList = blobs.get("itemImage");
        if(keyId != null && keyId.length() > 0){
            item = pm.getObjectById(MenuItem.class, Long.parseLong(keyId));
        }
        else{
            if(itemName == null ||
                    priceString == null ||
                    blobKeyList == null){
                throw new IllegalArgumentException("Missing data");
            }
            item = new MenuItem();
        }
        if(itemName != null){
            item.setName(itemName);
        }
        if(priceString != null){
            item.setPrice(Integer.parseInt(priceString));
        }
        if(blobKeyList != null){
            assert blobKeyList.size() == 1;
            item.setImageString(blobKeyList.get(0).getKeyString());
        }
        //persist
        try{ pm.makePersistent(item); }
        finally{ pm.close(); }
    }



}

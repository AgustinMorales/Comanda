package com.company.comanda.peter.server;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.common.HttpParams.BlobServer;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@Singleton
public class ServeBlob extends HttpServlet  
{
    /**
     * 
     */
    private static final long serialVersionUID = 8488458599057747017L;
    
    private static final Logger log = LoggerFactory.getLogger(ServeBlob.class);
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        ServletHelper.logParameters(req, log);
        BlobKey blobKey = new BlobKey(req.getParameter(BlobServer.PARAM_ID));
        blobstoreService.serve(blobKey, resp);
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        doGet(req, resp);
    }
}

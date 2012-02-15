package com.company.comanda.brian.helpers;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;

public interface HttpRetriever {

    InputStream execute(HttpPost httpPost) 
            throws IllegalStateException, 
            ClientProtocolException, 
            IOException;
}

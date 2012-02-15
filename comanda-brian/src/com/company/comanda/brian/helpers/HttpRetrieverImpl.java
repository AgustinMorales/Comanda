package com.company.comanda.brian.helpers;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpRetrieverImpl implements HttpRetriever {

    @Override
    public InputStream execute(HttpPost httpPost) 
            throws IllegalStateException, ClientProtocolException, 
            IOException {
        HttpClient httpclient = new DefaultHttpClient();
        return httpclient.execute(httpPost).getEntity().getContent();
    }

}

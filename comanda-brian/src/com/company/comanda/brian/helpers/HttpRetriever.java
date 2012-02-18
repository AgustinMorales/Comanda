package com.company.comanda.brian.helpers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;

import android.util.Log;

import com.company.comanda.brian.helpers.HttpRetriever;

public class HttpRetriever {

    private static final String DECODE_QR_RESPONSE = 
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<Data>\n" + 
                "\t<Restaurant>\n" + 
                "\t\t<Name>Restaurant name</Name>\n" + 
                "\t\t<Id>Restaurant ID</Id>\n" + 
                "\t</Restaurant>\n" + 
                "\t<Table>\n" + 
                "\t\t<Name>Table name</Name>\n" + 
                "\t\t<Id>Table ID</Id>\n" + 
                "\t</Table>\n" + 
                "</Data>";
    
    private static final String SEARCH_RESTAURANTS_RESPONSE = 
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<RestaurantList>\n" + 
                "\t<Restaurant>\n" + 
                "\t\t<Name>Casa Manuela</Name>\n" + 
                "\t\t<Id>123</Id>\n" + 
                "\t</Restaurant>\n" + 
                "\t<Restaurant>\n" + 
                "\t\t<Name>La Tagliatella</Name>\n" + 
                "\t\t<Id>1234</Id>\n" + 
                "\t</Restaurant>\n" +
                "\t<Restaurant>\n" + 
                "\t\t<Name>Ginos</Name>\n" + 
                "\t\t<Id>12345</Id>\n" + 
                "\t</Restaurant>\n" +
                "</RestaurantList>";
    
    
    public InputStream execute(HttpPost httpPost) throws IllegalStateException,
            ClientProtocolException, IOException {
        final String uri = httpPost.getURI().toString();
        String response = "";
        if(uri.contains("/searchRestaurants")){
            response = SEARCH_RESTAURANTS_RESPONSE;
        }
        return new ByteArrayInputStream(
                response.getBytes("ISO-8859-1"));
    }

}

package com.company.comanda.brian.test.stubs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;

import com.company.comanda.brian.helpers.HttpRetriever;

public class StubHttpRetriever implements HttpRetriever {

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
    
    
    @Override
    public InputStream execute(HttpPost httpPost) throws IllegalStateException,
            ClientProtocolException, IOException {
        return new ByteArrayInputStream(
                DECODE_QR_RESPONSE.getBytes("ISO-8859-1"));
    }

}

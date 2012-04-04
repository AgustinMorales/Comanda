package com.company.comanda.peter.server.notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.common.HttpParams;

public class TwilioPhoneNotifier implements PhoneNotifier {


    private static final Logger log = LoggerFactory.
            getLogger(TwilioPhoneNotifier.class);

    @Inject
    public TwilioPhoneNotifier(){
        super();
    }
    @Override
    public boolean call(String phone) {
        boolean result = true;
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();

            httpClient.getCredentialsProvider().setCredentials(
                    new AuthScope("api.twilio.com", 443),
                    new UsernamePasswordCredentials("AC1826ab848e014c0b87b815d245af96e2",
                            "8da7bb628fbc6c80a0c7c667f63f0bd6"));

            HttpPost httppost = new HttpPost(
                    "https://api.twilio.com/2010-04-01/Accounts/" +
                    "AC1826ab848e014c0b87b815d245af96e2/Calls");

            List <NameValuePair> parameters = new ArrayList <NameValuePair>();
            parameters.add(new BasicNameValuePair("From", "+34696679754"));
            parameters.add(new BasicNameValuePair("To", phone));
            parameters.add(new BasicNameValuePair("Url", 
                    "http://www.comandamobile.com/twilio.xml"));
            parameters.add(new BasicNameValuePair("StatusCallback", 
                    HttpParams.BillNotificationEnded.SERVICE_NAME));

            UrlEncodedFormEntity sendentity = 
                    new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
            httppost.setEntity(sendentity); 

            System.out.println("executing request" + httppost.getRequestLine());
            HttpResponse response = httpClient.execute(httppost);
            HttpEntity entity = response.getEntity();

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            if (entity != null) {
                System.out.println("Response content length: " + entity.getContentLength());
            }
            if (entity != null) {
                entity.consumeContent();
            }

            httpClient.getConnectionManager().shutdown();
        }
        catch(IOException e){
            log.error("Could not complete phone call request for phone {}", phone, e);
            result = false;
        }
        return result;
    }

}

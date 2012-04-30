package com.company.comanda.peter.server.notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.common.HttpParams;


public class TwilioPhoneNotifier implements PhoneNotifier {

    private static final String AUTH_USERNAME = "AC1826ab848e014c0b87b815d245af96e2";
    private static final String AUTH_PASSWORD = "8da7bb628fbc6c80a0c7c667f63f0bd6";
    
    private static final String URL = "https://api.twilio.com/2010-04-01/Accounts/" +
            "AC1826ab848e014c0b87b815d245af96e2/Calls";
    
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
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization",
            "Basic "+Base64.encodeBase64String((AUTH_USERNAME + ":" + AUTH_PASSWORD).getBytes()));
            connection.setRequestMethod("POST");
            StringBuffer data = new StringBuffer();
            data.append("From");
            data.append("=");
            data.append(URLEncoder.encode("+34696679754", "UTF-8"));
            data.append("&");
            data.append("To");
            data.append("=");
            data.append(URLEncoder.encode(phone, "UTF-8"));
            data.append("&");
            data.append("Url");
            data.append("=");
            data.append(URLEncoder.encode("http://www.comandamobile.com/phone/pending_bills.xml", "UTF-8"));
            data.append("&");
            data.append("StatusCallback");
            data.append("=");
            data.append(URLEncoder.encode("https://comandapeter.appspot.com" + 
            HttpParams.BillNotificationEnded.SERVICE_NAME, "UTF-8"));

            

            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            
            //write parameters
            writer.write(data.toString());
            writer.flush();
            
            // Get the response
            StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            writer.close();
            reader.close();
            
            //Output the response
            log.info("Twilio answer: {}", answer.toString());

        }
        catch(IOException e){
            log.error("Could not complete phone call request for phone {}", phone, e);
            result = false;
        }
        return result;
    }

}

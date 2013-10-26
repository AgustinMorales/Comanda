package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;




public class TestTwilio {

    
    private static final String AUTH_USERNAME = "AC1826ab848e014c0b87b815d245af96e2";
    private static final String AUTH_PASSWORD = "8da7bb628fbc6c80a0c7c667f63f0bd6";
    
    private static final String URL = "https://api.twilio.com/2010-04-01/Accounts/" +
            "AC1826ab848e014c0b87b815d245af96e2/Calls";
    public static void main(String[] args) throws ClientProtocolException, IOException{
//        try {
//            URL url = new URL ("https://AC1826ab848e014c0b87b815d245af96e2:8da7bb628fbc6c80a0c7c667f63f0bd6@api.twilio.com/2010-04-01/Accounts/AC1826ab848e014c0b87b815d245af96e2/Calls");
//
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            connection.setRequestProperty("From", "+34696679754");
//            connection.setRequestProperty("To", "+34696679754");
//            connection.setRequestProperty("Url", "http://www.comandamobile.com/twilio.xml");
//            
//            InputStream content = (InputStream)connection.getInputStream();
//            BufferedReader in   = 
//                new BufferedReader (new InputStreamReader (content));
//            String line;
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }

        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization",
        "Basic "+new String(Base64.encodeBase64((AUTH_USERNAME + ":" + AUTH_PASSWORD).getBytes())));
        connection.setRequestMethod("POST");
        StringBuffer data = new StringBuffer();
        data.append("From");
        data.append("=");
        data.append(URLEncoder.encode("+34696679754", "UTF-8"));
        data.append("&");
        data.append("To");
        data.append("=");
        data.append(URLEncoder.encode("+34676825652", "UTF-8"));
        data.append("&");
        data.append("Url");
        data.append("=");
        data.append(URLEncoder.encode("http://www.comandamobile.com/twilio.xml", "UTF-8"));
        

        

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
        System.out.println("Twilio answer: " + answer.toString());


    }
}

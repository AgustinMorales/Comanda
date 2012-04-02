package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;



public class TestTwilio {

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

        DefaultHttpClient httpClient = new DefaultHttpClient();

        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope("api.twilio.com", 443),
                new UsernamePasswordCredentials("AC1826ab848e014c0b87b815d245af96e2",
                        "8da7bb628fbc6c80a0c7c667f63f0bd6"));

        HttpPost httppost = new HttpPost("https://api.twilio.com/2010-04-01/Accounts/AC1826ab848e014c0b87b815d245af96e2/Calls");

        List <NameValuePair> parameters = new ArrayList <NameValuePair>();
        parameters.add(new BasicNameValuePair("From", "+34696679754"));
        parameters.add(new BasicNameValuePair("To", "+34954072259"));
        parameters.add(new BasicNameValuePair("Url", "http://www.comandamobile.com/twilio.xml"));
        
        UrlEncodedFormEntity sendentity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
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
}

package com.company.comanda.peter.server.notification;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.company.comanda.common.HttpParams;
import com.company.comanda.peter.server.model.Bill;
import com.company.comanda.peter.server.model.PhoneNotification;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;


public class TwilioPhoneNotifier implements PhoneNotifier {

    private static final String AUTH_USERNAME = "AC1826ab848e014c0b87b815d245af96e2";
    private static final String AUTH_PASSWORD = "8da7bb628fbc6c80a0c7c667f63f0bd6";
    
    private static final String URL = "https://api.twilio.com/2010-04-01/Accounts/" +
            "AC1826ab848e014c0b87b815d245af96e2/Calls";
    
    private static final Logger log = LoggerFactory.
            getLogger(TwilioPhoneNotifier.class);
    
    private Objectify ofy;

    @Inject
    public TwilioPhoneNotifier(Objectify ofy){
        super();
        this.ofy = ofy;
    }
    @Override
    public boolean call(String phone, String billKeyString) {
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
            InputStream in = connection.getInputStream();
            
            
            InputSource responseXML = new InputSource(in);
            final StringBuffer callSid = new StringBuffer();
            DefaultHandler handler = new DefaultHandler(){

                private boolean inSid;
                @Override
                public void characters(char[] arg0, int arg1, int arg2)
                        throws SAXException {
                    if(inSid){
                        callSid.append(new String(arg0, arg1, arg2));
                    }
                }

                @Override
                public void endElement(String namespaceURI, String localName,
                        String qName)
                        throws SAXException {
                    if(qName.equals("Sid")){
                        inSid = false;
                    }
                }

                @Override
                public void startElement(String namespaceURI, 
                        String localName, String qName, 
                        Attributes atts) throws SAXException {
                    if(qName.equals("Sid")){
                        inSid = true;
                    }
                }
                
                
            };
            
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                    SAXParser parser = factory.newSAXParser();
                    XMLReader xr = parser.getXMLReader();
                    xr.setContentHandler(handler);
                    xr.parse(responseXML);
            } catch (ParserConfigurationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (SAXException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }


            writer.close();
            in.close();
            
            //Output the response
            log.info("Call SID: {}", callSid);

            PhoneNotification notification = new PhoneNotification();
            notification.setCallSid(callSid.toString());
            notification.setBill(new Key<Bill>(billKeyString));
            ofy.put(notification);
        }
        catch(IOException e){
            log.error("Could not complete phone call request for phone {}", phone, e);
            result = false;
        }
        return result;
    }

}

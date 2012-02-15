package com.company.comanda.brian.helpers;


import java.io.IOException;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.company.comanda.brian.Constants;
import com.company.comanda.brian.xmlhandlers.ComandaXMLHandler;
import com.google.inject.Inject;

public abstract class AsyncGetData <T>{

    @Inject
    private HttpRetriever retriever;
    
    private ProgressDialog m_ProgressDialog = null; 

    /**
     * 
     * @param data Be ready for a null value if data retrieval failed
     */
    public void afterOnUIThread(T data, Activity activity){
        
    }

    /**
     * 
     * @param data Be ready for a null value if data retrieval failed
     */
    public void afterOnBackground(T data, Activity activity){
        
    }

    public void beforeOnBackground(List<NameValuePair> params, 
            Activity activity){
        
    }

    public void execute(final Activity activity, final String service, 
            final List<NameValuePair> params,
            final Class<? extends ComandaXMLHandler<T>> handlerClass){

        Runnable retrieveData = new Runnable()
        {
            @Override
            public void run() 
            {
                beforeOnBackground(params, activity);
                T data = null;
                //this is where we populate m_items (ArrayList<FoodMenuItem>) 
                //which we can get from XML
                //the XML can be updated via Google App-Engine
                try
                {
                    data = getData(service, handlerClass, params);
                } 
                catch (Exception e) 
                { 
                    Log.e("ListViewSampleApp", "Unable to retrieve data.", e);
                }
                final T finalData = data;
                afterOnBackground(finalData, activity);
                Runnable onUIRunnable = new Runnable() {

                    @Override
                    public void run() {
                        m_ProgressDialog.dismiss();
                        afterOnUIThread(finalData,activity);
                    }
                };
                //This executes returnRes (see above) which will use the 
                //ItemAdapter to display the contents of m_items
                activity.runOnUiThread(onUIRunnable);
            }
        };
        //Create a new Thread to run viewItems
        Thread thread =  new Thread(null, retrieveData, "MagentoBackground");
        thread.start();
        //Make a popup progress dialog while we fetch and parse the data
        m_ProgressDialog = ProgressDialog.show(activity, "Please wait...",
                "Retrieving data ...", true);
    }

    private T getData(String service,
            Class<? extends ComandaXMLHandler<T>> handlerClass,
                    List<NameValuePair> params) throws IOException
                    {
        T result = null;
        try 
        {
            // Create a URL we want to load some xml-data from.
            HttpPost httppost = new HttpPost("http://" + 
                    Constants.SERVER_LOCATION + service);

            // Add your data
            httppost.setEntity(new UrlEncodedFormEntity(params));


            // Get a SAXParser from the SAXPArserFactory.
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            // Get the XMLReader of the SAXParser we created.
            XMLReader xr = sp.getXMLReader();
            // Create a new ContentHandler and 
            //apply it to the XML-Rea der
            ComandaXMLHandler<T> xmlHandler = handlerClass.newInstance();
            xr.setContentHandler(xmlHandler);
            InputSource xmlInput = new InputSource(retriever.execute(httppost));
            xmlInput.setEncoding("ISO-8859-1");
            Log.e("ListViewSampleApp", "Input Source Defined: "+ 
                    xmlInput.toString());
            /* Parse the xml-data from our URL. */
            xr.parse(xmlInput);
            /* Parsing has finished. */
            /* XMLHandler now provides the parsed data to us. */
            result = xmlHandler.getParsedData(); 
        } 
        catch (Exception e) 
        {
            Log.e("ListViewSampleApp XMLParser", "XML Error", e);
        }

        return result;
                    }
}

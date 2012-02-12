package com.company.comanda.brian;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.company.comanda.brian.RestaurantAndTableXMLHandler.ParsedData;

public class SelectTableActivity extends Activity
{
    private ProgressDialog m_ProgressDialog = null; 
    private Runnable retrieveData;
    
    private static final int SCAN_CODE_ACTIVITY = 1;
    
    
    private class ProcessResult implements Runnable{

        private ParsedData data;
        
        public ProcessResult(ParsedData data){
            this.data = data;
        }
        
        @Override
        public void run() {
            if(data != null){
                Intent intent = new Intent(
                        SelectTableActivity.this.getApplicationContext(), 
                        ComandaActivity.class);
                intent.putExtra(ComandaActivity.EXTRA_REST_ID, 
                        data.restId);
                intent.putExtra(ComandaActivity.EXTRA_REST_NAME, 
                        data.restName);
                intent.putExtra(ComandaActivity.EXTRA_TABLE_ID, 
                        data.tableId);
                intent.putExtra(ComandaActivity.EXTRA_TABLE_NAME, 
                        data.tableName);
                startActivity(intent);
            }
            m_ProgressDialog.dismiss();
            
        }
        
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_table);
        Button read_code_button = (Button)findViewById(R.id.read_code_button);
        read_code_button.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, SCAN_CODE_ACTIVITY);
                
            }
        });
    }
    public void fetchContent(final String code)
    {
        
        //create a Runnable object that does the work 
        //of retrieving the XML data online
        //This will be run in a new Thread
        retrieveData = new Runnable()
        {
            @Override
            public void run() 
            {
                //this is where we populate m_items (ArrayList<FoodMenuItem>) 
                //which we can get from XML
                //the XML can be updated via Google App-Engine
                ParsedData data = null;
                try
                {
                    data = getData();
                } 
                catch (Exception e) 
                { 
                    Log.e("ListViewSampleApp", "Unable to retrieve data.", e);
                }
                Runnable processResult = new ProcessResult(data);
                runOnUiThread(processResult);
            }
        };
        //Create a new Thread to run viewItems
        Thread thread =  new Thread(null, retrieveData, "MagentoBackground");
        thread.start();
        //Make a popup progress dialog while we fetch and parse the data
        m_ProgressDialog = ProgressDialog.show(this, "Please wait...",
                "Retrieving data ...", true);
    }
    private ParsedData getData() throws IOException
    {
        ParsedData result = null;
        try 
        {
            // Create a URL we want to load some xml-data from.
            URL url = new URL("http://" + Constants.SERVER_LOCATION + "/getTables");
            // Get a SAXParser from the SAXPArserFactory.
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            // Get the XMLReader of the SAXParser we created.
            XMLReader xr = sp.getXMLReader();
            // Create a new ContentHandler and 
            //apply it to the XML-Rea der
            RestaurantAndTableXMLHandler xmlHandler = new RestaurantAndTableXMLHandler();
            xr.setContentHandler(xmlHandler);
            InputSource xmlInput = new InputSource(url.openStream());
            Log.e("SelectTableActivity", "Input Source Defined: "+ xmlInput.toString());
            /* Parse the xml-data from our URL. */
            xr.parse(xmlInput);
            /* Parsing has finished. */
            /* XMLHandler now provides the parsed data to us. */
            result = xmlHandler.getParsedData(); 
        } 
        catch (Exception e) 
        {
            Log.e("SelectTableActivity XMLParser", "XML Error", e);
        }
        return result;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SCAN_CODE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
               String contents = intent.getStringExtra("SCAN_RESULT");
               //TODO: Some contents validation would be great...
               fetchContent(contents);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled", 20);
            }
         }
        
      }
    
}

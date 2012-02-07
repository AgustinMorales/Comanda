package com.company.comanda.brian;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SelectTableActivity extends Activity
{
    private ProgressDialog m_ProgressDialog = null; 
    private ArrayList<String> tables = null;
    private ArrayAdapter<String> m_adapter;
    private Runnable viewItems;
    private int artificiallyTrigggeredSelections;
    
    
    
    protected synchronized int decreaseSelections() {
        if (artificiallyTrigggeredSelections > 0){
            artificiallyTrigggeredSelections--;
        }
        return artificiallyTrigggeredSelections;
    }
    protected synchronized void increaseSelections() {
        this.artificiallyTrigggeredSelections++;
    }
    private Runnable returnRes = new Runnable()
    {
        @Override
        public void run() 
        {
            if(tables != null && tables.size() > 0)
            {
                m_adapter.notifyDataSetChanged();
                m_adapter.clear();
                for(int i=0;i<tables.size();i++)
                    m_adapter.add(tables.get(i));
                increaseSelections();
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
            increaseSelections();
        }
    };
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
                IntentIntegrator integrator = new IntentIntegrator(SelectTableActivity.this);
                integrator.initiateScan();
                
            }
        });
        fetchContent();
    }
    public void fetchContent()
    {
        tables = new ArrayList<String>();
        //set ListView adapter to basic ItemAdapter 
        //(it's a coincidence they are both called Item)
        Spinner spinner = (Spinner) findViewById(R.id.select_table_spinner);
        this.m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tables);
        this.m_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(m_adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int pos, long id) {
                if (decreaseSelections() == 0){
                    String tableName = parent.getItemAtPosition(pos).toString();
                    Intent intent = new Intent(parent.getContext(), ComandaActivity.class);
                    intent.putExtra("tableName", tableName);
                    startActivity(intent);
                }
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                
            }
        });
        //create a Runnable object that does the work 
        //of retrieving the XML data online
        //This will be run in a new Thread
        viewItems = new Runnable()
        {
            @Override
            public void run() 
            {
                //this is where we populate m_items (ArrayList<FoodMenuItem>) 
                //which we can get from XML
                //the XML can be updated via Google App-Engine
                try
                {
                    getData();
                } 
                catch (Exception e) 
                { 
                    Log.e("ListViewSampleApp", "Unable to retrieve data.", e);
                }
                //This executes returnRes (see above) which will use the 
                //ItemAdapter to display the contents of m_items
                runOnUiThread(returnRes);
            }
        };
        //Create a new Thread to run viewItems
        Thread thread =  new Thread(null, viewItems, "MagentoBackground");
        thread.start();
        //Make a popup progress dialog while we fetch and parse the data
        m_ProgressDialog = ProgressDialog.show(this, "Please wait...",
                "Retrieving data ...", true);
    }
    private void getData() throws IOException
    {
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
            TablesXMLHandler xmlHandler = new TablesXMLHandler();
            xr.setContentHandler(xmlHandler);
            InputSource xmlInput = new InputSource(url.openStream());
            Log.e("ListViewSampleApp", "Input Source Defined: "+ xmlInput.toString());
            /* Parse the xml-data from our URL. */
            xr.parse(xmlInput);
            /* Parsing has finished. */
            /* XMLHandler now provides the parsed data to us. */
            tables = xmlHandler.getParsedData(); 
        } 
        catch (Exception e) 
        {
            Log.e("ListViewSampleApp XMLParser", "XML Error", e);
        }
    }
    //OPTIONS MENU STUFF
    //     @Override
    //     public boolean onCreateOptionsMenu(Menu menu) 
    //     {
    //         MenuInflater inflater = getMenuInflater();
    //         inflater.inflate(R.menu.menu, menu);
    //         return true;
    //     }
    //     @Override
    //     public boolean onOptionsItemSelected(MenuItem item) 
    //     {
    //         // Handle item selection
    //         switch (item.getItemId()) {
    //         case R.id.refresh:
    //             fetchContent();
    //             return true;
    //         default:
    //             return super.onOptionsItemSelected(item);
    //         }
    //     }    

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
          Toast.makeText(getApplicationContext(), scanResult.getContents(), 15).show();
        }
        // else continue with any other code you need in the method
        
      }
    
}

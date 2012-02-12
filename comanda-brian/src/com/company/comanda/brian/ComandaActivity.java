package com.company.comanda.brian;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.company.comanda.brian.model.FoodMenuItem;

public class ComandaActivity extends ListActivity
{
    private ProgressDialog m_ProgressDialog = null; 
    private ArrayList<FoodMenuItem> m_items = null;
    private ItemAdapter m_adapter;
    private Runnable viewItems;
    private String tableName;
    
    public static final int ORDER_PLACED_TOAST_DURATION = 3;
    
    public static final String EXTRA_TABLE_NAME = "tableName";
    public static final String EXTRA_TABLE_ID = "tableId";
    public static final String EXTRA_REST_NAME = "restaurantName";
    public static final String EXTRA_REST_ID = "restaurantId";
    
    private Runnable returnRes = new Runnable(){
        @Override
        public void run() 
        {
            if(m_items != null && m_items.size() > 0)
            {
                m_adapter.notifyDataSetChanged();
                m_adapter.clear();
                for(int i=0;i<m_items.size();i++)
                    m_adapter.add(m_items.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Bundle extras = getIntent().getExtras();
        tableName = extras.getString("tableName");
        TextView tableNameTextView = (TextView)findViewById(R.id.tableNametextView);
        tableNameTextView.setText(getString(R.string.you_are_at_table) + " " + tableName);
        fetchContent();
    }
    public void fetchContent()
    {
        m_items = new ArrayList<FoodMenuItem>();
        //set ListView adapter to basic ItemAdapter 
        //(it's a coincidence they are both called Item)
        this.m_adapter = new ItemAdapter(this, R.layout.row, m_items);
        setListAdapter(this.m_adapter);
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
            URL url = new URL("http://" + 
                    Constants.SERVER_LOCATION + "/menuitems");
            // Get a SAXParser from the SAXPArserFactory.
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            // Get the XMLReader of the SAXParser we created.
            XMLReader xr = sp.getXMLReader();
            // Create a new ContentHandler and 
            //apply it to the XML-Rea der
            XMLHandler xmlHandler = new XMLHandler();
            xr.setContentHandler(xmlHandler);
            InputSource xmlInput = new InputSource(url.openStream());
            xmlInput.setEncoding("ISO-8859-1");
            Log.e("ListViewSampleApp", "Input Source Defined: "+ xmlInput.toString());
            /* Parse the xml-data from our URL. */
            xr.parse(xmlInput);
            /* Parsing has finished. */
            /* XMLHandler now provides the parsed data to us. */
            m_items = xmlHandler.getParsedData(); 
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

    
    private class PlaceOrderTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... keyIds) {
            assert keyIds.length == 1;
            String keyId = keyIds[0];
            
            
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + 
                    Constants.SERVER_LOCATION + "/placeOrder");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("keyId", keyId));
                nameValuePairs.add(new BasicNameValuePair("table", tableName));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                httpclient.execute(httppost);
                
            } catch (ClientProtocolException e) {
                keyId = "ERROR!!!";
            } catch (IOException e) {
                keyId = "ERROR!!!";
            }
            
            
            return keyId;
        }


        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), 
                    R.string.order_placed, ORDER_PLACED_TOAST_DURATION).show();
        }
    }


    /*
     * PRIVATE ADAPTER CLASS. Assigns data to be displayed on the listview
     */
    private class ItemAdapter extends ArrayAdapter<FoodMenuItem> 
    {
        //Hold array of items to be displayed in the list
        private ArrayList<FoodMenuItem> items;

        public ItemAdapter(Context context, int textViewResourceId,
                ArrayList<FoodMenuItem> items) 
        {
            super(context, textViewResourceId, items);
            this.items = items;// TODO Auto-generated catch block
        }
        //This method returns the actual view
        //that is displayed as a row (we will inflate with row.xml)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
            View v = convertView;
            if (v == null) 
            {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //inflate using res/layout/row.xml
                v = vi.inflate(R.layout.row, null);
            }
            //get the FoodMenuItem corresponding to 
            //the position in the list we are rendering
            FoodMenuItem o = items.get(position);
            if (o != null) 
            {
                //Set all of the UI components 
                //with the respective Object data
                ImageView icon = (ImageView) v.findViewById(R.id.icon);
                TextView tt = (TextView) v.findViewById(R.id.toptext);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                Button placeOrderButton = (Button)v.findViewById(R.id.placeorderbutton);
                final String menuItemName = o.getName();
                final String menuItemKeyId = o.getKeyId();
                final String menuItemDescription = o.getDescription();
                if (tt != null)
                {
                    tt.setText("Name: " + menuItemName);   
                }
                if(bt != null)
                {
                    bt.setText("No description");
                }
                placeOrderButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        (new PlaceOrderTask()).execute(menuItemKeyId);

                    }
                });
                Bitmap rawBitMap = null;
                
                if(icon != null)
                {
                    URL imageURL = null;
                    try      
                    {        
                        //use our image serve page to get the image URL
                        imageURL = new URL("http://" + Constants.SERVER_LOCATION + "/serveBlob?id="
                                + o.getImageString());
                    } 
                    catch (MalformedURLException e) 
                    {
                        e.printStackTrace();
                    }
                    try 
                    {
                        //Decode and resize the image then set as the icon
//                        BitmapFactory.Options options = new BitmapFactory
//                                .Options();
//                        options.inJustDecodeBounds = true;
//                        options.inSampleSize = 1/2;
                        InputStream bitmapIS = (InputStream)imageURL
                                .getContent();
                        rawBitMap = BitmapFactory
                                .decodeStream(bitmapIS);
                        bitmapIS.close();
                        if(rawBitMap != null){
                            Bitmap finImg = Bitmap
                                    .createScaledBitmap(rawBitMap, 50, 50, false);
                            icon.setImageBitmap(finImg);
                        }
                    } 
                    catch (IOException e) 
                    {                        
                        e.printStackTrace();
                    }
                }
                //returns the view to the Adapter to be displayed

                final Bitmap bitMap = rawBitMap;
                v.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = (LayoutInflater)
                                ComandaActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.menu_item_details, null, false);
                        
                        final PopupWindow pw = new PopupWindow(
                                view, 
                                250, 
                                300, 
                                true);
                        pw.setBackgroundDrawable(null);
                        TextView text = (TextView) view.findViewById(R.id.contextMenuItemDescription);
                        text.setText(menuItemDescription);
                        TextView textName = (TextView) view.findViewById(R.id.contextMenuItemName);
                        textName.setText(menuItemName);
                        Button btnClose = (Button) view.findViewById(R.id.btnCloseMenuItemPopup);
                        btnClose.setOnClickListener(new OnClickListener() {
                            
                            @Override
                            public void onClick(View v) {
                                pw.dismiss();
                                
                            }
                        });
                        if(bitMap != null){
                            ImageView image = (ImageView) view.findViewById(R.id.image);
                            image.setImageBitmap(Bitmap
                                    .createScaledBitmap(bitMap, 100, 100, false));
                        }
                        // The code below assumes that the root container has an id called 'main'
                        pw.showAtLocation(findViewById(R.id.main_menu_item_list), Gravity.CENTER, 0, 0);

                    }
                });
            }

            return v;
        }        
    }


}

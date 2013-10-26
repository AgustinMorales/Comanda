package com.company.comanda.brian;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.company.comanda.brian.helpers.AsyncGetData;
import com.company.comanda.brian.helpers.Formatter;
import com.company.comanda.brian.model.Restaurant;
import com.company.comanda.brian.xmlhandlers.RestaurantListHandler;
import com.company.comanda.common.HttpParams.SearchRestaurants;

public class ChooseRestaurantActivity extends ListActivity {

    private static final Logger log = 
            LoggerFactory.getLogger(ChooseRestaurantActivity.class);
    
    private double latitude;
    private double longitude;
    private String address;
    private String addressDetails;
    private String city;
    
    private ArrayList<Restaurant> restaurants;
    
    public static final String EXTRA_LATITUDE = "latitude";
    public static final String EXTRA_LONGITUDE = "longitude";
    public static final String EXTRA_NICE_ADDRESS = "niceAddress";
    public static final String EXTRA_CITY = "city";
    public static final String EXTRA_ADDRESS_DETAILS = "addressDetails";
    
    private static final int SMALL_IMAGE_SIZE = 75;
    
    private static final int PLACE_ORDER_CODE = 1;
    
    private ItemAdapter adapter;
    
    private WeakHashMap<String, Bitmap> smallBitmaps;
    
    
    private static class GetRestaurants extends AsyncGetData<ArrayList<Restaurant>>{
        
        @Override
        public void afterOnUIThread(ArrayList<Restaurant> data,
                Activity activity) {
            super.afterOnUIThread(data, activity);
            final ChooseRestaurantActivity local = (ChooseRestaurantActivity)activity;
            log.debug("afterOnUIThread");
            if(local.restaurants != null && local.restaurants.size() > 0)
            {
                
                local.adapter.notifyDataSetChanged();
                local.adapter.clear();
                for(int i=0;i<local.restaurants.size();i++){
                    log.debug("Item #{}", i);
                    local.adapter.add(local.restaurants.get(i));
                }
            }
            local.adapter.notifyDataSetChanged();
        }
        
        @Override
        public void afterOnBackground(ArrayList<Restaurant> data,
                Activity activity) {
            super.afterOnBackground(data, activity);
            ((ChooseRestaurantActivity)activity).restaurants = data;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        this.latitude = extras.getDouble(EXTRA_LATITUDE);
        this.longitude = extras.getDouble(EXTRA_LONGITUDE);
        this.address = extras.getString(EXTRA_NICE_ADDRESS);
        this.addressDetails = extras.getString(EXTRA_ADDRESS_DETAILS);
        this.city = extras.getString(EXTRA_CITY);
        
        smallBitmaps = new WeakHashMap<String, Bitmap>();
        setContentView(R.layout.choose_restaurant);
        
        restaurants = new ArrayList<Restaurant>();
        adapter = new ItemAdapter(this, R.layout.restaurant_row, restaurants);
        
        setListAdapter(adapter);
        
        GetRestaurants getRestaurants = new GetRestaurants();
        
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair(SearchRestaurants.PARAM_LATITUDE, "" + latitude));
        params.add(new BasicNameValuePair(SearchRestaurants.PARAM_LONGITUDE, "" + longitude));
        getRestaurants.execute(this, SearchRestaurants.SERVICE_NAME, params, 
                RestaurantListHandler.class);
        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                    long id) {
                Restaurant restaurant = (Restaurant)arg0.getItemAtPosition(position);
                Intent intent = new Intent(
                        getApplicationContext(), 
                        ComandaActivity.class);
                intent.putExtra(ComandaActivity.EXTRA_REST_ID, 
                        restaurant.id);
                intent.putExtra(ComandaActivity.EXTRA_REST_NAME, 
                        restaurant.name);
                intent.putExtra(ComandaActivity.EXTRA_TABLE_ID, 
                        "");
                intent.putExtra(ComandaActivity.EXTRA_TABLE_NAME, 
                        "");
                intent.putExtra(ComandaActivity.EXTRA_NICE_ADDRESS, 
                        address);
                intent.putExtra(ComandaActivity.EXTRA_CITY, 
                        city);
                intent.putExtra(ComandaActivity.EXTRA_ADDRESS_DETAILS, 
                        addressDetails);
                intent.putExtra(ComandaActivity.EXTRA_DELIVERY_COST, 
                        restaurant.deliveryCost);
                intent.putExtra(ComandaActivity.EXTRA_MINIMUM_FOR_DELIVERY, 
                        restaurant.minimumForDelivery);
                startActivityForResult(intent, PLACE_ORDER_CODE);
            }
        });
    }

    /*
     * PRIVATE ADAPTER CLASS. Assigns data to be displayed on the listview
     */
    private class ItemAdapter extends ArrayAdapter<Restaurant> 
    {
        //Hold array of items to be displayed in the list
        private ArrayList<Restaurant> items;

        public ItemAdapter(Context context, int textViewResourceId,
                ArrayList<Restaurant> items) 
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
                v = vi.inflate(R.layout.restaurant_row, null);
            }
            //get the FoodMenuItem corresponding to 
            //the position in the list we are rendering
            Restaurant o = items.get(position);
            if (o != null) 
            {
                //Set all of the UI components 
                //with the respective Object data
                final TextView tt = (TextView) v.findViewById(R.id.toptext);
                final TextView tvDeliveryCost = (TextView) v.findViewById(R.id.tvDeliveryCost);
                final TextView tvMinimumOrder = (TextView) v.findViewById(R.id.tvMinimumOrder);
                final LinearLayout deliveryCostPanel = (LinearLayout) v.findViewById(R.id.deliveryCostPanel);
                final LinearLayout minimumOrderPanel = (LinearLayout) v.findViewById(R.id.minimumOrderPanel);
                final String restaurantName = o.name;
                if (tt != null)
                {
                    tt.setText(restaurantName);   
                }
                if(o.deliveryCost > 0){
                    deliveryCostPanel.setVisibility(View.VISIBLE);
                    tvDeliveryCost.setText(Formatter.money(o.deliveryCost));
                }
                else{
                    deliveryCostPanel.setVisibility(View.GONE);
                }
                if(o.minimumForDelivery > 0){
                    minimumOrderPanel.setVisibility(View.VISIBLE);
                    tvMinimumOrder.setText(Formatter.money(o.minimumForDelivery));
                }
                else{
                    minimumOrderPanel.setVisibility(View.GONE);
                }
                if(o.imageURL != null && o.imageURL.length() > 0){
                    Bitmap bitmap = null;
                    if(smallBitmaps.containsKey(o.imageURL)){
                        bitmap = smallBitmaps.get(o.imageURL);
                    }
                    else{
                        try{
                            URL imageUrl = new URL(
                                    o.imageURL + "=s" + 
                            SMALL_IMAGE_SIZE);
                            InputStream bitmapIS = (InputStream)imageUrl
                                    .getContent();
                            bitmap = BitmapFactory
                                    .decodeStream(bitmapIS);
                            bitmapIS.close();
                            smallBitmaps.put(o.imageURL, bitmap);
                        }
                        catch(MalformedURLException e){
                            log.error("Wrong URL while " +
                            		"retrieving restaurant image", e);
                        } catch (IOException e) {
                            log.error("IOException while " +
                            		"retrieving restaurant image",
                            		e);
                        }
                    }
                    if(bitmap != null){
                        ImageView icon = (ImageView) v.findViewById(R.id.icon);
                        icon.setImageBitmap(bitmap);
                    }
                }
                
            }
            return v;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_ORDER_CODE){
            if(resultCode == Constants.ORDER_PLACED_RESULT){
                setResult(Constants.ORDER_PLACED_RESULT);
                finish();
            }
        }
    }
    
    
}

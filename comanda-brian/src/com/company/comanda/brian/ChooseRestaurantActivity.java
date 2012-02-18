package com.company.comanda.brian;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.company.comanda.brian.helpers.AsyncGetData;
import com.company.comanda.brian.model.Category;
import com.company.comanda.brian.model.Restaurant;
import com.company.comanda.brian.xmlhandlers.CategoriesHandler;
import com.company.comanda.brian.xmlhandlers.RestaurantListHandler;

public class ChooseRestaurantActivity extends ListActivity {

    private double latitude;
    private double longitude;
    
    private ArrayList<Restaurant> restaurants;
    
    public static final String EXTRA_LATITUDE = "latitude";
    public static final String EXTRA_LONGITUDE = "longitude";
    public static final String EXTRA_NICE_ADDRESS = "niceAddress";
    public static final String EXTRA_ADDRESS_DETAILS = "addressDetails";
    public static final String PARAM_RESTAURANT_ID = "restaurantId";
    public static final String PARAM_LATITUDE = "latitude";
    public static final String PARAM_LONGITUDE = "longitude";
    
    private ItemAdapter adapter;
    
    private String restaurantId;
    private String restaurantName;
    
    
    private static class GetRestaurants extends AsyncGetData<ArrayList<Restaurant>>{
        
        @Override
        public void afterOnUIThread(ArrayList<Restaurant> data,
                Activity activity) {
            super.afterOnUIThread(data, activity);
            final ChooseRestaurantActivity local = (ChooseRestaurantActivity)activity;
            Log.d("Comanda", "afterOnUIThread");
            if(local.restaurants != null && local.restaurants.size() > 0)
            {
                
                local.adapter.notifyDataSetChanged();
                local.adapter.clear();
                for(int i=0;i<local.restaurants.size();i++){
                    Log.d("Comanda", "Item #" + i);
                    local.adapter.add(local.restaurants.get(i));
                }
            }
            local.adapter.notifyDataSetChanged();
        }
    }
    
    private static class GetCategories extends AsyncGetData<ArrayList<Category>>{
        
        @Override
        public void afterOnUIThread(ArrayList<Category> data,
                Activity activity) {
            super.afterOnUIThread(data, activity);
            final ChooseRestaurantActivity local = (ChooseRestaurantActivity) activity;
            Intent intent = new Intent(
                    activity.getApplicationContext(), 
                    ComandaActivity.class);
            intent.putExtra(ComandaActivity.EXTRA_REST_ID, 
                    local.restaurantId);
            intent.putExtra(ComandaActivity.EXTRA_REST_NAME, 
                    local.restaurantName);
            intent.putExtra(ComandaActivity.EXTRA_TABLE_ID, 
                    "");
            intent.putExtra(ComandaActivity.EXTRA_TABLE_NAME, 
                    "");
            intent.putExtra(ComandaActivity.EXTRA_CATEGORIES, data);
            local.startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        this.latitude = extras.getDouble(EXTRA_LATITUDE);
        this.longitude = extras.getDouble(EXTRA_LONGITUDE);
        
        setContentView(R.layout.choose_restaurant);
        
        restaurants = new ArrayList<Restaurant>();
        adapter = new ItemAdapter(this, R.layout.restaurant_row, restaurants);
        
        setListAdapter(adapter);
        
        GetRestaurants getRestaurants = new GetRestaurants();
        
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair(PARAM_LATITUDE, "" + latitude));
        params.add(new BasicNameValuePair(PARAM_LONGITUDE, "" + longitude));
        getRestaurants.execute(this, "/searchRestaurants", params, 
                RestaurantListHandler.class);
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
                v = vi.inflate(R.layout.row, null);
            }
            //get the FoodMenuItem corresponding to 
            //the position in the list we are rendering
            Restaurant o = items.get(position);
            if (o != null) 
            {
                //Set all of the UI components 
                //with the respective Object data
                TextView tt = (TextView) v.findViewById(R.id.toptext);
                restaurantName = o.name;
                restaurantId = o.id;
                if (tt != null)
                {
                    tt.setText(restaurantName);   
                }
                
                v.setOnClickListener(new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        GetCategories getCategories = new GetCategories();
                        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(1);
                        params.add(new BasicNameValuePair(PARAM_RESTAURANT_ID, restaurantId));
                        getCategories.execute(ChooseRestaurantActivity.this, 
                                "/getCategories", params, CategoriesHandler.class);
                        
                    }
                });
                
            }
            
            

            return v;
        }        
    }
}

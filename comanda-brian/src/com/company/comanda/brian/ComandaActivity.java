package com.company.comanda.brian;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.peterkuterna.android.apps.swipeytabs.SwipeyTabs;
import net.peterkuterna.android.apps.swipeytabs.SwipeyTabsAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.company.comanda.brian.helpers.AsyncGetData;
import com.company.comanda.brian.model.Category;
import com.company.comanda.brian.model.FoodMenuItem;
import com.company.comanda.brian.xmlhandlers.MenuItemsHandler;

public class ComandaActivity extends FragmentActivity
{
    
    private static final String PARAM_RESTAURANT_ID = "restaurantId";
    
    private ArrayList<FoodMenuItem> m_items = null;
    private ArrayList<Category> categories = null;
    private ItemAdapter[] adapters;
    private String tableName;
    private String restName;
    private String tableId;
    private String restId;
    
    private SwipeyTabs categoriesTabs;
    private ViewPager categoriesPager;
    
    private static class AsyncGetMenuItems extends AsyncGetData<ArrayList<FoodMenuItem>>{

        @Override
        public void afterOnUIThread(ArrayList<FoodMenuItem> data,
                Activity activity) {
            super.afterOnUIThread(data, activity);
            ComandaActivity local = (ComandaActivity)activity;
            Log.d("Comanda", "afterOnUIThread");
            if(local.m_items != null && local.m_items.size() > 0)
            {
                
                for(int i=0;i<local.adapters.length; i++){
                    ItemAdapter adapter = local.adapters[i];
                    adapter.notifyDataSetChanged();
                    adapter.clear();
                    ArrayList<FoodMenuItem> currentList =
                            local.filterMenuItems(local.categories.get(i).id);
                    for(FoodMenuItem currentItem : currentList){
                        Log.d("Comanda", "Item #" + i);
                        adapter.add(currentItem);
                    }
                }
            }
            for(ItemAdapter adapter : local.adapters){
                adapter.notifyDataSetChanged();
            }
        }


        @Override
        public void afterOnBackground(ArrayList<FoodMenuItem> data,
                Activity activity) {
            super.afterOnBackground(data, activity);
            ((ComandaActivity)activity).m_items = data;
        }


        @Override
        public void beforeOnBackground(List<NameValuePair> params,
                Activity activity) {
            super.beforeOnBackground(params, activity);
            params.add(new BasicNameValuePair(PARAM_RESTAURANT_ID, 
                    ((ComandaActivity)activity).restId));
        }
        
    }
    
    public static final int ORDER_PLACED_TOAST_DURATION = 3;
    
    public static final String EXTRA_TABLE_NAME = "tableName";
    public static final String EXTRA_TABLE_ID = "tableId";
    public static final String EXTRA_REST_NAME = "restaurantName";
    public static final String EXTRA_REST_ID = "restaurantId";
    public static final String EXTRA_CATEGORIES = "categories";
    
    public static final String PARAM_TABLE_ID = "tableId";
    public static final String PARAM_REST_ID = "restaurantId";
    public static final String PARAM_ITEM_ID = "itemId";
    public static final String PARAM_USER_ID = "userId";
    public static final String PARAM_PASSWORD = "password";
    
    SharedPreferences prefs;
    /** Called when the activity is first created. */
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Bundle extras = getIntent().getExtras();
        tableName = extras.getString(EXTRA_TABLE_NAME);
        tableId = extras.getString(EXTRA_TABLE_ID);
        restName = extras.getString(EXTRA_REST_NAME);
        restId = extras.getString(EXTRA_REST_ID);
        categories = (ArrayList<Category>)extras.
                get(EXTRA_CATEGORIES);
        TextView tableNameTextView = (TextView)findViewById(R.id.tableNametextView);
        tableNameTextView.setText(getString(R.string.you_are_at_table) + 
                " " + tableName + ". " + 
                getString(R.string.at_restaurant) + " " + restName);
        
        m_items = new ArrayList<FoodMenuItem>();
        //set ListView adapter to basic ItemAdapter 
        //(it's a coincidence they are both called Item)
        final int noOfCategories = categories.size();
        adapters = new ItemAdapter[noOfCategories];
        for(int i=0;i<noOfCategories;i++){
            adapters[i] = new ItemAdapter(this, 
                    R.layout.row, filterMenuItems(categories.get(i).id));
        }
        
        AsyncGetMenuItems getData = new AsyncGetMenuItems();
        getData.execute(this, "/menuitems", new ArrayList<NameValuePair>(1), MenuItemsHandler.class);
        
        categoriesPager = (ViewPager)findViewById(R.id.categoriesPager);
        categoriesTabs = (SwipeyTabs)findViewById(R.id.categoriesTabs);
        
        final SwipeyTabsPagerAdapter adapter = new SwipeyTabsPagerAdapter(
                this, getSupportFragmentManager());
        categoriesPager.setAdapter(adapter);
        categoriesTabs.setAdapter(adapter);
        categoriesPager.setOnPageChangeListener(categoriesTabs);
        categoriesPager.setCurrentItem(0);
    }
    
    
    public static class CategoriesTabFragment extends Fragment {
        
        private ListAdapter adapter;
        
        public CategoriesTabFragment(ListAdapter adapter){
            this.adapter = adapter;
        }
        
        public static Fragment newInstance(String title, 
                ListAdapter adapter) {
            CategoriesTabFragment f = new CategoriesTabFragment(adapter);
            Bundle args = new Bundle();
            args.putString("title", title);
            f.setArguments(args);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            ViewGroup root = (ViewGroup) inflater.
                    inflate(R.layout.menu_items_list, null);
            ((ListView) root.findViewById(
                    R.id.menu_items_listview)).setAdapter(adapter);
            return root;
        }

    }
    

    private class SwipeyTabsPagerAdapter extends FragmentPagerAdapter implements
    SwipeyTabsAdapter {

        private final Context mContext;

        public SwipeyTabsPagerAdapter(Context context, FragmentManager fm) {
            super(fm);

            this.mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            return CategoriesTabFragment.newInstance(
                    categories.get(position).name, adapters[position]);
        }

        @Override
        public int getCount() {
            return categories.size();
        }

        public TextView getTab(final int position, SwipeyTabs root) {
            TextView view = (TextView) LayoutInflater.from(mContext).inflate(
                    R.layout.swipey_tab_indicator, root, false);
            view.setText(categories.get(position).name);
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    categoriesPager.setCurrentItem(position);
                }
            });

            return view;
        }

    }

    
    @Override
    protected void onStart() {
        super.onStart();
        prefs = PreferenceManager.
                getDefaultSharedPreferences(getApplicationContext());
    }



    public void fetchContent()
    {
        
        
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

    
    private class PlaceOrderTask extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... keyIds) {
            assert keyIds.length == 1;
            String keyId = keyIds[0];
            boolean success = false;
            
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + 
                    Constants.SERVER_LOCATION + "/placeOrder");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair(PARAM_ITEM_ID, keyId));
                nameValuePairs.add(new BasicNameValuePair(PARAM_TABLE_ID, tableId));
                nameValuePairs.add(new BasicNameValuePair(PARAM_REST_ID, restId));
                nameValuePairs.add(new BasicNameValuePair(PARAM_USER_ID, 
                        prefs.getString(ComandaPreferences.USER_ID, "")));
                nameValuePairs.add(new BasicNameValuePair(PARAM_PASSWORD, 
                        prefs.getString(ComandaPreferences.USER_ID, "")));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                httpclient.execute(httppost);
                success = true;
                
            } catch (Exception e){
                success = false;
            }
            
            
            return success;
        }


        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(getApplicationContext(), 
                        R.string.order_placed, ORDER_PLACED_TOAST_DURATION).show();
            }
            else{
                Constants.showErrorDialog(
                        R.string.error_while_placing_order, 
                        ComandaActivity.this);
            }
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
                Button placeOrderButton = (Button)v.findViewById(R.id.placeorderbutton);
                final String menuItemName = o.getName();
                final String menuItemKeyId = o.getKeyId();
                final String menuItemDescription = o.getDescription();
                if (tt != null)
                {
                    tt.setText(menuItemName);   
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


    ArrayList<FoodMenuItem> filterMenuItems(long categoryId){
        ArrayList<FoodMenuItem> result = new ArrayList<FoodMenuItem>(m_items.size());
        for(FoodMenuItem elem : m_items){
            if(elem.getCategoryId() == categoryId){
                result.add(elem);
            }
        }
        return result;
    }
}

package com.company.comanda.brian;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

import net.peterkuterna.android.apps.swipeytabs.SwipeyTabs;
import net.peterkuterna.android.apps.swipeytabs.SwipeyTabsAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.company.comanda.brian.helpers.AsyncGetData;
import com.company.comanda.brian.model.Category;
import com.company.comanda.brian.model.FoodMenuItem;
import com.company.comanda.brian.xmlhandlers.BooleanHandler;
import com.company.comanda.brian.xmlhandlers.CategoriesHandler;
import com.company.comanda.brian.xmlhandlers.MenuItemsHandler;
import com.company.comanda.common.HttpParams;
import com.company.comanda.common.HttpParams.GetCategories;
import com.company.comanda.common.HttpParams.GetMenuItems;

public class ComandaActivity extends FragmentActivity
{

    private static final Logger log = 
            LoggerFactory.getLogger(ComandaActivity.class);
    private ArrayList<FoodMenuItem> m_items = null;
    private ArrayList<Category> categories = null;
    private ItemAdapter[] adapters;
    private String tableName;
    private String restName;
    private String tableId;
    private String restId;
    private NumberFormat priceFormat;
    
    private static final int BIG_IMAGE_SIZE = 100;
    private static final int SMALL_IMAGE_SIZE = 50;

    private HashMap<String, Bitmap> smallBitmaps = new HashMap<String, Bitmap>();
    private HashMap<String, Bitmap> largeBitmaps = new HashMap<String, Bitmap>();

    private SwipeyTabs categoriesTabs;
    private ViewPager categoriesPager;

    private ArrayList<FoodMenuItem> orderItems;
    private HashMap<FoodMenuItem, Integer> orderNumbers;

    private ArrayAdapter<FoodMenuItem> reviewOrdersAdapter;

    private static final int REVIEW_ORDER_DIALOG = 1;
    private static final int ITEM_DETAILS_DIALOG = 2;

    private FoodMenuItem selectedMenuItem;

    private static class AsyncGetMenuItems extends AsyncGetData<ArrayList<FoodMenuItem>>{

        @Override
        public void afterOnUIThread(ArrayList<FoodMenuItem> data,
                Activity activity) {
            super.afterOnUIThread(data, activity);
            ComandaActivity local = (ComandaActivity)activity;
            log.debug("afterOnUIThread");
            if(local.m_items != null && local.m_items.size() > 0)
            {

                for(int i=0;i<local.adapters.length; i++){
                    ItemAdapter adapter = local.adapters[i];
                    adapter.notifyDataSetChanged();
                    adapter.clear();
                    ArrayList<FoodMenuItem> currentList =
                            local.filterMenuItems(local.categories.get(i).id);
                    for(FoodMenuItem currentItem : currentList){
                        log.debug("Item #{}", i);
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
            params.add(new BasicNameValuePair(GetMenuItems.PARAM_RESTAURANT_ID, 
                    ((ComandaActivity)activity).restId));
        }

    }

    private class AsyncGetCategories extends AsyncGetData<ArrayList<Category>>{

        @Override
        public void afterOnUIThread(ArrayList<Category> data,
                Activity activity) {
            super.afterOnUIThread(data, activity);
            categories = data;
            TextView tableNameTextView = (TextView)findViewById(R.id.tableNametextView);
            tableNameTextView.setText(getString(R.string.you_are_at_table) + 
                    " " + tableName + ". " + 
                    getString(R.string.at_restaurant) + " " + restName);

            m_items = new ArrayList<FoodMenuItem>();
            orderItems = new ArrayList<FoodMenuItem>();
            orderNumbers = new HashMap<FoodMenuItem, Integer>();
            //set ListView adapter to basic ItemAdapter 
            //(it's a coincidence they are both called Item)
            final int noOfCategories = categories.size();
            adapters = new ItemAdapter[noOfCategories];
            for(int i=0;i<noOfCategories;i++){
                adapters[i] = new ItemAdapter(ComandaActivity.this, 
                        R.layout.row, filterMenuItems(categories.get(i).id));
            }

            AsyncGetMenuItems getData = new AsyncGetMenuItems();
            getData.execute(ComandaActivity.this, GetMenuItems.SERVICE_NAME, new ArrayList<NameValuePair>(1), MenuItemsHandler.class);

            categoriesPager = (ViewPager)findViewById(R.id.categoriesPager);
            categoriesTabs = (SwipeyTabs)findViewById(R.id.categoriesTabs);

            final SwipeyTabsPagerAdapter adapter = new SwipeyTabsPagerAdapter(
                    ComandaActivity.this, getSupportFragmentManager());
            categoriesPager.setAdapter(adapter);
            categoriesTabs.setAdapter(adapter);
            categoriesPager.setOnPageChangeListener(categoriesTabs);
            categoriesPager.setCurrentItem(0);

            Button reviewOrderButton = (Button)findViewById(R.id.buttonReviewOrder);
            reviewOrderButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    showDialog(REVIEW_ORDER_DIALOG);

                }
            });
            reviewOrdersAdapter = new ReviewOrderAdapter(ComandaActivity.this, 
                    R.layout.row, orderItems);
        }
    }


    public static final int ORDER_PLACED_TOAST_DURATION = 3;

    public static final String EXTRA_TABLE_NAME = "tableName";
    public static final String EXTRA_TABLE_ID = "tableId";
    public static final String EXTRA_REST_NAME = "restaurantName";
    public static final String EXTRA_REST_ID = "restaurantId";


    SharedPreferences prefs;
    /** Called when the activity is first created. */
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

        priceFormat = NumberFormat.getCurrencyInstance();
        priceFormat.setCurrency(Currency.getInstance("EUR"));
        
        AsyncGetCategories getCategories = new AsyncGetCategories();
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair(GetCategories.PARAM_RESTAURANT_ID, restId));
        getCategories.execute(this, 
                GetCategories.SERVICE_NAME, params, CategoriesHandler.class);


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

    //FIXME: Should accept Void instead of String
    private class PlaceOrderTask extends AsyncGetData<Boolean> {




        @Override
        public void afterOnUIThread(Boolean data, Activity activity) {
            super.afterOnUIThread(data, activity);
            dismissDialog(REVIEW_ORDER_DIALOG);
            orderItems.clear();
            orderNumbers.clear();
            reviewOrdersAdapter.notifyDataSetChanged();
            refreshAllTables();
            if(data){
                Toast.makeText(getApplicationContext(), 
                        R.string.order_placed, ORDER_PLACED_TOAST_DURATION).show();
            }
            else{
                Constants.showErrorDialog(
                        R.string.error_while_placing_order, 
                        ComandaActivity.this);
            }
        }


        @Override
        public void beforeOnBackground(List<NameValuePair> params,
                Activity activity) {
            super.beforeOnBackground(params, activity);

            // Add your data
            StringBuffer keyIds = new StringBuffer();
            for(int i=0;i<orderItems.size();i++){
                final FoodMenuItem currentItem = orderItems.get(i);
                final String currentKeyId = currentItem.getKeyId();
                for(int j=0;j<orderNumbers.get(currentItem);j++){
                    keyIds.append(currentKeyId);
                    keyIds.append(":");
                }
            }
            keyIds.deleteCharAt(keyIds.length() - 1);
            log.debug("keyIds: {}", keyIds);
            params.add(new BasicNameValuePair(HttpParams.PlaceOrder.PARAM_ITEM_IDS, 
                    keyIds.toString()));
            params.add(new BasicNameValuePair(HttpParams.PlaceOrder.PARAM_TABLE_ID, tableId));
            params.add(new BasicNameValuePair(HttpParams.PlaceOrder.PARAM_RESTAURANT_ID, restId));
            params.add(new BasicNameValuePair(HttpParams.PlaceOrder.PARAM_USER_ID, 
                    prefs.getString(ComandaPreferences.USER_ID, "")));
            params.add(new BasicNameValuePair(HttpParams.PlaceOrder.PARAM_PASSWORD, 
                    prefs.getString(ComandaPreferences.PASSWORD, "")));
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
            final FoodMenuItem o = items.get(position);
            if (o != null) 
            {
                fillWithMenuItemInfo(v, o);

            }

            return v;
        }        
    }

    private void fillWithMenuItemInfo(View v, final FoodMenuItem o){
        //Set all of the UI components 
        //with the respective Object data
        ImageView icon = (ImageView) v.findViewById(R.id.icon);
        TextView tt = (TextView) v.findViewById(R.id.item_name);
        final String menuItemName = o.getName();
        final String menuItemDescription = o.getDescription();
        final String imageString = o.getImageString();
        if (tt != null)
        {
            tt.setText(menuItemName);   
        }
        TextView tvPrice = (TextView) v.findViewById(R.id.item_price);
        tvPrice.setText(priceFormat.format(o.getPrice()));
        Bitmap rawBitMap = null;
        Bitmap finImg = null;
        if(icon != null && imageString != null && imageString.length() > 0)
        {
            if(smallBitmaps.containsKey(imageString)){
                finImg = smallBitmaps.get(imageString);
            }
            else{
                URL imageURL = null;
                try      
                {        
                    //use our image serve page to get the image URL
                    imageURL = new URL(imageString + "=s"
                            + BIG_IMAGE_SIZE);
                } 
                catch (MalformedURLException e) 
                {
                    e.printStackTrace();
                }
                try 
                {
                    //Decode and resize the image then set as the icon
                    //                BitmapFactory.Options options = new BitmapFactory
                    //                        .Options();
                    //                options.inJustDecodeBounds = true;
                    //                options.inSampleSize = 1/2;
                    
                    if(largeBitmaps.containsKey(imageString)){
                        rawBitMap = largeBitmaps.get(imageString);
                    }
                    else{
                        InputStream bitmapIS = (InputStream)imageURL
                                .getContent();
                        rawBitMap = BitmapFactory
                                .decodeStream(bitmapIS);
                        bitmapIS.close();
                        if(rawBitMap != null){
                            largeBitmaps.put(imageString, rawBitMap);
                        }
                    }
                    if(smallBitmaps.containsKey(imageString) == false){
                        if(rawBitMap != null){
                            finImg = Bitmap
                                    .createScaledBitmap(rawBitMap, SMALL_IMAGE_SIZE, 
                                            SMALL_IMAGE_SIZE, false);
                            smallBitmaps.put(imageString, finImg);
                        }
                    }
                    else{
                        finImg = smallBitmaps.get(imageString);
                    }
                } 
                catch (IOException e) 
                {                        
                    e.printStackTrace();
                }
            }
        }
        //returns the view to the Adapter to be displayed
        if(finImg != null){
            icon.setImageBitmap(finImg);
        }
        View.OnClickListener clickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectedMenuItem = o;
                showDialog(ITEM_DETAILS_DIALOG);
            }
        };
        LinearLayout itemNameAndPrice = 
                (LinearLayout)v.findViewById(
                        R.id.item_name_and_price);
        itemNameAndPrice.setOnClickListener(clickListener);
        icon.setOnClickListener(clickListener);

        ImageButton removeButton = (ImageButton)v.findViewById(R.id.removeorderbutton);
        removeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                removeItemFromOrder(o);
            }
        });

        ImageButton placeOrderButton = (ImageButton)v.findViewById(R.id.placeorderbutton);
        placeOrderButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                addItemForOrder(o);

            }
        });
        
        TextView no_of_items = (TextView)v.findViewById(R.id.no_of_items);
        Integer numberOrdered = orderNumbers.get(o);
        if(numberOrdered != null){
            no_of_items.setText(numberOrdered.toString());
            no_of_items.setVisibility(View.VISIBLE);
            removeButton.setVisibility(View.VISIBLE);
            removeButton.setEnabled(true);
        }
        else{
            no_of_items.setVisibility(View.INVISIBLE);
            removeButton.setVisibility(View.INVISIBLE);
            removeButton.setEnabled(false);
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


    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog result = null;
        if(id == REVIEW_ORDER_DIALOG){
            result = new Dialog(this);
            final Dialog dialog = result;
            result.setTitle(this.getString(R.string.review_order_dialog_title));
            result.setContentView(R.layout.review_order);

            ListView listView = (ListView)result.findViewById(R.id.listViewReviewOrder);
            listView.setAdapter(reviewOrdersAdapter);

            final Button commitOrderButton = (Button)result.findViewById(R.id.buttonCommitOrder);
            commitOrderButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    (new PlaceOrderTask()).execute(
                            ComandaActivity.this,
                            HttpParams.PlaceOrder.SERVICE_NAME,
                            new ArrayList<NameValuePair>(),
                            BooleanHandler.class);
                }
            });

            Button backButton = (Button) result.findViewById(R.id.buttonReviewOrderBack);
            backButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            if(orderItems.size() == 0){
                commitOrderButton.setEnabled(false);
            }
            else{
                commitOrderButton.setEnabled(true);
            }
            reviewOrdersAdapter.registerDataSetObserver(new DataSetObserver() {

                @Override
                public void onChanged() {
                    if(orderItems.size() > 0){
                        commitOrderButton.setEnabled(true);
                    }
                    else{
                        commitOrderButton.setEnabled(false);
                    }
                }

            });
        }
        else if(id == ITEM_DETAILS_DIALOG){
            result = new Dialog(this);
            result.setContentView(R.layout.menu_item_details);
            Button btnClose = (Button) result.findViewById(R.id.btnCloseMenuItemPopup);
            btnClose.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissDialog(ITEM_DETAILS_DIALOG);
                }
            });
        }
        else{
            result = super.onCreateDialog(id);
        }
        return result;
    }


    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        if(id == ITEM_DETAILS_DIALOG){
            TextView text = (TextView) dialog.findViewById(R.id.contextMenuItemDescription);
            text.setText(selectedMenuItem.getDescription());
            TextView textName = (TextView) dialog.findViewById(R.id.contextMenuItemName);
            textName.setText(selectedMenuItem.getName());
            String imageString = selectedMenuItem.getImageString();
            Bitmap largeBitmap = null;
            if(largeBitmaps.containsKey(imageString)){
                largeBitmap = largeBitmaps.get(imageString);
            }
            if(largeBitmap != null){
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageBitmap(largeBitmap);
            }
        }
        else{
            super.onPrepareDialog(id, dialog);
        }
    }


    private class ReviewOrderAdapter extends ArrayAdapter<FoodMenuItem>{

        public ReviewOrderAdapter(Context context, int textViewResourceId,
                List<FoodMenuItem> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) 
            {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //inflate using res/layout/row.xml
                v = vi.inflate(R.layout.row, null);
            }
            final FoodMenuItem o = orderItems.get(position);
            if (o != null) 
            {
                fillWithMenuItemInfo(v, o);
            }

            return v;
        }

    }

    private void addItemForOrder(FoodMenuItem item){
        if(orderNumbers.containsKey(item)){
            int previousnumber = orderNumbers.get(item);
            orderNumbers.put(item,previousnumber + 1);
        }
        else{
            orderItems.add(item);
            orderNumbers.put(item,1);
        }
        reviewOrdersAdapter.notifyDataSetChanged();
        refreshAllTables();
    }

    private void removeItemFromOrder(FoodMenuItem item){
        if(orderNumbers.containsKey(item)){
            int previousnumber = orderNumbers.get(item);
            if(previousnumber > 1){
                orderNumbers.put(item,previousnumber - 1);
            }
            else{
                orderItems.remove(item);
                orderNumbers.remove(item);
            }
            reviewOrdersAdapter.notifyDataSetChanged();
            refreshAllTables();
        }
        else{
            throw new IllegalStateException("Not in the order list");
        }
    }

    private void refreshAllTables(){
        for(ItemAdapter adapter : adapters){
            adapter.notifyDataSetChanged();
        }
    }

}

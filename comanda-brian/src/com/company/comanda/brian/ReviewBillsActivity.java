package com.company.comanda.brian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.company.comanda.brian.helpers.AsyncGetData;
import com.company.comanda.brian.helpers.Formatter;
import com.company.comanda.brian.model.Bill;
import com.company.comanda.brian.model.Order;
import com.company.comanda.brian.xmlhandlers.BillsHandler;
import com.company.comanda.brian.xmlhandlers.OrdersHandler;

public class ReviewBillsActivity extends ListActivity {
    
    
    private static final int BILL_DETAILS_DIALOG = 1;
    
    private static final Logger log = 
            LoggerFactory.getLogger(ReviewBillsActivity.class);

    private ArrayList<Bill> bills;
    private String userId;
    private String password;
    
    public static final String EXTRA_USER_ID = "userId";
    public static final String EXTRA_PASSWORD = "password";
    
    private ListView ordersListView;
    private String displayedBillKeyString;
    private String selectedBillKeyString;
    
    private ItemAdapter adapter;
    
    private HashMap<String, ArrayList<Order>> ordersMap;
    
    private static class GetOrders extends AsyncGetData<ArrayList<Order>>{
        @Override
        public void afterOnUIThread(ArrayList<Order> data,
                Activity activity) {
            ReviewBillsActivity local = (ReviewBillsActivity)activity;
            local.showDialog(ReviewBillsActivity.BILL_DETAILS_DIALOG);
        }
        
        @Override
        public void beforeOnBackground(List<NameValuePair> params,
                Activity activity) {
            super.beforeOnBackground(params, activity);
            ReviewBillsActivity local = (ReviewBillsActivity)activity;
            params.add(new BasicNameValuePair(
                    com.company.comanda.common.
                    HttpParams.GetOrders.PARAM_USER_ID, 
                    local.userId));
            params.add(new BasicNameValuePair(
                    com.company.comanda.common.
                    HttpParams.GetOrders.PARAM_PASSWORD, 
                    local.password));
            params.add(new BasicNameValuePair(
                    com.company.comanda.common.
                    HttpParams.GetOrders.PARAM_BILL, 
                    local.selectedBillKeyString));
        }


        @Override
        public void afterOnBackground(ArrayList<Order> data,
                Activity activity) {
            super.afterOnBackground(data, activity);
            ReviewBillsActivity local = (ReviewBillsActivity)activity;
            local.ordersMap.put(local.selectedBillKeyString,data);
        }
    }
    
    
    private static class GetBills extends AsyncGetData<ArrayList<Bill>>{
        @Override
        public void afterOnUIThread(ArrayList<Bill> data,
                Activity activity) {
            super.afterOnUIThread(data, activity);
            final ReviewBillsActivity local = (ReviewBillsActivity)activity;
            log.debug("afterOnUIThread");
            if(local.bills != null && local.bills.size() > 0)
            {
                
                local.adapter.notifyDataSetChanged();
                local.adapter.clear();
                for(int i=0;i<local.bills.size();i++){
                    log.debug("Item #{}", i);
                    local.adapter.add(local.bills.get(i));
                }
            }
            local.adapter.notifyDataSetChanged();
        }
        
        
        
        @Override
        public void beforeOnBackground(List<NameValuePair> params,
                Activity activity) {
            super.beforeOnBackground(params, activity);
            ReviewBillsActivity local = (ReviewBillsActivity)activity;
            params.add(new BasicNameValuePair(
                    com.company.comanda.common.
                    HttpParams.GetBills.PARAM_USER_ID, 
                    local.userId));
            params.add(new BasicNameValuePair(
                    com.company.comanda.common.
                    HttpParams.GetBills.PARAM_PASSWORD, 
                    local.password));
        }



        @Override
        public void afterOnBackground(ArrayList<Bill> data,
                Activity activity) {
            super.afterOnBackground(data, activity);
            ((ReviewBillsActivity)activity).bills = data;
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        this.userId = extras.getString(EXTRA_USER_ID);
        this.password = extras.getString(EXTRA_PASSWORD);
        
        setContentView(R.layout.bills);
        bills = new ArrayList<Bill>();
        ordersMap = new HashMap<String, ArrayList<Order>>();
        adapter = new ItemAdapter(this, R.layout.bill_row, bills);
        setListAdapter(adapter);
        
        ArrayList<NameValuePair> params = 
                new ArrayList<NameValuePair>(2);
        GetBills getBills = new GetBills();
        getBills.execute(this, 
                com.company.comanda.common.HttpParams.
                GetBills.SERVICE_NAME, params, 
                BillsHandler.class);
        
        getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                    long id) {
                Bill bill = (Bill)arg0.getItemAtPosition(position);
                selectedBillKeyString = bill.keyString;
                if(ordersMap.containsKey(bill.keyString)){
                    showDialog(BILL_DETAILS_DIALOG);
                }
                else{
                    GetOrders getOrders = new GetOrders();
                    ArrayList<NameValuePair> params = 
                            new ArrayList<NameValuePair>(3);
                    getOrders.execute(ReviewBillsActivity.this, 
                            com.company.comanda.common.HttpParams.GetOrders.SERVICE_NAME, 
                            params, OrdersHandler.class);
                }
            }
        });
    }

    
    private class OrdersAdapter extends ArrayAdapter<Order>{
        
        private ArrayList<Order> items;
        
        public OrdersAdapter(Context context, int textViewResourceId,
                ArrayList<Order> items) 
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
                v = vi.inflate(R.layout.order_row, null);
            }
            //get the FoodMenuItem corresponding to 
            //the position in the list we are rendering
            Order o = items.get(position);
            if (o != null) 
            {
                //Set all of the UI components 
                //with the respective Object data
                TextView tvMenuItemName = (TextView) v.findViewById(R.id.item_name);
                final String itemName = o.menuItemName;
                if (tvMenuItemName != null)
                {
                    tvMenuItemName.setText(itemName);   
                }
                TextView tvNoOfItems = (TextView) v.findViewById(R.id.no_of_items);
                tvNoOfItems.setText("" + o.menuItemNumber);
                TextView tvItemPrice = (TextView) v.findViewById(R.id.price);
                tvItemPrice.setText("" + o.menuItemPrice);
            }
            
            
            

            return v;
        }
    }
    
    /*
     * PRIVATE ADAPTER CLASS. Assigns data to be displayed on the listview
     */
    private class ItemAdapter extends ArrayAdapter<Bill> 
    {
        //Hold array of items to be displayed in the list
        private ArrayList<Bill> items;

        public ItemAdapter(Context context, int textViewResourceId,
                ArrayList<Bill> items) 
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
                v = vi.inflate(R.layout.bill_row, null);
            }
            //get the FoodMenuItem corresponding to 
            //the position in the list we are rendering
            Bill o = items.get(position);
            if (o != null) 
            {
                //Set all of the UI components 
                //with the respective Object data
                TextView tvRestaurantName = (TextView) v.findViewById(R.id.restaurant_name);
                final String restaurantName = o.restaurantName;
                if (tvRestaurantName != null)
                {
                    tvRestaurantName.setText(restaurantName);   
                }
                TextView tvOpenDate = (TextView) v.findViewById(R.id.date);
                tvOpenDate.setText(Formatter.formatToYesterdayOrToday(o.openDate, getContext()));
                TextView tvTotalAmount = (TextView) v.findViewById(R.id.total_amount);
                tvTotalAmount.setText(Formatter.money(o.totalAmount));
            }
            return v;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog result = null;
        if(id == BILL_DETAILS_DIALOG){
            result = new Dialog(this);
            result.setTitle("Order details:");
            result.setContentView(R.layout.orders_dialog);

            ordersListView = (ListView)result.findViewById(R.id.listViewOrders);

        }
        else{
            result = super.onCreateDialog(id);
        }
        return result;
    }



    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        if(selectedBillKeyString.equals(displayedBillKeyString) == false){
            final ArrayList<Order> orders = ordersMap.get(selectedBillKeyString);
            
            ArrayAdapter<Order> ordersAdapter = new OrdersAdapter(this, R.layout.order_row, orders);
            
            ordersListView.setAdapter(ordersAdapter);
            ordersAdapter.notifyDataSetChanged();
            displayedBillKeyString = selectedBillKeyString;
        }
        
    }
    
    
    
}

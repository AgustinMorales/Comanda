package com.company.comanda.brian;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.company.comanda.brian.helpers.AsyncGetData;
import com.company.comanda.brian.model.Bill;
import com.company.comanda.brian.xmlhandlers.BillsHandler;

public class ReviewBillsActivity extends ListActivity {
    
    private static final Logger log = 
            LoggerFactory.getLogger(ReviewBillsActivity.class);

    private ArrayList<Bill> bills;
    private String userId;
    private String password;
    
    public static final String EXTRA_USER_ID = "userId";
    public static final String EXTRA_PASSWORD = "password";
    
    private ItemAdapter adapter;
    
    
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
//        Bundle extras = getIntent().getExtras();
//        this.userId = extras.getString(EXTRA_USER_ID);
//        this.password = extras.getString(EXTRA_PASSWORD);
        
        setContentView(R.layout.bills);
        bills = new ArrayList<Bill>();
        adapter = new ItemAdapter(this, R.layout.bill_row, bills);
        setListAdapter(adapter);
        
        ArrayList<NameValuePair> params = 
                new ArrayList<NameValuePair>(2);
        GetBills getBills = new GetBills();
        getBills.execute(this, 
                com.company.comanda.common.HttpParams.
                GetBills.SERVICE_NAME, params, 
                BillsHandler.class);
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
                tvOpenDate.setText(o.openDate.toString());
                TextView tvTotalAmount = (TextView) v.findViewById(R.id.total_amount);
                tvTotalAmount.setText("" + o.totalAmount);
            }
            
            
            

            return v;
        }
    }
    
}

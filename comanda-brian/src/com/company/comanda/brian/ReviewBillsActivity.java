package com.company.comanda.brian;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.company.comanda.brian.helpers.AsyncGetData;
import com.company.comanda.brian.helpers.Formatter;
import com.company.comanda.brian.helpers.LayoutHelper;
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

    private TextView tvRestaurantName;
    private TextView tvOrderDate;
    private TextView tvState;
    private TextView tvDeliveryAddress;
    private TextView tvTotalAmount;
    private TextView tvDeliveryCost;

    public static final String EXTRA_USER_ID = "userId";
    public static final String EXTRA_PASSWORD = "password";

    private LinearLayout ordersPanel;
    private String displayedBillKeyString;
    private String selectedBillKeyString;
    private Bill selectedBill;

    private Button btnRefresh;

    private ItemAdapter adapter;

    private WeakHashMap<String, ArrayList<Order>> ordersMap;

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
            local.doNotrefreshing();
        }



        @Override
        public void beforeOnBackground(List<NameValuePair> params,
                Activity activity) {
            super.beforeOnBackground(params, activity);
            //Check for subsequent invocations (btnRefresh)
            if(params.size() == 0){
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
        if(extras != null){
            this.userId = extras.getString(EXTRA_USER_ID);
            this.password = extras.getString(EXTRA_PASSWORD);
        }
        setContentView(R.layout.bills);
        btnRefresh = (Button)findViewById(R.id.btnRefresh);
        bills = new ArrayList<Bill>();
        ordersMap = new WeakHashMap<String, ArrayList<Order>>();
        adapter = new ItemAdapter(this, R.layout.bill_row, bills);
        setListAdapter(adapter);

        final ArrayList<NameValuePair> params = 
                new ArrayList<NameValuePair>(2);
        final GetBills getBills = new GetBills();
        doRefreshing();
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
                selectedBill = bill;
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



        btnRefresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                doRefreshing();
                getBills.execute(ReviewBillsActivity.this, 
                        com.company.comanda.common.HttpParams.
                        GetBills.SERVICE_NAME, params, 
                        BillsHandler.class);

            }
        });

    }

    private void doRefreshing(){
        btnRefresh.setEnabled(false);
    }

    private void doNotrefreshing(){
        ordersMap.clear();
        btnRefresh.setEnabled(true);
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
                TextView tvStatus = (TextView) v.findViewById(R.id.status);
                StringAndColor stringAndColor = getStateString(o);
                tvStatus.setTextColor(stringAndColor.color);
                tvStatus.setText(stringAndColor.string);
                TextView tvTotalAmount = (TextView) v.findViewById(R.id.total_amount);
                tvTotalAmount.setText(Formatter.money(o.totalAmount));
            }
            return v;
        }
    }

    private static class StringAndColor{
        public String string;
        public int color;

        public StringAndColor(String string, int color){
            this.string = string;
            this.color = color;
        }
    }

    private StringAndColor getStateString(Bill bill){
        String statusString = null;
        int color = 0;
        if("OPEN".equals(bill.state)){
            statusString = getString(R.string.orderSent);
            color = Color.YELLOW;
        }
        else if("REJECTED_OFF_DUTY".equals(bill.state)){
            statusString = getString(R.string.orderRejectedOffDuty);
            color = Color.RED;
        }
        else if("REJECTED_NO_DELIVERY_THERE".equals(bill.state)){
            statusString = getString(R.string.orderRejectedNoDeliveryThere);
            color = Color.RED;
        }
        else if("REJECTED_NOT_ENOUGH_FOR_DELIVERY".equals(bill.state)){
            statusString = getString(R.string.orderRejectedNotEnoughForDelivery);
            color = Color.RED;
        }
        else if("REJECTED_UNKNOWN_ADDRESS".equals(bill.state)){
            statusString = getString(R.string.orderRejectedUnknownAddress);
            color = Color.RED;
        }
        else if("REJECTED_OUT_OF_SOMETHING".equals(bill.state)){
            statusString = getString(R.string.orderRejectedOutOfSomething);
            color = Color.RED;
        }
        else if("REJECTED_OVERLOAD".equals(bill.state)){
            statusString = getString(R.string.orderRejectedOverload);
            color = Color.RED;
        }
        else if("REJECTED".equals(bill.state)){
            statusString = getString(R.string.orderRejected);
            color = Color.RED;
        }
        else if("DELIVERED".equals(bill.state)){
            statusString = String.format("%s %s", 
                    getString(R.string.orderAcceptedEstimatedTime),
                    Formatter.formatToTime(bill.estimatedDeliveryDate, this));
            color = Color.GREEN;
        }
        return new StringAndColor(statusString, color);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog result = null;
        if(id == BILL_DETAILS_DIALOG){
            result = new Dialog(this);
            result.setTitle(getString(R.string.order_details));
            result.setContentView(R.layout.orders_dialog);

            ordersPanel = (LinearLayout)result.findViewById(R.id.ordersPanel);
            tvRestaurantName = (TextView)result.findViewById(R.id.tvRestaurantName);
            tvOrderDate = (TextView)result.findViewById(R.id.tvOrderDate);
            tvState = (TextView)result.findViewById(R.id.tvState);
            tvDeliveryAddress = (TextView)result.findViewById(R.id.tvDeliveryAddress);
            tvTotalAmount = (TextView)result.findViewById(R.id.tvTotalAmount);
            tvDeliveryCost = (TextView) result.findViewById(R.id.tvDeliveryCost);
            final Button btnClose = (Button)result.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissDialog(BILL_DETAILS_DIALOG);

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

        if(id == BILL_DETAILS_DIALOG){
            if(selectedBillKeyString.equals(displayedBillKeyString) == false){
                tvRestaurantName.setText(selectedBill.restaurantName);
                tvState.setText(getStateString(selectedBill).string);
                tvDeliveryAddress.setText(selectedBill.address);
                tvOrderDate.setText(Formatter.formatToYesterdayOrToday(selectedBill.openDate, this));
                tvTotalAmount.setText(Formatter.money(selectedBill.totalAmount));
                tvDeliveryCost.setText(Formatter.money(selectedBill.deliveryCost));
                final ArrayList<Order> orders = ordersMap.get(selectedBillKeyString);

                
                ordersPanel.removeAllViews();
                for(Order order : orders){
                    ordersPanel.addView(createOrderRow(order));
                }
                displayedBillKeyString = selectedBillKeyString;
                LayoutHelper.dialog_fill_parent(dialog);
            }
        }
        else{
            super.onPrepareDialog(id, dialog);
        }

    }


    protected View createOrderRow(Order o){
        View v = null;
        LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate using res/layout/row.xml
        v = vi.inflate(R.layout.order_row, null);
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
        tvItemPrice.setText(Formatter.money(o.menuItemPrice));
        return v;
    }

}

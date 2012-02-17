package com.company.comanda.brian;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.company.comanda.brian.helpers.AsyncGetData;
import com.company.comanda.brian.model.Category;
import com.company.comanda.brian.xmlhandlers.CategoriesHandler;
import com.company.comanda.brian.xmlhandlers.RestaurantAndTableXMLHandler;
import com.company.comanda.brian.xmlhandlers.RestaurantAndTableXMLHandler.ParsedData;

public class SelectTableActivity extends Activity
{
    
    private static final int SCAN_CODE_ACTIVITY = 1;
    
    private static final String PARAM_CODE = "code";
    private static final String PARAM_REST_ID = "restaurantId";
    
    
    public static class GetCategories extends 
    AsyncGetData<ArrayList<Category>>{

        private String restId;
        private String restName;
        private String tableId;
        private String tableName;
        
        public GetCategories(String restId,
                String restName,
                String tableId,
                String tableName){
            this.restId = restId;
            this.restName = restName;
            this.tableId = tableId;
            this.tableName = tableName;
        }
        
        @Override
        public void afterOnUIThread(ArrayList<Category> data, Activity activity) {
            super.afterOnUIThread(data, activity);
            Intent intent = new Intent(
                    activity.getApplicationContext(), 
                    ComandaActivity.class);
            intent.putExtra(ComandaActivity.EXTRA_REST_ID, 
                    restId);
            intent.putExtra(ComandaActivity.EXTRA_REST_NAME, 
                    restName);
            intent.putExtra(ComandaActivity.EXTRA_TABLE_ID, 
                    tableId);
            intent.putExtra(ComandaActivity.EXTRA_TABLE_NAME, 
                    tableName);
            intent.putExtra(ComandaActivity.EXTRA_CATEGORIES, data);
            activity.startActivity(intent);
        }
        
        
    }
    
    
    public static class GetTableData extends AsyncGetData<ParsedData>{

        @Override
        public void afterOnUIThread(ParsedData data, 
                Activity activity) {
            if(data != null){
                Intent intent = new Intent(
                        activity.getApplicationContext(), 
                        ComandaActivity.class);
                intent.putExtra(ComandaActivity.EXTRA_REST_ID, 
                        data.restId);
                intent.putExtra(ComandaActivity.EXTRA_REST_NAME, 
                        data.restName);
                intent.putExtra(ComandaActivity.EXTRA_TABLE_ID, 
                        data.tableId);
                intent.putExtra(ComandaActivity.EXTRA_TABLE_NAME, 
                        data.tableName);
                activity.startActivity(intent);
            }
            
        }

        @Override
        public void afterOnBackground(ParsedData data, Activity activity) {
            super.afterOnBackground(data, activity);
            
            GetCategories getCategories = new GetCategories(
                    data.restId, data.restName, 
                    data.tableId, data.tableName);
            
            List<NameValuePair> params = new ArrayList<NameValuePair>(1);
            params.add(new BasicNameValuePair(PARAM_REST_ID, data.restId));
            getCategories.execute(activity, "/getCategories", 
                    params, CategoriesHandler.class);
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

    
    public void onScanCodeClick(){
        GetTableData getData = new GetTableData();
        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair(PARAM_CODE, "2300200022742"));
        getData.execute(SelectTableActivity.this, "/decodeQR", params, 
                RestaurantAndTableXMLHandler.class);
        
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SCAN_CODE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
               String contents = intent.getStringExtra("SCAN_RESULT");
               //TODO: Some contents validation would be great...
               GetTableData getData = new GetTableData();
               List<NameValuePair> params = new ArrayList<NameValuePair>(1);
               params.add(new BasicNameValuePair(PARAM_CODE, contents));
               getData.execute(this, "/decodeQR", params, 
                       RestaurantAndTableXMLHandler.class);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled", 20);
            }
         }
        
      }
    
}

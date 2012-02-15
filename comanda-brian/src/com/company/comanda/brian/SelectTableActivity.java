package com.company.comanda.brian;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import roboguice.activity.RoboActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.company.comanda.brian.helpers.AsyncGetData;
import com.company.comanda.brian.xmlhandlers.RestaurantAndTableXMLHandler;
import com.company.comanda.brian.xmlhandlers.RestaurantAndTableXMLHandler.ParsedData;
import com.google.inject.Inject;

public class SelectTableActivity extends RoboActivity
{
    
    private static final int SCAN_CODE_ACTIVITY = 1;
    
    private static final String PARAM_CODE = "code";
    
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

        
    }
    
    @Inject
    private GetTableData getTableData;
    
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
//                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                startActivityForResult(intent, SCAN_CODE_ACTIVITY);
                
                GetTableData getData = getTableData;
                List<NameValuePair> params = new ArrayList<NameValuePair>(1);
                params.add(new BasicNameValuePair(PARAM_CODE, "2300200022742"));
                getData.execute(SelectTableActivity.this, "/decodeQR", params, 
                        RestaurantAndTableXMLHandler.class);
            }
        });
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

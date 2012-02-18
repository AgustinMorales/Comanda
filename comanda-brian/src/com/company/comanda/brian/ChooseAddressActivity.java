package com.company.comanda.brian;

import java.nio.ReadOnlyBufferException;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.company.comanda.brian.helpers.AddressOpenHelper;


public class ChooseAddressActivity extends ListActivity {

    
    private CursorAdapter adapter;
    private Cursor cursor;
    private AddressOpenHelper openHelper;
    private SQLiteDatabase readableDatabase;
    private Button addAddressButton;
    
    private static final int NEW_ADDRESS_DIALOG = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.choose_address);
        
        openHelper = new AddressOpenHelper(this);
        
        cursor = newQuery();
        
        startManagingCursor(cursor);
        
        adapter = new AddressAdapter(this, cursor);
        
        setListAdapter(adapter);
        
        addAddressButton = (Button)findViewById(R.id.new_address_button);
        
        addAddressButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                showDialog(NEW_ADDRESS_DIALOG);
                
            }
        });
    }

    private Cursor newQuery(){
        if(readableDatabase != null){
            if(readableDatabase.isOpen()){
                readableDatabase.close();
            }
        }
        readableDatabase = openHelper.getReadableDatabase();
        return readableDatabase.query(
                AddressOpenHelper.ADDRESS_TABLE_NAME, 
                new String[]{
                        AddressOpenHelper.COLUMN_ID,
                        AddressOpenHelper.COLUMN_NICE_STRING,
                        AddressOpenHelper.COLUMN_ADDITIONAL_DATA,
                        AddressOpenHelper.COLUMN_LATITUDE,
                        AddressOpenHelper.COLUMN_LONGITUDE
                },
                null, null, null, null, null);
    }

    private class AddressAdapter extends CursorAdapter{

        public AddressAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            prepareView(view, cursor);
            
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //inflate using res/layout/row.xml
            View v = vi.inflate(R.layout.address_row, null);
            
            prepareView(v, cursor);
            return v;
        }
        
        private void prepareView(View v, Cursor cursor){
            TextView textView = (TextView)v.findViewById(
                    R.id.textViewAddressNiceString);
            final String niceAddress = cursor.getString(1);
            final String additionalData = cursor.getString(2);
            final double latitude = cursor.getDouble(3);
            final double longitude = cursor.getDouble(4);
            
            textView.setText(niceAddress);
            
            v.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            getApplicationContext(), 
                            ChooseRestaurantActivity.class);
                    intent.putExtra(ChooseRestaurantActivity.EXTRA_LATITUDE, 
                            latitude);
                    intent.putExtra(ChooseRestaurantActivity.EXTRA_LATITUDE, 
                            longitude);
                    intent.putExtra(ChooseRestaurantActivity.EXTRA_ADDRESS_DETAILS, 
                            additionalData);
                    intent.putExtra(ChooseRestaurantActivity.EXTRA_NICE_ADDRESS, 
                            niceAddress);
                    startActivity(intent);
                    
                }
            });
        }
    }

    

    @Override
    protected void onPause() {
        if(readableDatabase != null){
            if(readableDatabase.isOpen()){
                readableDatabase.close();
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog result = null;
        if(id == NEW_ADDRESS_DIALOG){
            result = new Dialog(this);
            result.setContentView(R.layout.new_address_dialog);
            result.setTitle(R.string.new_address_dialog_title);
            
            final Button submitNewAddressButton = (Button)
                    result.findViewById(R.id.buttonSubmitNewAddress);
            final EditText niceAddressEditText = (EditText)
                    result.findViewById(R.id.editTextNiceAddress);
            final EditText additionalDetailsEditText = (EditText)
                    result.findViewById(R.id.editTextAdditionalAddress);
            
            submitNewAddressButton.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    if(addNewAddress(niceAddressEditText.getText().toString(), 
                            additionalDetailsEditText.getText().toString())){
                    
                        dismissDialog(NEW_ADDRESS_DIALOG);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChooseAddressActivity.this);
                        builder.setMessage("Your address could not be recognized. Please type it again.")
                               .setCancelable(false)
                               .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                   }
                               });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    
                }
            });
            
            result.setOnDismissListener(new OnDismissListener() {
                
                @Override
                public void onDismiss(DialogInterface dialog) {
                    refreshList();
                }
            });
        }
        else{
            result = super.onCreateDialog(id);
        }
        return result;
    }

    private void refreshList(){
        adapter.changeCursor(newQuery());
        adapter.notifyDataSetChanged();
    }

    private boolean addNewAddress(String niceAddress, 
            String additionalDetails){
        boolean result = false;
        Geocoder coder = new Geocoder(this);

        try {
            List<Address>address = coder.getFromLocationName(niceAddress,5);
            if (address != null) {
                Address location = address.get(0);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                SQLiteDatabase database = openHelper.getWritableDatabase();
                ContentValues values = new ContentValues(4);
                values.put(AddressOpenHelper.COLUMN_NICE_STRING, 
                        niceAddress);
                values.put(AddressOpenHelper.COLUMN_ADDITIONAL_DATA, 
                        additionalDetails);
                values.put(AddressOpenHelper.COLUMN_LATITUDE, 
                        latitude);
                values.put(AddressOpenHelper.COLUMN_LONGITUDE, 
                        longitude);
                database.insert(AddressOpenHelper.ADDRESS_TABLE_NAME, 
                        null, values);
                database.close();
                result = true;
            }
        }
        catch (Exception e) {
            Log.e("Comanda", "Could not save address...", e);
        }
        return result;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        if(id == NEW_ADDRESS_DIALOG){
            ((EditText)dialog.findViewById(R.id.editTextNiceAddress)).setText("");
            ((EditText)dialog.findViewById(R.id.editTextAdditionalAddress)).setText("");
        }
        else{
            super.onPrepareDialog(id, dialog);
        }
    }
    
    
}

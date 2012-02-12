package com.company.comanda.brian;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.company.comanda.brian.helpers.AsyncGetData;
import com.company.comanda.brian.xmlhandlers.UserIdHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class InputYourDataActivity extends Activity {

    private Button okButton;
    private EditText phoneET;
    private SharedPreferences prefs;
    
    private static final String PARAM_PHONE_NUMER = 
            "phoneNumber";
    private static final String PARAM_PASSWORD = 
            "password";
    private static final String PARAM_VALIDATION_CODE = 
            "validationCode";
    
    private Random random;
    
    private class GetUserData extends AsyncGetData<String>{

        @Override
        public void afterOnUIThread(String data) {
            startSelectTable();
            
        }

        @Override
        public void afterOnBackground(String data) {
            Editor editor = prefs.edit();
            editor.putString(ComandaPreferences.USER_ID, data);
            editor.commit();
        }

        @Override
        public void beforeOnBackground(List<NameValuePair> params) {
            super.beforeOnBackground(params);
            params.add(new BasicNameValuePair(
                    PARAM_VALIDATION_CODE, "validation"));
            String generatedPassword = String.format("%04d", 
                    random.nextInt(9999));
            params.add(new BasicNameValuePair(PARAM_PASSWORD, 
                    generatedPassword));
        }
        
        
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        random = new Random();
        prefs = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        final String storedPhoneNumber = prefs.getString(ComandaPreferences.PHONE_NUMBER, null);
        if(storedPhoneNumber != null){
            //FIXME: Should't register again in this case
            registerUser(storedPhoneNumber);
        }
        else{
            setContentView(R.layout.enter_personal_data);
            okButton = (Button)findViewById(R.id.okPhoneButton);
            phoneET = (EditText)findViewById(R.id.etPhoneNumber);
            okButton.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    final String phoneNumber = 
                            phoneET.getText().toString();
                    if(validatePhone(phoneNumber)){
                        Editor prefsEditor = prefs.edit();
                        prefsEditor.putString(
                                ComandaPreferences.PHONE_NUMBER, 
                                phoneNumber);
                        prefsEditor.commit();
                        registerUser(phoneNumber);
                    }
                    else{
                        Constants.showErrorDialog(
                                R.string.incorrect_phone_number, 
                                InputYourDataActivity.this);
                    }
                }
            });
        }
    }

    private void registerUser(String phone){
        GetUserData getData = new GetUserData();
        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair(PARAM_PHONE_NUMER, phone));
        getData.execute(this, "/registerUser", params, UserIdHandler.class);
        
    }
    
    private boolean validatePhone(String phone){
        return phone.length() > 0;
    }
    
    private void startSelectTable(){
        Intent intent = new Intent(getApplicationContext(), SelectTableActivity.class);
        startActivity(intent);
    }
    
}

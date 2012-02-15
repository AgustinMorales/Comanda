package com.company.comanda.brian.test;

import roboguice.test.RoboActivityUnitTestCase;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;

import com.company.comanda.brian.ComandaActivity;
import com.company.comanda.brian.R;
import com.company.comanda.brian.SelectTableActivity;

public class TestAsyncDataProvider extends RoboActivityUnitTestCase<SelectTableActivity> {

    protected Intent intent = new Intent(Intent.ACTION_MAIN);
    
    
    
    public TestAsyncDataProvider() {
        super(SelectTableActivity.class);
    }

    @MediumTest
    public void test01(){
        setApplication( new ComandaTestApp( getInstrumentation().getTargetContext() ) );
        startActivity(new Intent(),null, null);
        Activity activity = getActivity();
        assertNotNull(activity);
        
        ((SelectTableActivity)activity).onScanCodeClick();
        
        Intent intent = getStartedActivityIntent();
        assertEquals("Restaurant name", intent.getExtras().getString(ComandaActivity.EXTRA_REST_NAME));
    }
}

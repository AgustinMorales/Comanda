package com.company.comanda.brian.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.company.comanda.brian.SelectTableActivity;
import com.jayway.android.robotium.solo.Solo;

public class TestAsyncDataProvider extends ActivityInstrumentationTestCase2<SelectTableActivity> {

    protected Intent intent = new Intent(Intent.ACTION_MAIN);
    
    private Solo solo;
    
    public TestAsyncDataProvider() {
        super("com.company.comanda.brian", SelectTableActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }
    public void test01(){
        solo.clickOnText("Scan code");
    }
}

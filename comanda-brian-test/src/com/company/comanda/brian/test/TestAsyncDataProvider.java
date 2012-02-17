package com.company.comanda.brian.test;

import junit.framework.Assert;
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
        solo.clickOnText(solo.getString(
                com.company.comanda.brian.R.string.order_food));
        solo.sleep(1000);
        solo.clickOnButton(solo.getString(
                com.company.comanda.brian.R.string.add_new_address));
        solo.sleep(1000);
        solo.enterText(0, "Puerto de Envalira, 1, Sevilla");
        solo.enterText(1, "10 D");
        solo.clickOnText(solo.getString(
                com.company.comanda.brian.R.string.add_address_button));
        Assert.assertTrue(solo.searchText("Puerto de Envalira, 1, Sevilla"));
        solo.goBack();
        solo.clickOnText(solo.getString(
                com.company.comanda.brian.R.string.order_food));
        Assert.assertTrue(solo.searchText("Puerto de Envalira, 1, Sevilla"));
        
    }
}

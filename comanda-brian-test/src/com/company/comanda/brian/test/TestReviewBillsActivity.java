package com.company.comanda.brian.test;

import junit.framework.Assert;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.company.comanda.brian.ReviewBillsActivity;
import com.jayway.android.robotium.solo.Solo;

public class TestReviewBillsActivity extends ActivityInstrumentationTestCase2<ReviewBillsActivity> {

    protected Intent intent = new Intent(Intent.ACTION_MAIN);
    
    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    
    private Solo solo;
    
    public TestReviewBillsActivity() {
        super("com.company.comanda.brian", 
                ReviewBillsActivity.class);
//        intent.getExtras().putString(ReviewBillsActivity.EXTRA_USER_ID, USER_ID);
//        intent.getExtras().putString(ReviewBillsActivity.EXTRA_PASSWORD, PASSWORD);
//        setActivityIntent(intent);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }
    public void test01(){
        Assert.assertTrue(solo.searchText("Telepizza"));
        Assert.assertTrue(solo.searchText("Burguer King"));
        solo.clickOnText("Telepizza");
        Assert.assertTrue(solo.searchText("Carbonara"));
    }
}
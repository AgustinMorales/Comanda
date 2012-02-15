package com.company.comanda.brian.test;

import java.util.List;

import roboguice.application.RoboApplication;
import android.content.Context;
import android.test.suitebuilder.annotation.MediumTest;

import com.company.comanda.brian.test.guice.TestHttpRetrieverModule;
import com.google.inject.Module;

public class ComandaTestApp extends RoboApplication {

    
    public ComandaTestApp( Context context ) {
        super();
        attachBaseContext(context);
    }
    
    @Override
    protected void addApplicationModules(List<Module> modules) {
        modules.add(new TestHttpRetrieverModule());
    }
    
    
    
    
}

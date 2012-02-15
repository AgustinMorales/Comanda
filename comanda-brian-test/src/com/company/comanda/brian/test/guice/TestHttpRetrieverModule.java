package com.company.comanda.brian.test.guice;

import com.company.comanda.brian.helpers.HttpRetriever;
import com.company.comanda.brian.test.stubs.StubHttpRetriever;

import roboguice.config.AbstractAndroidModule;

public class TestHttpRetrieverModule extends AbstractAndroidModule{

    @Override
    protected void configure() {
        bind(HttpRetriever.class).to(StubHttpRetriever.class);
        
    }
    
    

}

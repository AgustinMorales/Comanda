package com.company.comanda.quagmire.guice;

import com.company.comanda.quagmire.BillsCheckerImpl;
import com.company.comanda.quagmire.ServerConnector;
import com.company.comanda.quagmire.ServerConnectorImpl;
import com.google.inject.AbstractModule;

public class QuagmireModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BillsCheckerImpl.class);
        bind(ServerConnector.class).to(ServerConnectorImpl.class);
    }

}

package com.company.comanda.quagmire.guice;

import com.company.comanda.quagmire.BillsCheckerFactory;
import com.company.comanda.quagmire.BillsCheckerImpl;
import com.company.comanda.quagmire.QuagmireUI;
import com.company.comanda.quagmire.QuagmmireUIImpl;
import com.company.comanda.quagmire.ServerConnector;
import com.company.comanda.quagmire.ServerConnectorImpl;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class QuagmireModule extends AbstractModule {

    @Override
    protected void configure() {
    	install(new FactoryModuleBuilder()
        .implement(BillsCheckerImpl.class, BillsCheckerImpl.class)
        .build(BillsCheckerFactory.class));
        bind(ServerConnector.class).to(ServerConnectorImpl.class);
        bind(QuagmireUI.class).to(QuagmmireUIImpl.class);
    }

}

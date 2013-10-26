package com.company.comanda.peter.server.guice;

import com.company.comanda.peter.server.SessionAttributes;
import com.company.comanda.peter.server.SessionAttributesFactory;
import com.company.comanda.peter.server.SessionAttributesHttp;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class SessionModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
        .implement(SessionAttributes.class, SessionAttributesHttp.class)
        .build(SessionAttributesFactory.class));
    }

}

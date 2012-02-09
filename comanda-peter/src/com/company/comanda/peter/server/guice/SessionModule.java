package com.company.comanda.peter.server.guice;

import com.company.comanda.peter.server.SessionAttributes;
import com.company.comanda.peter.server.SessionAttributesHttp;
import com.google.inject.AbstractModule;

public class SessionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SessionAttributes.class).to(SessionAttributesHttp.class);

    }

}

package com.company.comanda.brian.guice;

import com.company.comanda.brian.SelectTableActivity;
import com.company.comanda.brian.helpers.HttpRetriever;
import com.company.comanda.brian.helpers.HttpRetrieverImpl;
import com.google.inject.AbstractModule;

public class BrianModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HttpRetriever.class).to(HttpRetrieverImpl.class);
        bind(SelectTableActivity.GetTableData.class);
    }

}

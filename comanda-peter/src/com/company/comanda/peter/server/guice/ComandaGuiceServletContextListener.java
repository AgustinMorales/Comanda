package com.company.comanda.peter.server.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class ComandaGuiceServletContextListener extends GuiceServletContextListener {

  @Override protected Injector getInjector() {
    return Guice.createInjector(
        new ComandaServletModule(),
        new BusinessModule(),
        new SessionModule(),
        new NotificationModule());
  }
}

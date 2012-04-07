package com.company.comanda.peter.server.guice;

import com.company.comanda.peter.server.notification.NotificationManager;
import com.company.comanda.peter.server.notification.NotificationManagerImpl;
import com.company.comanda.peter.server.notification.PhoneNotifier;
import com.company.comanda.peter.server.notification.TwilioPhoneNotifier;
import com.google.inject.AbstractModule;

public class NotificationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NotificationManager.class).to(NotificationManagerImpl.class);
        bind(PhoneNotifier.class).to(TwilioPhoneNotifier.class);
    }
}

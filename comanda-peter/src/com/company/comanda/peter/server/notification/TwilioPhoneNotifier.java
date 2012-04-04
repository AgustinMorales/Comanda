package com.company.comanda.peter.server.notification;

import javax.inject.Inject;

public class TwilioPhoneNotifier implements PhoneNotifier {

    @Inject
    public TwilioPhoneNotifier(){
        super();
    }
    @Override
    public void call(String phone) {
        // TODO Auto-generated method stub

    }

}

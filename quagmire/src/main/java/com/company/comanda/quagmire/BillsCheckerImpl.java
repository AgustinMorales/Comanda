package com.company.comanda.quagmire;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.assistedinject.Assisted;

public class BillsCheckerImpl implements Runnable{

    private static final Logger log = 
            LoggerFactory.getLogger(BillsCheckerImpl.class);
    
    private static final int PERIOD = 20000;
    private ServerConnector connector;
    private QuagmireUI uiManager;
    private String username;
    private String password;
    private boolean run;
    
    @Inject
    public BillsCheckerImpl(ServerConnector connector, QuagmireUI uiManager,
            @Assisted String username, @Assisted String password){
        this.connector = connector;
        this.uiManager = uiManager;
        this.username = username;
        this.password = password;
        this.run = true;
    }

    public void run() {
        while(run){
            doCheck();
            try {
                Thread.sleep(PERIOD);
            } catch (InterruptedException e) {
                log.warn("Check loop interrupted. run={}", run, e);
            }
        }
    }
    
    public void doCheck(){
        String data = connector.getData(username, password);
        try{
            int noOfPendingBills = Integer.parseInt(data);
            uiManager.notifyPendingBills(noOfPendingBills);
        }
        catch(NumberFormatException e){
            log.error("Could not parse number: {}",data,e);
        }
        
    }
    
    public void stop(){
        run = false;
    }
}

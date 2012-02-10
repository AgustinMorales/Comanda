package com.company.comanda.peter.stubs;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.dev.HighRepJobPolicy;

public class FirstOperationOnlyPolicy implements HighRepJobPolicy {

    private static final Logger log =
            Logger.getLogger(FirstOperationOnlyPolicy.class.getName());
    
    int counter;
    
    public FirstOperationOnlyPolicy(){
        log.info("New policy instance");
        counter = 0;
    }
    
    @Override
    public boolean shouldApplyNewJob(Key arg0) {
        boolean result = false;
        if(counter == 0){
            counter = counter + 1;
            log.info("Appliying operation");
            result = true;
        }
        return result;
    }

    @Override
    public boolean shouldRollForwardExistingJob(Key arg0) {
        return false;
    }

}

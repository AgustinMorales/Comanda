package com.company.comanda.quagmire;

import com.google.inject.assistedinject.Assisted;

public interface BillsCheckerFactory {

    BillsCheckerImpl create(@Assisted("username") String username, 
    		@Assisted("password") String password);
}

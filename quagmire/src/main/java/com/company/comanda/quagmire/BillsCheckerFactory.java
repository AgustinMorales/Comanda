package com.company.comanda.quagmire;

public interface BillsCheckerFactory {

    BillsCheckerImpl create(String username, String password);
}

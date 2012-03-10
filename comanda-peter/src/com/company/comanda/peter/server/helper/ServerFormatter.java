package com.company.comanda.peter.server.helper;

public class ServerFormatter {

    public static String money(float amount){
        return String.format("%.2f €", amount);
    }
}

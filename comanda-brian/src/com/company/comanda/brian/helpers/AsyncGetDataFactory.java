package com.company.comanda.brian.helpers;

import com.company.comanda.brian.xmlhandlers.RestaurantAndTableXMLHandler.ParsedData;

public class AsyncGetDataFactory {

    public AsyncGetData<ParsedData> createParsedData(){
        return new AsyncGetData<ParsedData>() {
        };
    }
}

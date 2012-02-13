package com.company.comanda.brian.xmlhandlers;

import org.xml.sax.helpers.DefaultHandler;

public abstract class ComandaXMLHandler<T> extends DefaultHandler {

    public abstract T getParsedData();
}

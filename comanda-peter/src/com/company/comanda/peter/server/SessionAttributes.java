package com.company.comanda.peter.server;

public interface SessionAttributes {

    Object getAttribute(String name);
    void setAttribute(String name, Object value);
}

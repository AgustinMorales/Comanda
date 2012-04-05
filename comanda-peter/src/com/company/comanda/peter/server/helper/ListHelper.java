package com.company.comanda.peter.server.helper;

import java.util.List;

public class ListHelper {
    public static List cutList(List list, int start, int length){
        int size = list.size();
        start = Math.max(0, Math.min(start, size - 1));
        length = Math.min(length, size - start);

        list = list.subList(start, start + length);
        return list;
    }
}

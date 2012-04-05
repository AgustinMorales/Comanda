package com.company.comanda.peter.server.helper;

import com.company.comanda.peter.shared.Qualifiers;

public class QualifierTranslator {

    public static String translate(Qualifiers qualifier){
        String result = "";
        switch(qualifier){
        case TAPA:
            result = " (Tapa)";
            break;
        case HALF:
            result = " (Media)";
            break;
        case FULL:
            result = " (Ración)";
            break;
        case SMALL:
            result = " (Pequeña)";
            break;
        case MEDIUM:
            result = " (Mediana)";
            break;
        case LARGE:
            result = " (Grande)";
            break;
        }
        return result;
    }
}

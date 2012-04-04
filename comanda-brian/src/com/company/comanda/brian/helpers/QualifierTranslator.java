package com.company.comanda.brian.helpers;

import com.company.comanda.brian.R;

import android.content.Context;

public class QualifierTranslator {

    public static CharSequence translate(String qualifier, Context context){
        CharSequence result;
        if("SMALL".equals(qualifier)){
            result = context.getText(R.string.qualSmall);
        }
        else if("MEDIUM".equals(qualifier)){
            result = context.getText(R.string.qualMedium);
        }
        else if("LARGE".equals(qualifier)){
            result = context.getText(R.string.qualLarge);
        }
        else if("TAPA".equals(qualifier)){
            result = context.getText(R.string.qualTapa);
        }
        else if("HALF".equals(qualifier)){
            result = context.getText(R.string.qualHalf);
        }
        else if("FULL".equals(qualifier)){
            result = context.getText(R.string.qualFull);
        }
        else{
            result = "";
        }
        return result;
    }
}

package com.company.comanda.peter;

import org.junit.Assert;
import org.junit.Test;

public class TestGetTwiML {

    @Test
    public void testLetterSeparation(){
        String result = "10 d".replaceAll("(?<=[0-9]) (?=[A-Za-z])", ", ");
        Assert.assertEquals("10, d", result);
    }
    
    @Test
    public void testPhoneticAlphablet(){
        String result = "Juan pablos, numero 3, 10, d".replaceAll(", d\\b", ", d de dinamarca");
        Assert.assertEquals("Juan pablos, numero 3, 10, d de dinamarca", result);
        result = "Juan pablos, numero 3, 10, d ".replaceAll(", d\\b", ", d de dinamarca");
        Assert.assertEquals("Juan pablos, numero 3, 10, d de dinamarca ", result);
    }
}

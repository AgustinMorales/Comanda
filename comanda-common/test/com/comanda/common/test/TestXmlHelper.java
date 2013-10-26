package com.comanda.common.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.company.comanda.common.XmlHelper;

public class TestXmlHelper {

    @Test
    public void testEnclose(){
        String tag = "Hola";
        Map<String, String> atts = new HashMap<String, String>(2);
        atts.put("uno", "valor");
        atts.put("elotro", "...");
        String contents = "Este es el contenido";
        String result = XmlHelper.enclose(tag, atts, contents);
        Assert.assertEquals("<Hola uno=\"valor\" elotro=\"...\">Este es el contenido</Hola>", result);
    }
}

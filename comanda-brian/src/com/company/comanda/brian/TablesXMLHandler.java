package com.company.comanda.brian;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.company.comanda.brian.model.FoodMenuItem;

import android.util.Log;


public class TablesXMLHandler extends DefaultHandler
{

    // ===========================================================
    // Fields
    // ===========================================================

    private boolean in_table = false;

    private ArrayList<String> tables = null;

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public ArrayList<String> getParsedData() 
    {
        return this.tables;
    }

    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void startDocument() throws SAXException 
    {
        Log.e("XMLHandler", "Initiating parser...");
        this.tables = new ArrayList<String>();
    }

    @Override
    public void endDocument() throws SAXException 
    {
        // Nothing to do
    }

    /** Gets be called on opening tags like: 
     * <tag> 
     * Can provide attribute(s), when xml was like:
     * <tag attribute="attributeValue">*/
    @Override
    public void startElement(String namespaceURI, 
            String localName, String qName, 
            Attributes atts) throws SAXException{
        if (localName.equals("Table")) 
        {
            this.in_table = true;
            Log.e("XMLHandler", "Found a table");
        }
    }

    /** Gets be called on closing tags like: 
     * </tag> */
    @Override
    public void endElement(String namespaceURI, String localName,
            String qName) throws SAXException {
        if (localName.equals("Table")) 
        {
            this.in_table = false;
        }
    }

    /** Gets be called on the following structure: 
     * <tag>characters</tag> */

    @Override
    public void characters(char ch[], int start, int length) 
    {
        if(this.in_table)
        {
            String textBetween = new String(ch, start, length);
            this.tables.add(textBetween);
        }
    }
}

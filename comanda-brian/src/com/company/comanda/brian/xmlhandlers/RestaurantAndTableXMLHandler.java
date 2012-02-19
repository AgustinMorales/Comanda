package com.company.comanda.brian.xmlhandlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import static com.company.comanda.common.XmlTags.RestaurantAndTableData.*;

public class RestaurantAndTableXMLHandler extends ComandaXMLHandler<RestaurantAndTableXMLHandler.ParsedData>
{

    public static class ParsedData{
        public String restName;
        public String restId;
        public String tableName;
        public String tableId;
    }

    // ===========================================================
    // Fields
    // ===========================================================

    
    private boolean in_table = false;
    private boolean in_restaurant = false;
    private boolean in_name = false;
    private boolean in_id = false;

    private ParsedData data = null;

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public ParsedData getParsedData() 
    {
        return this.data;
    }

    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void startDocument() throws SAXException 
    {
        Log.e("XMLHandler", "Initiating parser...");
        this.data = new ParsedData();
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
        if (localName.equals(TABLE)) 
        {
            this.in_table = true;
            Log.e("XMLHandler", "Found a table");
        }
        else if (localName.equals(RESTAURANT)) 
        {
            this.in_restaurant = true;
            Log.e("XMLHandler", "Found a restaurant");
        }
        else if (localName.equals(NAME)) 
        {
            this.in_name = true;
            Log.e("XMLHandler", "Found a name");
        }
        else if (localName.equals(ID)) 
        {
            this.in_id = true;
            Log.e("XMLHandler", "Found an ID");
        }
    }

    /** Gets be called on closing tags like: 
     * </tag> */
    @Override
    public void endElement(String namespaceURI, String localName,
            String qName) throws SAXException {
        if (localName.equals(TABLE)) 
        {
            this.in_table = false;
        }
        else if (localName.equals(RESTAURANT)) 
        {
            this.in_restaurant = false;
        }
        else if (localName.equals(NAME)) 
        {
            this.in_name = false;
        }
        else if (localName.equals(ID)) 
        {
            this.in_id = false;
        }
    }

    /** Gets be called on the following structure: 
     * <tag>characters</tag> */

    @Override
    public void characters(char ch[], int start, int length) 
    {
        String textBetween = new String(ch, start, length);
        if(this.in_name)
        {
            if(this.in_table){
                this.data.tableName = textBetween;
            }
            else if(this.in_restaurant){
                this.data.restName = textBetween;
            }
        }
        else if(this.in_id)
        {
            if(this.in_table){
                this.data.tableId = textBetween;
            }
            else if(this.in_restaurant){
                this.data.restId = textBetween;
            }
        }
    }
}

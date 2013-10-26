package com.company.comanda.brian.xmlhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


import static com.company.comanda.common.XmlTags.RestaurantAndTableData.*;

public class RestaurantAndTableXMLHandler extends ComandaXMLHandler<RestaurantAndTableXMLHandler.ParsedData>
{

    private static final Logger log = LoggerFactory.getLogger(RestaurantAndTableXMLHandler.class);
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
        log.debug("Initiating parser...");
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
            log.debug("Found a table");
        }
        else if (localName.equals(RESTAURANT)) 
        {
            this.in_restaurant = true;
            log.debug("Found a restaurant");
        }
        else if (localName.equals(NAME)) 
        {
            this.in_name = true;
            log.debug("Found a name");
        }
        else if (localName.equals(ID)) 
        {
            this.in_id = true;
            log.debug("Found an ID");
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

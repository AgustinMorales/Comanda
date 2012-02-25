package com.company.comanda.brian.xmlhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


import static com.company.comanda.common.XmlTags.UserData.*;

public class UserIdHandler extends ComandaXMLHandler<String>
{

    // ===========================================================
    // Fields
    // ===========================================================

    private static final Logger log = LoggerFactory.getLogger(UserIdHandler.class);
    private boolean in_user = false;
    private boolean in_id = false;


    private String userId;

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public String getParsedData() 
    {
        return this.userId;
    }

    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void startDocument() throws SAXException 
    {
        log.debug("Initiating parser...");
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
        if (localName.equals(USER)) 
        {
            this.in_user = true;
        }
        else if (localName.equals(ID)) 
        {
            this.in_id = true;
        }
    }

    /** Gets be called on closing tags like: 
     * </tag> */
    @Override
    public void endElement(String namespaceURI, String localName,
            String qName) throws SAXException {
        if (localName.equals(USER)) 
        {
            this.in_user = false;
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
        if(this.in_id)
        {
            String textBetween = new String(ch, start, length);
            if(this.in_user){
                userId = textBetween;
            }
        }
    }
}

package com.company.comanda.brian.xmlhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;



import static com.company.comanda.common.XmlTags.BooleanResult.*;

public class BooleanHandler extends ComandaXMLHandler<Boolean>
{

    // ===========================================================
    // Fields
    // ===========================================================

    private static final Logger log = LoggerFactory.getLogger(BooleanHandler.class);

    private boolean in_result = false;


    private Boolean result;

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public Boolean getParsedData() 
    {
        return this.result;
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
        if (localName.equals(RESULT)) 
        {
            this.in_result = true;
        }
    }

    /** Gets be called on closing tags like: 
     * </tag> */
    @Override
    public void endElement(String namespaceURI, String localName,
            String qName) throws SAXException {
        if (localName.equals(RESULT)) 
        {
            this.in_result = false;
        }
    }

    /** Gets be called on the following structure: 
     * <tag>characters</tag> */

    @Override
    public void characters(char ch[], int start, int length) 
    {
        if(this.in_result)
        {
            String textBetween = new String(ch, start, length);
            result = Boolean.parseBoolean(textBetween);
        }
    }
}

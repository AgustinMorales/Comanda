package com.company.comanda.brian.xmlhandlers;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


import com.company.comanda.brian.model.FoodMenuItem;
import static com.company.comanda.common.XmlTags.MenuItemList.*;


public class MenuItemsHandler extends ComandaXMLHandler<ArrayList<FoodMenuItem>>
{

    // ===========================================================
    // Fields
    // ===========================================================
    

    private static final Logger log = LoggerFactory.getLogger(MenuItemsHandler.class);
    
    private boolean in_item = false;
    private boolean in_name = false;
    private boolean in_keyId = false;
    private boolean in_description = false;
    private boolean in_imageString = false;
    private boolean in_categoryId = false;
    private boolean in_price = false;

    private FoodMenuItem item = null;

    private ArrayList<FoodMenuItem> items = null;


    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void startDocument() throws SAXException 
    {
        log.debug("Initiating parser...");
        this.items = new ArrayList<FoodMenuItem>();
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
        if (localName.equals(ITEM)) 
        {
            this.in_item = true;
            item = new FoodMenuItem();
            log.debug("Found an Item");
        }
        else if (localName.equals(NAME)) 
        {
            this.in_name = true;
        }
        else if (localName.equals(IMAGE_STRING)) 
        {
            this.in_imageString = true;
        }
        else if (localName.equals(ID)) 
        {
            this.in_keyId = true;
        }
        else if (localName.equals(DESCRIPTION)) 
        {
            this.in_description = true;
        }
        else if (localName.equals(CATEGORY_ID)) 
        {
            this.in_categoryId = true;
        }
        else if (localName.equals(PRICE)) 
        {
            this.in_price = true;
        }
    }

    /** Gets be called on closing tags like: 
     * </tag> */
    @Override
    public void endElement(String namespaceURI, String localName,
            String qName) throws SAXException {
        if (localName.equals(ITEM)) 
        {
            this.in_item = false;
            items.add(item);
        }
        else if (localName.equals(NAME)) 
        {
            this.in_name = false;
        }
        else if (localName.equals(IMAGE_STRING)) 
        {
            this.in_imageString = false;
        }
        else if (localName.equals(ID)) 
        {
            this.in_keyId = false;
        }
        else if (localName.equals(DESCRIPTION)) 
        {
            this.in_description = false;
        }
        else if (localName.equals(CATEGORY_ID)) 
        {
            this.in_categoryId = false;
        }
        else if (localName.equals(PRICE)) 
        {
            this.in_price = false;
        }
    }

    /** Gets be called on the following structure: 
     * <tag>characters</tag> */

    @Override
    public void characters(char ch[], int start, int length) 
    {
        if(this.in_item)
        {
            String textBetween = new String(ch, start, length);
            if(this.in_name){
                log.debug("Name: {}", textBetween);
                item.setName(textBetween);
            }
            else if(this.in_imageString){
                log.debug("ImageString: {}", textBetween);
                item.setImageString(textBetween);
            }
                
            else if(this.in_description){
                log.debug("Description: {}", textBetween);
                item.setDescription(textBetween);
            }
            else if(this.in_keyId){
                log.debug("KeyId: {}", textBetween);
                //We are not parsing the long here. What for?
                item.setKeyId(textBetween);
            }
            else if(this.in_categoryId){
                log.debug("CategoryId: {}", textBetween);
                item.setCategoryId(Long.parseLong(textBetween));
            }
            else if(this.in_categoryId){
                log.debug("Price: {}", textBetween);
                item.setPrice(Float.parseFloat(textBetween));
            }
        }
    }

    @Override
    public ArrayList<FoodMenuItem> getParsedData() {
        return this.items;
    }
}

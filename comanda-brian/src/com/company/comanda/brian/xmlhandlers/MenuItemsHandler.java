package com.company.comanda.brian.xmlhandlers;

import java.util.ArrayList;
import java.util.List;

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
    private boolean in_qualifier = false;
    private boolean in_extra_name = false;
    private boolean in_extra_price = false;
    private boolean in_extras_global_name = false;

    
    private StringBuffer name;
    private StringBuffer keyId;
    private StringBuffer description;
    private StringBuffer imageString;
    private StringBuffer categoryId;
    private StringBuffer price;
    private StringBuffer qualifier;
    private StringBuffer extra_name;
    private StringBuffer extra_price;
    private StringBuffer extras_global_name;
    
    
    private FoodMenuItem item = null;
    private List<Float> prices = null;
    private List<String> qualifiers = null;
    private List<String> extras = null;
    private List<Float> extrasPrices = null;
    

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
            prices = new ArrayList<Float>();
            qualifiers = new ArrayList<String>();
            extras = new ArrayList<String>();
            extrasPrices = new ArrayList<Float>();
            log.debug("Found an Item");
        }
        else if (localName.equals(NAME)) 
        {
            this.in_name = true;
            name = new StringBuffer();
        }
        else if (localName.equals(IMAGE_STRING)) 
        {
            this.in_imageString = true;
            imageString = new StringBuffer();
        }
        else if (localName.equals(ID)) 
        {
            this.in_keyId = true;
            keyId = new StringBuffer();
        }
        else if (localName.equals(DESCRIPTION)) 
        {
            this.in_description = true;
            description = new StringBuffer();
        }
        else if (localName.equals(CATEGORY_ID)) 
        {
            this.in_categoryId = true;
            categoryId = new StringBuffer();
        }
        else if (localName.equals(PRICE)) 
        {
            this.in_price = true;
            price = new StringBuffer();
        }
        else if (localName.equals(QUALIFIER)) 
        {
            this.in_qualifier = true;
            qualifier = new StringBuffer();
        }
        else if (localName.equals(EXTRA_NAME)) 
        {
            this.in_extra_name = true;
            extra_name = new StringBuffer();
        }
        else if (localName.equals(EXTRA_PRICE)) 
        {
            this.in_extra_price = true;
            extra_price = new StringBuffer();
        }
        else if (localName.equals(EXTRAS_GLOBAL_NAME)) 
        {
            this.in_extras_global_name = true;
            extras_global_name = new StringBuffer();
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
            item.setPrices(prices);
            item.setQualifiers(qualifiers);
            item.setExtras(extras);
            item.setExtrasPrice(extrasPrices);
            items.add(item);
        }
        else if (localName.equals(NAME)) 
        {
            this.in_name = false;
            item.setName(name.toString());
        }
        else if (localName.equals(IMAGE_STRING)) 
        {
            this.in_imageString = false;
            item.setImageString(imageString.toString());
        }
        else if (localName.equals(ID)) 
        {
            this.in_keyId = false;
            item.setKeyId(keyId.toString());
        }
        else if (localName.equals(DESCRIPTION)) 
        {
            this.in_description = false;
            item.setDescription(description.toString());
        }
        else if (localName.equals(CATEGORY_ID)) 
        {
            this.in_categoryId = false;
            item.setCategoryId(Long.parseLong(categoryId.toString()));
        }
        else if (localName.equals(PRICE)) 
        {
            this.in_price = false;
            prices.add(Float.parseFloat(price.toString()));
        }
        else if (localName.equals(QUALIFIER)) 
        {
            this.in_qualifier = false;
            qualifiers.add(qualifier.toString());
        }
        else if (localName.equals(EXTRA_NAME)) 
        {
            this.in_extra_name = false;
            extras.add(extra_name.toString());
        }
        else if (localName.equals(EXTRA_PRICE)) 
        {
            this.in_extra_price = false;
            extrasPrices.add(Float.parseFloat(extra_price.toString()));
        }
        else if (localName.equals(EXTRAS_GLOBAL_NAME)) 
        {
            this.in_extras_global_name = false;
            item.setExtrasName(extras_global_name.toString());
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
                name.append(textBetween);
            }
            else if(this.in_imageString){
                log.debug("ImageString: {}", textBetween);
                imageString.append(textBetween);
            }
                
            else if(this.in_description){
                log.debug("Description: {}", textBetween);
                description.append(textBetween);
            }
            else if(this.in_keyId){
                log.debug("KeyId: {}", textBetween);
                //We are not parsing the long here. What for?
                keyId.append(textBetween);
            }
            else if(this.in_categoryId){
                log.debug("CategoryId: {}", textBetween);
                categoryId.append(textBetween);
            }
            else if(this.in_price){
                log.debug("Price: {}", textBetween);
                price.append(textBetween);
            }
            else if(this.in_qualifier){
                log.debug("Qualifier: {}", textBetween);
                qualifier.append(textBetween);
            }
            else if(this.in_extra_name){
                log.debug("Extra: {}", textBetween);
                extra_name.append(textBetween);
            }
            else if(this.in_extra_price){
                log.debug("Extra price: {}", textBetween);
                extra_price.append(textBetween);
            }
            else if(this.in_extras_global_name){
                log.debug("Extras global name: {}", textBetween);
                extras_global_name.append(textBetween);
            }
        }
    }

    @Override
    public ArrayList<FoodMenuItem> getParsedData() {
        return this.items;
    }
}

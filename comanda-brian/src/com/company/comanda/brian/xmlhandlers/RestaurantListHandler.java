package com.company.comanda.brian.xmlhandlers;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


import com.company.comanda.brian.model.Restaurant;

import static com.company.comanda.common.XmlTags.Restaurantlist.*;


public class RestaurantListHandler extends ComandaXMLHandler<ArrayList<Restaurant>>
{

    // ===========================================================
    // Fields
    // ===========================================================

    private boolean in_restaurant = false;
    private boolean in_name = false;
    private boolean in_id = false;
    private boolean in_image = false;

    private Restaurant item = null;

    private ArrayList<Restaurant> restaurants = null;

    private static final Logger log = LoggerFactory.getLogger(RestaurantListHandler.class);

    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void startDocument() throws SAXException 
    {
        log.debug("Initiating parser...");
        restaurants = new ArrayList<Restaurant>();
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
        if (localName.equals(RESTAURANT)) 
        {
            this.in_restaurant = true;
            item = new Restaurant();
            log.debug("Found a Restaurant");
        }
        else if (localName.equals(NAME)) 
        {
            this.in_name = true;
        }
        else if (localName.equals(ID)) 
        {
            this.in_id = true;
        }
        else if (localName.equals(IMAGE_URL)) 
        {
            this.in_image = true;
        }
    }

    /** Gets be called on closing tags like: 
     * </tag> */
    @Override
    public void endElement(String namespaceURI, String localName,
            String qName) throws SAXException {
        if (localName.equals(RESTAURANT)) 
        {
            this.in_restaurant = false;
            restaurants.add(item);
        }
        else if (localName.equals(NAME)) 
        {
            this.in_name = false;
        }
        else if (localName.equals(ID)) 
        {
            this.in_id = false;
        }
        else if (localName.equals(IMAGE_URL)) 
        {
            this.in_image = false;
        }
    }

    /** Gets be called on the following structure: 
     * <tag>characters</tag> */

    @Override
    public void characters(char ch[], int start, int length) 
    {
        if(this.in_restaurant)
        {
            String textBetween = new String(ch, start, length);
            if(this.in_name){
                log.debug("Name: {}", textBetween);
                item.name = textBetween;
            }
            else if(this.in_id){
                log.debug("Id: {}", textBetween);
                item.id = textBetween;
            }
            else if(this.in_image){
                log.debug("ImageURL: {}", textBetween);
                item.imageURL = textBetween;
            }
        }
    }

    @Override
    public ArrayList<Restaurant> getParsedData() {
        return restaurants;
    }
}

package com.company.comanda.brian.xmlhandlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.company.comanda.brian.model.Restaurant;


public class RestaurantListHandler extends ComandaXMLHandler<ArrayList<Restaurant>>
{

    // ===========================================================
    // Fields
    // ===========================================================
        
    private static final String RESTAURANT = "Restaurant";
    private static final String NAME = "Name";
    private static final String ID = "Id";

    private boolean in_restaurant = false;
    private boolean in_name = false;
    private boolean in_id = false;

    private Restaurant item = null;

    private ArrayList<Restaurant> restaurants = null;


    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void startDocument() throws SAXException 
    {
        Log.e("XMLHandler", "Initiating parser...");
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
            Log.e("XMLHandler", "Found a Restaurant");
        }
        else if (localName.equals(NAME)) 
        {
            this.in_name = true;
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
                Log.d("Comanda", "Name: " + textBetween);
                item.name = textBetween;
            }
            else if(this.in_id){
                Log.d("Comanda", "Id: " + textBetween);
                item.id = textBetween;
            }
        }
    }

    @Override
    public ArrayList<Restaurant> getParsedData() {
        return restaurants;
    }
}

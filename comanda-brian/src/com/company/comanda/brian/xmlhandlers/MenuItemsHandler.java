package com.company.comanda.brian.xmlhandlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.company.comanda.brian.model.FoodMenuItem;


public class MenuItemsHandler extends ComandaXMLHandler<ArrayList<FoodMenuItem>>
{

    // ===========================================================
    // Fields
    // ===========================================================

    private boolean in_item = false;
    private boolean in_name = false;
    private boolean in_keyId = false;
    private boolean in_description = false;
    private boolean in_imageString = false;

    private FoodMenuItem item = null;

    private ArrayList<FoodMenuItem> items = null;


    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void startDocument() throws SAXException 
    {
        Log.e("XMLHandler", "Initiating parser...");
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
        if (localName.equals("Item")) 
        {
            this.in_item = true;
            item = new FoodMenuItem();
            Log.e("XMLHandler", "Found an Item");
        }
        else if (localName.equals("Name")) 
        {
            this.in_name = true;
        }
        else if (localName.equals("ImageString")) 
        {
            this.in_imageString = true;
        }
        else if (localName.equals("KeyId")) 
        {
            this.in_keyId = true;
        }
        else if (localName.equals("Description")) 
        {
            this.in_description = true;
        }
    }

    /** Gets be called on closing tags like: 
     * </tag> */
    @Override
    public void endElement(String namespaceURI, String localName,
            String qName) throws SAXException {
        if (localName.equals("Item")) 
        {
            this.in_item = false;
            items.add(item);
        }
        else if (localName.equals("Name")) 
        {
            this.in_name = false;
        }
        else if (localName.equals("ImageString")) 
        {
            this.in_imageString = false;
        }
        else if (localName.equals("KeyId")) 
        {
            this.in_keyId = false;
        }
        else if (localName.equals("Description")) 
        {
            this.in_description = false;
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
                Log.d("Comanda", "Name: " + textBetween);
                item.setName(textBetween);
            }
            else if(this.in_imageString){
                Log.d("Comanda", "ImageString: " + textBetween);
                item.setImageString(textBetween);
            }
                
            else if(this.in_description){
                Log.d("Comanda", "Description: " + textBetween);
                item.setDescription(textBetween);
            }
            else if(this.in_keyId){
                Log.d("Comanda", "KeyId: " + textBetween);
                //We are not parsing the long here. What for?
                item.setKeyId(textBetween);
            }
        }
    }

    @Override
    public ArrayList<FoodMenuItem> getParsedData() {
        return this.items;
    }
}

package com.company.comanda.brian.xmlhandlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.company.comanda.brian.model.Category;

public class CategoriesHandler extends ComandaXMLHandler<ArrayList<Category>> {

 // ===========================================================
    // Fields
    // ===========================================================

    private static final String CATEGORY = "Category";
    private static final String ID = "Id";
    private static final String NAME = "Name";
    
    
    private boolean in_category = false;
    private boolean in_id = false;
    private boolean in_name = false;

    private Category category;

    private ArrayList<Category> categories = null;
    


    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void startDocument() throws SAXException 
    {
        Log.e("XMLHandler", "Initiating parser...");
        this.categories = new ArrayList<Category>();
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
        if (localName.equals(CATEGORY)) 
        {
            this.in_category = true;
            category = new Category();
            Log.e("XMLHandler", "Found a category");
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
        if (localName.equals(CATEGORY)) 
        {
            this.in_category = false;
            categories.add(category);
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
        if(this.in_category)
        {
            String textBetween = new String(ch, start, length);
            if(this.in_name){
                Log.d("Comanda", "Name: " + textBetween);
                category.name = textBetween;
            }
            else if(this.in_id){
                Log.d("Comanda", "Id: " + textBetween);
                category.id = textBetween;
            }
        }
    }

    @Override
    public ArrayList<Category> getParsedData() {
        return this.categories;
    }

}

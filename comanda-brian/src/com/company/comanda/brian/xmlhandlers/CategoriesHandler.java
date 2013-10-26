package com.company.comanda.brian.xmlhandlers;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


import com.company.comanda.brian.model.Category;
import static com.company.comanda.common.XmlTags.CategoryList.*;

public class CategoriesHandler extends ComandaXMLHandler<ArrayList<Category>> {

 // ===========================================================
    // Fields
    // ===========================================================

    private static final Logger log = LoggerFactory.getLogger(CategoriesHandler.class);
    
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
        log.debug("Initiating parser...");
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
            log.debug("Found a category");
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
                log.debug("Name: {}", textBetween);
                category.name = textBetween;
            }
            else if(this.in_id){
                log.debug("Id: {}", textBetween);
                category.id = Long.parseLong(textBetween);
            }
        }
    }

    @Override
    public ArrayList<Category> getParsedData() {
        return this.categories;
    }

}

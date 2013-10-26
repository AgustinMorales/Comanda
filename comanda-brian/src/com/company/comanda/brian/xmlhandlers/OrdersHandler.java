package com.company.comanda.brian.xmlhandlers;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


import com.company.comanda.brian.model.Order;

import static com.company.comanda.common.XmlTags.Orders.*;

public class OrdersHandler extends ComandaXMLHandler<ArrayList<Order>> {

 // ===========================================================
    // Fields
    // ===========================================================

    private static final Logger log = LoggerFactory.getLogger(OrdersHandler.class);
    
    private boolean in_order = false;
    private boolean in_key_string = false;
    private boolean in_item_name = false;
    private boolean in_item_number = false;
    private boolean in_item_price = false;
    private boolean in_comments = false;


    private Order order;

    private ArrayList<Order> orders = null;
    


    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void startDocument() throws SAXException 
    {
        log.debug("Initiating parser...");
        this.orders = new ArrayList<Order>();
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
        if (localName.equals(ORDER)) 
        {
            this.in_order = true;
            order = new Order();
            log.debug("Found a category");
        }
        else if (localName.equals(KEY_STRING)) 
        {
            this.in_key_string = true;
        }
        else if (localName.equals(MENU_ITEM_NAME)) 
        {
            this.in_item_name = true;
        }
        else if (localName.equals(MENU_ITEM_NUMBER)) 
        {
            this.in_item_number = true;
        }
        else if (localName.equals(PRICE)) 
        {
            this.in_item_price = true;
        }
        else if (localName.equals(COMMENTS)) 
        {
            this.in_comments = true;
        }
    }

    /** Gets be called on closing tags like: 
     * </tag> */
    @Override
    public void endElement(String namespaceURI, String localName,
            String qName) throws SAXException {
        if (localName.equals(ORDER)) 
        {
            this.in_order = false;
            orders.add(order);
        }
        else if (localName.equals(KEY_STRING)) 
        {
            this.in_key_string = false;
        }
        else if (localName.equals(MENU_ITEM_NAME)) 
        {
            this.in_item_name = false;
        }
        else if (localName.equals(MENU_ITEM_NUMBER)) 
        {
            this.in_item_number = false;
        }
        else if (localName.equals(PRICE)) 
        {
            this.in_item_price = false;
        }
        else if (localName.equals(COMMENTS)) 
        {
            this.in_comments = false;
        }
    }

    /** Gets be called on the following structure: 
     * <tag>characters</tag> */

    @Override
    public void characters(char ch[], int start, int length) 
    {
        if(this.in_order)
        {
            String textBetween = new String(ch, start, length);
            if(this.in_key_string){
                log.debug("KeyString: {}", textBetween);
                order.keyString = textBetween;
            }
            else if(this.in_item_name){
                log.debug("ItemName: {}", textBetween);
                order.menuItemName = textBetween;
            }
            else if(this.in_item_number){
                log.debug("ItemNumber: {}", textBetween);
                order.menuItemNumber = 
                        Integer.parseInt(textBetween);
            }
            else if(this.in_item_price){
                log.debug("Price: {}", textBetween);
                order.menuItemPrice = 
                        Float.parseFloat(textBetween);
            }
            else if(this.in_comments){
                log.debug("Comments: {}", textBetween);
                order.comments = textBetween;
            }
        }
    }

    @Override
    public ArrayList<Order> getParsedData() {
        return this.orders;
    }

}

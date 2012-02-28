package com.company.comanda.brian.xmlhandlers;

import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


import com.company.comanda.brian.model.Bill;
import static com.company.comanda.common.XmlTags.Bills.*;

public class BillsHandler extends ComandaXMLHandler<ArrayList<Bill>> {

 // ===========================================================
    // Fields
    // ===========================================================

    private static final Logger log = LoggerFactory.getLogger(BillsHandler.class);
    
    private boolean in_bill = false;
    private boolean in_key_string = false;
    private boolean in_total_amount = false;
    private boolean in_state = false;
    private boolean in_type = false;
    private boolean in_table_name = false;
    private boolean in_address = false;
    private boolean in_restaurant_name = false;
    private boolean in_comments = false;
    private boolean in_open_date = false;


    private Bill bill;

    private ArrayList<Bill> bills = null;
    


    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public void startDocument() throws SAXException 
    {
        log.debug("Initiating parser...");
        this.bills = new ArrayList<Bill>();
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
        if (localName.equals(BILL)) 
        {
            this.in_bill = true;
            bill = new Bill();
            log.debug("Found a category");
        }
        else if (localName.equals(KEY_STRING)) 
        {
            this.in_key_string = true;
        }
        else if (localName.equals(TOTAL_AMOUNT)) 
        {
            this.in_total_amount = true;
        }
        else if (localName.equals(STATE)) 
        {
            this.in_state = true;
        }
        else if (localName.equals(TYPE)) 
        {
            this.in_type = true;
        }
        else if (localName.equals(TABLE_NAME)) 
        {
            this.in_table_name = true;
        }
        else if (localName.equals(ADDRESS)) 
        {
            this.in_address = true;
        }
        else if (localName.equals(RESTAURANT_NAME)) 
        {
            this.in_restaurant_name = true;
        }
        else if (localName.equals(COMMENTS)) 
        {
            this.in_comments = true;
        }
        else if (localName.equals(OPEN_DATE)) 
        {
            this.in_open_date = true;
        }
    }

    /** Gets be called on closing tags like: 
     * </tag> */
    @Override
    public void endElement(String namespaceURI, String localName,
            String qName) throws SAXException {
        if (localName.equals(BILL)) 
        {
            this.in_bill = false;
            bills.add(bill);
        }
        else if (localName.equals(KEY_STRING)) 
        {
            this.in_key_string = false;
        }
        else if (localName.equals(TOTAL_AMOUNT)) 
        {
            this.in_total_amount = false;
        }
        else if (localName.equals(STATE)) 
        {
            this.in_state = false;
        }
        else if (localName.equals(TYPE)) 
        {
            this.in_type = false;
        }
        else if (localName.equals(TABLE_NAME)) 
        {
            this.in_table_name = false;
        }
        else if (localName.equals(ADDRESS)) 
        {
            this.in_address = false;
        }
        else if (localName.equals(RESTAURANT_NAME)) 
        {
            this.in_restaurant_name = false;
        }
        else if (localName.equals(COMMENTS)) 
        {
            this.in_comments = false;
        }
        else if (localName.equals(OPEN_DATE)) 
        {
            this.in_open_date = false;
        }
    }

    /** Gets be called on the following structure: 
     * <tag>characters</tag> */

    @Override
    public void characters(char ch[], int start, int length) 
    {
        if(this.in_bill)
        {
            String textBetween = new String(ch, start, length);
            if(this.in_key_string){
                log.debug("KeyString: {}", textBetween);
                bill.keyString = textBetween;
            }
            else if(this.in_total_amount){
                log.debug("TotalAmount: {}", textBetween);
                bill.totalAmount = Float.parseFloat(textBetween);
            }
            else if(this.in_state){
                log.debug("State: {}", textBetween);
                bill.state = textBetween;
            }
            else if(this.in_type){
                log.debug("Type: {}", textBetween);
                bill.type = textBetween;
            }
            else if(this.in_table_name){
                log.debug("TableName: {}", textBetween);
                bill.tableName = textBetween;
            }
            else if(this.in_address){
                log.debug("Address: {}", textBetween);
                bill.address = textBetween;
            }
            else if(this.in_restaurant_name){
                log.debug("RestaurantName: {}", textBetween);
                bill.restaurantName = textBetween;
            }
            else if(this.in_comments){
                log.debug("Comments: {}", textBetween);
                bill.comments = textBetween;
            }
            else if(this.in_open_date){
                log.debug("OpenDate: {}", textBetween);
                bill.openDate = new Date(textBetween);
            }
        }
    }

    @Override
    public ArrayList<Bill> getParsedData() {
        return this.bills;
    }

}

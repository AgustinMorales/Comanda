package com.company.comanda.brian.helpers;

import static com.company.comanda.common.XmlHelper.close;
import static com.company.comanda.common.XmlHelper.enclose;
import static com.company.comanda.common.XmlHelper.open;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;

import com.company.comanda.common.HttpParams.GetCategories;
import com.company.comanda.common.HttpParams.GetMenuItems;
import com.company.comanda.common.HttpParams.SearchRestaurants;
import com.company.comanda.common.XmlTags.CategoryList;
import com.company.comanda.common.XmlTags.MenuItemList;
import com.company.comanda.common.XmlTags.Restaurantlist;
import com.company.comanda.common.XmlTags;

public class HttpRetriever {

    private static final String DECODE_QR_RESPONSE = 
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<Data>\n" + 
                "\t<Restaurant>\n" + 
                "\t\t<Name>Restaurant name</Name>\n" + 
                "\t\t<Id>Restaurant ID</Id>\n" + 
                "\t</Restaurant>\n" + 
                "\t<Table>\n" + 
                "\t\t<Name>Table name</Name>\n" + 
                "\t\t<Id>Table ID</Id>\n" + 
                "\t</Table>\n" + 
                "</Data>";
    
    private static final String SEARCH_RESTAURANTS_RESPONSE = 
            XmlTags.HEADER +
                open(Restaurantlist.RESTAURANT_LIST) + 
                open(Restaurantlist.RESTAURANT) + 
                enclose(Restaurantlist.NAME, "Casa Manuela") + 
                enclose(Restaurantlist.ID, "123") + 
                close(Restaurantlist.RESTAURANT) + 
                open(Restaurantlist.RESTAURANT) + 
                enclose(Restaurantlist.NAME, "La Tagliatella") + 
                enclose(Restaurantlist.ID, "1234") + 
                close(Restaurantlist.RESTAURANT) + 
                open(Restaurantlist.RESTAURANT) + 
                enclose(Restaurantlist.NAME, "Ginos") + 
                enclose(Restaurantlist.ID, "12345") + 
                close(Restaurantlist.RESTAURANT) + 
                close(Restaurantlist.RESTAURANT_LIST);
    
    private static final String GET_CATEGORIES_RESPONSE = 
            XmlTags.HEADER +
                    open(CategoryList.CATEGORY_LIST) + 
                    open(CategoryList.CATEGORY) + 
                    enclose(CategoryList.NAME, "Entrantes") + 
                    enclose(CategoryList.ID, "1") + 
                    close(CategoryList.CATEGORY) + 
                    open(CategoryList.CATEGORY) + 
                    enclose(CategoryList.NAME, "Primeros") + 
                    enclose(CategoryList.ID, "2") + 
                    close(CategoryList.CATEGORY) + 
                    open(CategoryList.CATEGORY) + 
                    enclose(CategoryList.NAME, "Segundos") + 
                    enclose(CategoryList.ID, "3") + 
                    close(CategoryList.CATEGORY) + 
                    open(CategoryList.CATEGORY) + 
                    enclose(CategoryList.NAME, "Bebidas") + 
                    enclose(CategoryList.ID, "4") + 
                    close(CategoryList.CATEGORY) + 
                    close(CategoryList.CATEGORY_LIST);
    
    private static final String GET_MENU_ITEMS_RESPONSE = 
            XmlTags.HEADER +
                    open(MenuItemList.ITEM_LIST) + 
                    open(MenuItemList.ITEM) + 
                    enclose(MenuItemList.NAME, "Nachos") + 
                    enclose(MenuItemList.ID, "1") + 
                    enclose(MenuItemList.DESCRIPTION, "Unos buenos nachos") + 
                    enclose(MenuItemList.IMAGE_STRING, "") + 
                    enclose(MenuItemList.CATEGORY_ID, "1") + 
                    close(MenuItemList.ITEM) + 
                    open(MenuItemList.ITEM) + 
                    enclose(MenuItemList.NAME, "Pizza") + 
                    enclose(MenuItemList.ID, "2") + 
                    enclose(MenuItemList.DESCRIPTION, "Una pizza") + 
                    enclose(MenuItemList.IMAGE_STRING, "") + 
                    enclose(MenuItemList.CATEGORY_ID, "2") + 
                    close(MenuItemList.ITEM) + 
                    close(MenuItemList.ITEM_LIST);
    
    
    public InputStream execute(HttpPost httpPost) throws IllegalStateException,
            ClientProtocolException, IOException {
        final String uri = httpPost.getURI().toString();
        String response = "";
        if(uri.contains(SearchRestaurants.SERVICE_NAME)){
            response = SEARCH_RESTAURANTS_RESPONSE;
        }
        else if(uri.contains(GetCategories.SERVICE_NAME)){
            response = GET_CATEGORIES_RESPONSE;
        }
        else if(uri.contains(GetMenuItems.SERVICE_NAME)){
            response = GET_MENU_ITEMS_RESPONSE;
        }
        return new ByteArrayInputStream(
                response.getBytes("ISO-8859-1"));
    }

}

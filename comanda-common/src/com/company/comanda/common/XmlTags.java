package com.company.comanda.common;

public class XmlTags {

    public static final String HEADER = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
    
    public static class Restaurantlist{
        public static final String RESTAURANT_LIST = "RestaurantList";
        public static final String RESTAURANT = "Restaurant";
        public static final String ID = "Id";
        public static final String NAME = "Name";
    }
    
    public static class CategoryList{
        public static final String CATEGORY_LIST = "CategoryList";
        public static final String CATEGORY = "Category";
        public static final String ID = "Id";
        public static final String NAME = "Name";
    }
    
    public static class MenuItemList{
        public static final String ITEM_LIST = "CategoryList";
        public static final String ITEM = "Category";
        public static final String ID = "Id";
        public static final String NAME = "Name";
        public static final String DESCRIPTION = "Description";
        public static final String IMAGE_STRING = "ImageString";
        public static final String CATEGORY_ID = "CategoryId";
    }
    
    public static class BooleanResult{
        public static final String RESULT = "Result";
    }
    
    public static class RestaurantAndTableData{
        public static final String DATA = "Data";
        public static final String RESTAURANT = "Restaurant";
        public static final String NAME = "Name";
        public static final String ID = "Id";
        public static final String TABLE = "Table";
    }
    
    public static class UserData{
        public static final String USER = "User";
        public static final String ID = "Id";
    }
    
    public static class Orders{
        public static final String ORDER_LIST = "Orders";
        public static final String ORDER = "Order";
        public static final String KEY_STRING = "KeyString";
        public static final String MENU_ITEM_NAME = "MenuItem";
        public static final String MENU_ITEM_NUMBER = "MenuItemNumber";
        public static final String PRICE = "Price";
        public static final String COMMENTS = "Comments";
    }
    
    public static class KeyStringResult{
        public static final String KEY_STRING = "KeyString";
    }
}

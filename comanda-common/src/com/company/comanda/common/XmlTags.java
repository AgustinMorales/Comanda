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
}

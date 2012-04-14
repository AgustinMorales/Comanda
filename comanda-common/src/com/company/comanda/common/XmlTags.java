package com.company.comanda.common;

public class XmlTags {

    public static final String HEADER = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
    
    public static class Restaurantlist{
        public static final String RESTAURANT_LIST = "RestaurantList";
        public static final String RESTAURANT = "Restaurant";
        public static final String DESCRIPTION = "Description";
        public static final String IMAGE_URL = "imageUrl";
        public static final String ID = "Id";
        public static final String NAME = "Name";
        public static final String DELIVERY_COST = "DeliveryCost";
        public static final String MINIMUM_FOR_DELIVERY = "MinimumForDelivery";
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
        public static final String PRICE = "Price";
        public static final String QUALIFIER = "Qualifier";
        public static final String EXTRAS_GLOBAL_NAME = "ExtrasGlobalName";
        public static final String EXTRA_PRICE = "ExtraPrice";
        public static final String EXTRA_NAME = "ExtraName";
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
    
    public static class Bills{
        public static final String BILL_LIST = "Bills";
        public static final String BILL = "Bill";
        public static final String KEY_STRING = "KeyString";
        public static final String TOTAL_AMOUNT = "TotalAmount";
        public static final String STATE = "State";
        public static final String TYPE = "Type";
        public static final String TABLE_NAME = "TableName";
        public static final String ADDRESS = "Address";
        public static final String RESTAURANT_NAME = "RestaurantName";
        public static final String COMMENTS = "Comments";
        public static final String OPEN_DATE = "OpenDate";
        public static final String ESTIMATED_DELIVERY_DATE = "EstDelivDate";
        public static final String DELIVERY_COST = "DeliveryCost";
    }
}

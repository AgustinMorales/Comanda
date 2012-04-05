package com.company.comanda.common;

public class HttpParams {

    public static class GetItems{
        public static final String RESTAURANT_ID =
                "restaurantId";
    }
    
    
    public static class SearchRestaurants{
        public static final String SERVICE_NAME = "/searchRestaurants";
        public static final String PARAM_LATITUDE = "latitude";
        public static final String PARAM_LONGITUDE = "longitude";
    }
    
    public static class GetCategories{
        public static final String SERVICE_NAME = "/getCategories";
        public static final String PARAM_RESTAURANT_ID = "restaurantId";
    }
    
    public static class GetMenuItems{
        public static final String SERVICE_NAME = "/getMenuItems";
        public static final String PARAM_RESTAURANT_ID = "restaurantId";
    }
    
    public static class PlaceOrder{
        public static final String SERVICE_NAME = "/placeOrder";
        public static final String PARAM_ITEM_IDS = "itemIds";
        public static final String PARAM_TABLE_ID = "tableId";
        public static final String PARAM_USER_ID = "userId";
        public static final String PARAM_PASSWORD = "password";
        public static final String PARAM_RESTAURANT_ID = "restaurantId";
        public static final String PARAM_ADDRESS = "address";
        public static final String PARAM_COMMENTS = "comments";
        public static final String PARAM_MENU_ITEM_COMMENTS = "menuItemComments";
        public static final String PARAM_BILL_KEY_STRING = "billKeyString";
        public static final String PARAM_QUALIFIERS = "Qualifiers";
        public static final String PARAM_NO_OF_ITEMS = "noOfItems";
    }
    
    public static class BlobServer{
        public static final String SERVICE_NAME = "/serveBlob";
        public static final String PARAM_ID = "id";
    }
    
    //FIXME: This is no longer being used
    public static class GetTables{
        public static final String SERVICE_NAME = "/getTables";
        public static final String ID = "id";
    }
    
    public static class DecodeQR{
        public static final String SERVICE_NAME = "/decodeQR";
        public static final String PARAM_CODE = "code";
    }
    
    public static class RegisterUser{
        public static final String SERVICE_NAME = "/registerUser";
        public static final String PARAM_PHONE_NUMBER = 
                "phoneNumber";
        public static final String PARAM_PASSWORD = 
                "password";
        public static final String PARAM_VALIDATION_CODE = 
                "validationCode";
    }
    
    public static class GetOrders{
    	public static final String SERVICE_NAME = "/getOrders";
    	public static final String PARAM_BILL = "billKeyString";
    	public static final String PARAM_USER_ID = "userId";
    	public static final String PARAM_PASSWORD = "password";
    }
    
    public static class GetBills{
        public static final String SERVICE_NAME = "/getBills";
        public static final String PARAM_USER_ID = "userId";
        public static final String PARAM_PASSWORD = "password";
    }
    
    public static class GetPendingBills{
    	public static final String PARAM_USERNAME = "username";
    	public static final String PARAM_PASSWORD = "password";
    }
    
    public static class NotifyPendingBills{
        public static final String SERVICE_NAME = "/notifyPendingBills";
        public static final String PARAM_RESTAURANT_KEY_STRING = "restKeyString";
    }
    
    public static class BillNotificationEnded{
        public static final String SERVICE_NAME = "/billNotificationEnded";
    }
}

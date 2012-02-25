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
}

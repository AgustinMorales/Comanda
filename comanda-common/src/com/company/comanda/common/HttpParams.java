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
}

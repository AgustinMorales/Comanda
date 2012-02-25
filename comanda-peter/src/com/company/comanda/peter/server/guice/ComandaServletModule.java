package com.company.comanda.peter.server.guice;

import com.company.comanda.common.HttpParams.BlobServer;
import com.company.comanda.common.HttpParams.DecodeQR;
import com.company.comanda.common.HttpParams.GetCategories;
import com.company.comanda.common.HttpParams.GetMenuItems;
import com.company.comanda.common.HttpParams.PlaceOrder;
import com.company.comanda.common.HttpParams.RegisterUser;
import com.company.comanda.common.HttpParams.SearchRestaurants;
import com.company.comanda.peter.server.GUIServiceImpl;
import com.company.comanda.peter.server.GetCategoriesServlet;
import com.company.comanda.peter.server.GetItemsServlet;
import com.company.comanda.peter.server.NewMenuItemServlet;
import com.company.comanda.peter.server.PlaceOrderServlet;
import com.company.comanda.peter.server.QRDecoderServlet;
import com.company.comanda.peter.server.RegisterUserServlet;
import com.company.comanda.peter.server.RestaurantLoginServiceImpl;
import com.company.comanda.peter.server.SearchRestaurantsServlet;
import com.company.comanda.peter.server.ServeBlob;
import com.google.inject.servlet.ServletModule;

class ComandaServletModule extends ServletModule {
  @Override protected void configureServlets() {
    serve("/comanda_peter/greet").with(GUIServiceImpl.class);
    serve("/comanda_peter/login").with(RestaurantLoginServiceImpl.class);
    serve(GetMenuItems.SERVICE_NAME).with(GetItemsServlet.class);
    serve(PlaceOrder.SERVICE_NAME).with(PlaceOrderServlet.class);
    serve("/newMenuItem").with(NewMenuItemServlet.class);
    serve(BlobServer.SERVICE_NAME).with(ServeBlob.class);
    serve(DecodeQR.SERVICE_NAME).with(QRDecoderServlet.class);
    serve(RegisterUser.SERVICE_NAME).with(RegisterUserServlet.class);
    serve(GetCategories.SERVICE_NAME).with(GetCategoriesServlet.class);
    serve(SearchRestaurants.SERVICE_NAME).with(
            SearchRestaurantsServlet.class);
  }
}
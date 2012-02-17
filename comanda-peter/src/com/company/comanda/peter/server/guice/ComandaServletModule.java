package com.company.comanda.peter.server.guice;

import com.company.comanda.peter.server.GetCategoriesServlet;
import com.company.comanda.peter.server.GetItemsServlet;
import com.company.comanda.peter.server.GetTablesServlet;
import com.company.comanda.peter.server.GUIServiceImpl;
import com.company.comanda.peter.server.QRDecoderServlet;
import com.company.comanda.peter.server.RegisterUserServlet;
import com.company.comanda.peter.server.RestaurantLoginServiceImpl;
import com.company.comanda.peter.server.NewMenuItemServlet;
import com.company.comanda.peter.server.PlaceOrderServlet;
import com.company.comanda.peter.server.ServeBlob;
import com.google.inject.servlet.ServletModule;

class ComandaServletModule extends ServletModule {
  @Override protected void configureServlets() {
    serve("/comanda_peter/greet").with(GUIServiceImpl.class);
    serve("/comanda_peter/login").with(RestaurantLoginServiceImpl.class);
    serve("/menuitems").with(GetItemsServlet.class);
    serve("/placeOrder").with(PlaceOrderServlet.class);
    serve("/newMenuItem").with(NewMenuItemServlet.class);
    serve("/serveBlob").with(ServeBlob.class);
    serve("/getTables").with(GetTablesServlet.class);
    serve("/decodeQR").with(QRDecoderServlet.class);
    serve("/registerUser").with(RegisterUserServlet.class);
    serve("/getCategories").with(GetCategoriesServlet.class);
  }
}
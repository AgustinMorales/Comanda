package com.company.comanda.peter.server.guice;

import com.company.comanda.common.HttpParams.BlobServer;
import com.company.comanda.common.HttpParams.DecodeQR;
import com.company.comanda.common.HttpParams.GetBills;
import com.company.comanda.common.HttpParams.GetCategories;
import com.company.comanda.common.HttpParams.GetMenuItems;
import com.company.comanda.common.HttpParams.GetOrders;
import com.company.comanda.common.HttpParams.PlaceOrder;
import com.company.comanda.common.HttpParams.RegisterUser;
import com.company.comanda.common.HttpParams.SearchRestaurants;
import com.company.comanda.common.HttpParams.NotifyPendingBills;
import com.company.comanda.common.HttpParams.BillNotificationEnded;
import com.company.comanda.peter.server.AdminServiceImpl;
import com.company.comanda.peter.server.BillsNotificationEndedServlet;
import com.company.comanda.peter.server.GUIServiceImpl;
import com.company.comanda.peter.server.GetBillTwiMLServlet;
import com.company.comanda.peter.server.GetBillsServlet;
import com.company.comanda.peter.server.GetCategoriesServlet;
import com.company.comanda.peter.server.GetItemsServlet;
import com.company.comanda.peter.server.GetOrdersServlet;
import com.company.comanda.peter.server.GetPendingBillsServlet;
import com.company.comanda.peter.server.NewMenuItemServlet;
import com.company.comanda.peter.server.NewRestaurantServlet;
import com.company.comanda.peter.server.NotifyPendingBillsServlet;
import com.company.comanda.peter.server.PlaceOrderServlet;
import com.company.comanda.peter.server.ProcessNotificationResponseServlet;
import com.company.comanda.peter.server.QRDecoderServlet;
import com.company.comanda.peter.server.RegisterUserServlet;
import com.company.comanda.peter.server.RestaurantLoginServiceImpl;
import com.company.comanda.peter.server.SearchRestaurantsServlet;
import com.company.comanda.peter.server.ServeBlob;
import com.company.comanda.peter.server.maintenance.UpdateModelServlet;
import com.google.inject.servlet.ServletModule;

class ComandaServletModule extends ServletModule {
    @Override protected void configureServlets() {
        serve("/comanda_peter/greet").with(GUIServiceImpl.class);
        serve("/comanda_peter_admin/greet").with(GUIServiceImpl.class);
        serve("/comanda_peter_admin/administration").with(AdminServiceImpl.class);
        serve("/comanda_peter/login").with(RestaurantLoginServiceImpl.class);
        serve(GetMenuItems.SERVICE_NAME).with(GetItemsServlet.class);
        serve(PlaceOrder.SERVICE_NAME).with(PlaceOrderServlet.class);
        serve("/newMenuItem").with(NewMenuItemServlet.class);
        serve("/newRestaurant").with(NewRestaurantServlet.class);
        serve(BlobServer.SERVICE_NAME).with(ServeBlob.class);
        serve(DecodeQR.SERVICE_NAME).with(QRDecoderServlet.class);
        serve(RegisterUser.SERVICE_NAME).with(RegisterUserServlet.class);
        serve(GetCategories.SERVICE_NAME).with(GetCategoriesServlet.class);
        serve(SearchRestaurants.SERVICE_NAME).with(
                SearchRestaurantsServlet.class);
        serve(GetOrders.SERVICE_NAME).with(GetOrdersServlet.class);
        serve(GetBills.SERVICE_NAME).with(GetBillsServlet.class);
        serve("/getNumberOfPendingBills").with(GetPendingBillsServlet.class);
        serve("/updateModel").with(UpdateModelServlet.class);
        serve(NotifyPendingBills.SERVICE_NAME).with(NotifyPendingBillsServlet.class);
        serve(BillNotificationEnded.SERVICE_NAME).with(BillsNotificationEndedServlet.class);
        serve("/getBillTwiML").with(GetBillTwiMLServlet.class);
        serve("/notificationResponse").with(ProcessNotificationResponseServlet.class);
    }
}
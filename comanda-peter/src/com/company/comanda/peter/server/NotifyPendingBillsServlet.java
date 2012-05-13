package com.company.comanda.peter.server;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.common.HttpParams;
import com.company.comanda.peter.server.notification.NotificationManager;

@Singleton
public class NotifyPendingBillsServlet extends HttpServlet {
    
    private static final Logger log = 
            LoggerFactory.getLogger(NotifyPendingBillsServlet.class);
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private NotificationManager notificationManager;
    
    @Inject
    public NotifyPendingBillsServlet(NotificationManager notificationManager){
        this.notificationManager = notificationManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletHelper.logParameters(req, log);
        final String restaurantKeyString = req.getParameter(
                HttpParams.NotifyPendingBills.PARAM_RESTAURANT_KEY_STRING);
        final String billKeyString = req.getParameter(
                HttpParams.NotifyPendingBills.PARAM_BILL_KEY_STRING);
        log.info("Scheduling notification for restaurantKeyString: {}", 
                restaurantKeyString);
        notificationManager.notifyIfNecessary(restaurantKeyString, billKeyString);
    }

    
}

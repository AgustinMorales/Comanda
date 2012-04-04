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

import com.company.comanda.peter.server.notification.NotificationManager;

@Singleton
public class BillsNotificationEndedServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 2L;
    
    private static final Logger log = 
            LoggerFactory.getLogger(BillsNotificationEndedServlet.class);
    
    private NotificationManager notificationManager;

    @Inject
    public BillsNotificationEndedServlet(NotificationManager notificationManager){
        super();
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
        String callDuration = req.getParameter("CallDuration");
        String callStatus = req.getParameter("CallStatus");
        String phone = req.getParameter("To");
        log.info("Callback for call to {}. Duration: {}. Status: {}",
                new Object[]{phone, callDuration, callStatus});
        notificationManager.nofiticationEnded(phone,"completed".equals(callStatus));
    }

    
}

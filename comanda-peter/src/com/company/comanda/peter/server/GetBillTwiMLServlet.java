package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.common.HttpParams.GetBills;
import com.company.comanda.peter.server.model.Bill;
import com.company.comanda.peter.server.model.Order;

import static com.company.comanda.common.XmlHelper.*;
import static com.company.comanda.common.XmlTags.Bills.*;
import static com.company.comanda.peter.server.helper.Twilio.*;

@Singleton
public class GetBillTwiMLServlet extends HttpServlet{

    
    /**
     * 
     */
    private static final long serialVersionUID = -6628492120040209853L;

    private static final Logger log = 
            LoggerFactory.getLogger(GetBillTwiMLServlet.class);

    private RestaurantManager manager;

    @Inject
    public GetBillTwiMLServlet(RestaurantManager manager){
        this.manager = manager;
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
        String callSid = req.getParameter("CallSid");
        String phone = req.getParameter("To");

        PrintWriter out = ServletHelper.getXmlWriter(resp);

        RestaurantAgent agent = manager.getAgent(phone);
        Bill bill = agent.getBill(callSid);
        
        out.println(open(RESPONSE));
        Map<String, String> atts = new HashMap<String, String>(2);
        atts.put("action", "https://comandapeter.appspot.com/notificationResponse");
        atts.put("numDigits", "1");
        out.println(open(GATHER, atts));
        Map<String, String> attsSay = new HashMap<String, String>(2);
        attsSay.put("voice", "woman");
        attsSay.put("language", "es");
        out.println(enclose(SAY, attsSay, "Dirección"));
        out.println(enclose(SAY, attsSay, bill.getAddress()));
        out.println(enclose(PAUSE, ""));
        out.println(enclose(SAY, attsSay, "Teléfono"));
        String phoneString = bill.getPhoneNumber();
        for(int i=0;i<phoneString.length();i++){
            out.println(enclose(SAY, attsSay, "" + phoneString.charAt(i)));
        }
        out.println(enclose(SAY, attsSay, "Elementos del pedido"));
        List<Order> orders = agent.getOrders(null, null, null, bill.getKeyString());
        for(Order order : orders){
            out.println(enclose(SAY, attsSay, order.getMenuItemName()));
            List<String> extras = order.getExtras();
            if(extras.size() > 0){
                out.println(enclose(SAY, attsSay, order.getExtrasName()));
                StringBuilder sb = new StringBuilder();
                final int extrasSize = extras.size();
                for(int i=0;i<extrasSize; i++){
                    sb.append(extras.get(i));
                    if(extrasSize > 1){
                        if(i < extrasSize - 2){
                            sb.append(", ");
                        }
                        else if(i == extrasSize - 2){
                            sb.append(" y ");
                        }
                    }
                }
                out.println(enclose(SAY, attsSay, sb.toString()));
            }
            
        }
        out.println(close(GATHER));
        out.println(enclose(REDIRECT, "https://comandapeter.appspot.com/getBillTwiML"));
        out.println(close(RESPONSE));
        
    }

}

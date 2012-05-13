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

    private UserManager manager;

    @Inject
    public GetBillTwiMLServlet(UserManager manager){
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

        Bill bill = manager.getBill(phone, callSid);
        
        out.println(open(RESPONSE));
        Map<String, String> atts = new HashMap<String, String>(2);
        atts.put("action", "https://comandapeter.appspot.com/notificationResponse");
        atts.put("numDigits", "1");
        out.println(open(GATHER, atts));
        Map<String, String> attsSay = new HashMap<String, String>(2);
        atts.put("voice", "woman");
        atts.put("language", "es");
        out.println(enclose(SAY, attsSay, "Dirección"));
        out.println(enclose(PAUSE, ""));
        out.println(enclose(SAY, attsSay, bill.getAddress()));
        out.println(enclose(PAUSE, ""));
        out.println(enclose(PAUSE, ""));
        out.println(enclose(SAY, attsSay, "Teléfono"));
        out.println(enclose(PAUSE, ""));
        String phoneString = bill.getPhoneNumber();
        for(int i=0;i<phoneString.length();i++){
            out.println(enclose(SAY, attsSay, "" + phoneString.charAt(i)));
        }
        
        out.println(close(RESPONSE));
    }

}

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
import com.company.comanda.peter.shared.BillState;

import static com.company.comanda.common.XmlHelper.*;
import static com.company.comanda.common.XmlTags.Bills.*;
import static com.company.comanda.peter.server.helper.Twilio.*;

@Singleton
public class ProcessNotificationResponseServlet extends HttpServlet{


    /**
     * 
     */
    private static final long serialVersionUID = -6628492120040209853L;

    private static final int DEFAULT_DELIVERY_DELAY = 30;

    private static final Logger log = 
            LoggerFactory.getLogger(ProcessNotificationResponseServlet.class);

    private RestaurantManager manager;
    private GetBillTwiMLServlet getBillTwiMLServlet;

    @Inject
    public ProcessNotificationResponseServlet(RestaurantManager manager, 
            GetBillTwiMLServlet getBillTwiMLServlet){
        this.manager = manager;
        this.getBillTwiMLServlet = getBillTwiMLServlet;
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
        String digits = req.getParameter("Digits");

        RestaurantAgent agent = manager.getAgent(phone);

        Bill bill = agent.getBill(callSid);

        BillState newState = null;

        if("2".equals(digits)){
            getBillTwiMLServlet.doPost(req, resp);
        }
        else{
            if("3".equals(digits)){
                newState = BillState.DELIVERED;
            }
            else if("4".equals(digits)){
                newState = BillState.REJECTED_NO_DELIVERY_THERE;
            }
            else if("5".equals(digits)){
                newState = BillState.REJECTED_OUT_OF_SOMETHING;
            }
            else if("6".equals(digits)){
                newState = BillState.REJECTED_OFF_DUTY;
            }
            else if("7".equals(digits)){
                newState = BillState.REJECTED_UNKNOWN_ADDRESS;
            }
            else if("8".equals(digits)){
                newState = BillState.REJECTED;
            }
            agent.changeBillState(bill.getKeyString(), newState, DEFAULT_DELIVERY_DELAY);

            PrintWriter out = ServletHelper.getXmlWriter(resp);


            out.println(open(RESPONSE));
            Map<String, String> attsSay = new HashMap<String, String>(2);
            attsSay.put("voice", "woman");
            attsSay.put("language", "es");
            if(newState == BillState.DELIVERED){
                out.println(enclose(SAY, attsSay, "El pedido ha sido aceptado"));
            }
            else{
                out.println(enclose(SAY, attsSay, "El pedido ha sido rechazado"));
            }
            out.println(enclose(HANGUP, ""));
            out.println(close(RESPONSE));
        }
    }

}

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

    private static final int REPEAT = 1;

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

        StringBuilder sb = new StringBuilder();
        String address = bill.getAddress().replaceAll("(?<=[0-9])(?=[A-Za-z])", ", ");
        address = address.replaceAll("(?<=[0-9]) (?=[A-Za-z])", ", ");
        //Phonetic alphabet
        address = address.replaceAll(", \\bb\\b", ", b, de burro");
        address = address.replaceAll(", \\bc\\b", ", c, de casa");
        address = address.replaceAll(", \\bd\\b", ", d, de dromedario");
        sb.append("Dirección: ");
        sb.append(address);
        sb.append(". ");
        sb.append("Repito dirección: ");
        sb.append(address);
        sb.append(". ");
        sb.append("Teléfono: ");
        String phoneString = bill.getPhoneNumber();
        StringBuilder phoneSb = new StringBuilder();
        for(int i=0;i<phoneString.length();i++){
            phoneSb.append(phoneString.charAt(i));
            if(i<phoneString.length() - 1){
                phoneSb.append(", ");
            }
        }
        sb.append(phoneSb);
        sb.append(".");
        out.println(enclose(SAY, attsSay, sb.toString()));
        
        sb = new StringBuilder();
        sb.append("Elementos del pedido: ");
        List<Order> orders = agent.getOrders(null, null, null, bill.getKeyString());
        for(Order order : orders){
            if(order.getNoOfItems() > 1){
                sb.append(order.getNoOfItems());
                sb.append(" unidades de ");
            }
            else{
                sb.append(" una unidad de ");
            }
            sb.append(order.getMenuItemName());
            sb.append(". siguiente elemento, ");
            List<String> extras = order.getExtras();
            if(extras != null && extras.size() > 0){
                sb.append(" con ");
                sb.append(order.getExtrasName());
                sb.append(": ");
                StringBuilder sbExtras = new StringBuilder();
                final int extrasSize = extras.size();
                for(int i=0;i<extrasSize; i++){
                    sbExtras.append(extras.get(i));
                    if(extrasSize > 1){
                        if(i < extrasSize - 2){
                            sbExtras.append(", ");
                        }
                        else if(i == extrasSize - 2){
                            sbExtras.append(" y ");
                        }
                    }
                }
                sb.append(sbExtras);
            }
            sb.append(".");
        }
        out.println(enclose(SAY, attsSay, sb.toString()));
        sb = new StringBuilder();
        for(int i=0;i<REPEAT;i++){
            sb.append("Para volver a escuchar el pedido, pulse 2. ");
            sb.append("Para aceptar el pedido, pulse 3. ");
            sb.append("Para rechazar por fuera de zona de reparto, pulse 4. ");
            sb.append("Si quiere rechazar el pedido por falta de existencias, pulse 5. ");
            sb.append("Para rechazar el pedido por causas de horario, pulse 6. ");
            sb.append("Si desea rechazar el pedido por dirección incorrecta, pulse 7. ");
            sb.append("Para rechazar el pedido por otras causas, pulse 8.");
        }
        out.println(enclose(SAY, attsSay, sb.toString()));
        out.println(close(GATHER));
        out.println(enclose(REDIRECT, "https://comandapeter.appspot.com/getBillTwiML"));
        out.println(close(RESPONSE));

    }

}

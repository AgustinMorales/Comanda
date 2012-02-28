package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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

@Singleton
public class GetBillsServlet extends HttpServlet{

    /**
     * 
     */
    private static final long serialVersionUID = -6628492120040209853L;
    
    private static final Logger log = 
            LoggerFactory.getLogger(GetBillsServlet.class);
    
    private UserManager manager;
    
    @Inject
    public GetBillsServlet(UserManager manager){
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
        long userId = Long.parseLong(req.getParameter(GetBills.PARAM_USER_ID));
        String password = req.getParameter(GetBills.PARAM_PASSWORD);
        
        List<Bill> bills = manager.getBills(userId, password);
        
        PrintWriter out = ServletHelper.getXmlWriter(resp);
        
        out.println(open(BILL_LIST));
        for(Bill bill : bills){
            out.println(open(BILL));
            out.println(enclose(KEY_STRING, bill.getKeyString()));
            out.println(enclose(TOTAL_AMOUNT, "0"));
            out.println(enclose(STATE,bill.getState().toString()));
            out.println(enclose(TYPE, bill.getType().toString()));
            out.println(enclose(TABLE_NAME, bill.getTableName()));
            out.println(enclose(ADDRESS, bill.getAddress()));
            out.println(enclose(RESTAURANT_NAME, bill.getRestaurantName()));
            out.println(enclose(COMMENTS, bill.getComments()));
            
            out.println(close(BILL));
        }
        out.println(close(BILL_LIST));
    }

}

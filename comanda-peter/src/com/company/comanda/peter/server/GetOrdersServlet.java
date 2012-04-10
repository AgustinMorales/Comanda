package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.company.comanda.common.XmlHelper.*;
import static com.company.comanda.common.XmlTags.Orders.*;
import com.company.comanda.common.HttpParams.GetOrders;
import com.company.comanda.peter.server.helper.ServerFormatter;
import com.company.comanda.peter.server.model.Order;

@Singleton
public class GetOrdersServlet extends HttpServlet{
    
    /**
     * 
     */
    private static final long serialVersionUID = -3850715977073789224L;

    private static final Logger log = 
    		LoggerFactory.getLogger(GetOrdersServlet.class);
    
    private UserManager manager;
    
    @Inject
    public GetOrdersServlet(UserManager manager){
    	this.manager = manager;
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        doPost(req, resp);
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        ServletHelper.logParameters(req, log);
        String billKeyString = req.getParameter(GetOrders.PARAM_BILL);
        long userId = Long.parseLong(req.getParameter(GetOrders.PARAM_USER_ID));
        String password = req.getParameter(GetOrders.PARAM_PASSWORD);
        
        List<Order> orders = manager.getOrders(userId, password, billKeyString);
        
        PrintWriter out = ServletHelper.getXmlWriter(resp);
        
        out.println(open(ORDER_LIST));
        for(Order order:orders){
            
        	out.println(open(ORDER));
        	out.println(enclose(KEY_STRING,order.getKeyString()));
        	out.println(enclose(MENU_ITEM_NAME, order.getMenuItemName() + 
        	        ServerFormatter.getExtras(order)));
        	out.println(enclose(MENU_ITEM_NUMBER, "" + order.getNoOfItems()));
        	out.println(enclose(PRICE, "" + order.getTotalPrice()));
        	out.println(enclose(COMMENTS, order.getComments()));
        	out.println(close(ORDER));
        }
        out.println(close(ORDER_LIST));
    }
    
}

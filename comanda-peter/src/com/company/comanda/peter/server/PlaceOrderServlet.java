package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.peter.shared.BillType;

import static com.company.comanda.common.HttpParams.PlaceOrder.*;
import static com.company.comanda.common.XmlHelper.*;
import static com.company.comanda.common.XmlTags.BooleanResult.*;

@Singleton
public class PlaceOrderServlet extends HttpServlet  
{

    /**
     * 
     */
    private static final long serialVersionUID = 7406683513326724866L;

    private static final Logger log = LoggerFactory.getLogger(PlaceOrderServlet.class);

    private UserManager userManager;

    @Inject
    public PlaceOrderServlet(UserManager manager){
        this.userManager = manager;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        PrintWriter out = ServletHelper.getXmlWriter(resp);
        try{
            ServletHelper.logParameters(req, log);
            String menuItemIdsString = req.getParameter(PARAM_ITEM_IDS);
            String tableIdString = req.getParameter(PARAM_TABLE_ID);
            String address = req.getParameter(PARAM_ADDRESS);
            //        String menuItemComments = req.getParameter(PARAM_MENU_ITEM_COMMENTS);
            String billKeyString = req.getParameter(PARAM_BILL_KEY_STRING);
            String qualifierIndexesString = req.getParameter(PARAM_QUALIFIERS);
            String allNoOfItemsString = req.getParameter(PARAM_NO_OF_ITEMS);
            String comments = req.getParameter(PARAM_COMMENTS);
            Long tableId = null;
            if(tableIdString.length() > 0){
                tableId = Long.parseLong(req.getParameter(PARAM_TABLE_ID));
            }
            long userId = Long.parseLong(req.getParameter(PARAM_USER_ID));
            String password = req.getParameter(PARAM_PASSWORD);
            long restaurantId = Long.parseLong(
                    req.getParameter(PARAM_RESTAURANT_ID));

            //FIXME: What happens in case of error?
            String[] items = menuItemIdsString.split(":");
            String[] indexes = qualifierIndexesString.split(":");
            String[] noOfItemsStrings = allNoOfItemsString.split(":");
            List<Long> menuItemIds = new ArrayList<Long>(items.length);
            List<String> menuItemComments = new ArrayList<String>(items.length);
            List<Integer> qualifierIndexes = new ArrayList<Integer>(items.length);
            List<Integer> noOfItems = new ArrayList<Integer>(items.length);
            for (int i=0;i<items.length;i++){
                String item = items[i];
                String qualifierIndex = indexes[i];
                String noOfItemsString = noOfItemsStrings[i];
                log.debug("Adding item ID: {}, qualifierIndex: {}, noOfItems: {}", 
                        new Object[]{item, qualifierIndex, noOfItemsString});
                long menuItemId = Long.parseLong(item);
                menuItemIds.add(menuItemId);
                menuItemComments.add("");
                qualifierIndexes.add(Integer.parseInt(qualifierIndex));
                noOfItems.add(Integer.parseInt(noOfItemsString));
            }
            //FIXME: User real qualifierIndex
            userManager.placeOrder(userId, password, 
                    restaurantId, menuItemIds, 
                    qualifierIndexes,
                    noOfItems,
                    menuItemComments,
                    address,
                    tableId,
                    comments,
                    tableId==null?BillType.DELIVERY:BillType.IN_RESTAURANT,
                            billKeyString);
            out.println(enclose(RESULT, "" + true));
        }
        catch(Exception e){
            log.error("Exception while placing order", e);
            out.println(enclose(RESULT, "" + false));
        }
    }
}

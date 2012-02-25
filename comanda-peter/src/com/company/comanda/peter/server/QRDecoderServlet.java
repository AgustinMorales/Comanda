package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.common.HttpParams.DecodeQR;
import com.company.comanda.peter.server.UserManager.CodifiedData;
import com.googlecode.objectify.NotFoundException;


import static com.company.comanda.common.XmlTags.RestaurantAndTableData.*;
import static com.company.comanda.common.XmlHelper.*;
@Singleton
public class QRDecoderServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -1498697910467918329L;
    private UserManager userManager;
    
    private static final Logger log = LoggerFactory.getLogger(QRDecoderServlet.class);
    
    
    @Inject
    public QRDecoderServlet(UserManager userManager){
        this.userManager = userManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Parameters: {}", req.getParameterMap());
        final String code = (String)req.getParameter(DecodeQR.PARAM_CODE);
        PrintWriter out = ServletHelper.getXmlWriter(resp);
        try{
            CodifiedData data = userManager.getData(code);
            if (data != null &&
                    data.restaurant != null &&
                    data.table != null){ 
                out.println(open(DATA));
                out.println(open(RESTAURANT));
                out.println(enclose(NAME, data.restaurant.getName()));
                out.println(enclose(ID, "" + data.restaurant.getId()));
                out.println(close(RESTAURANT));
                out.println(open(TABLE));
                out.println(enclose(NAME, data.table.getName()));
                out.println(enclose(ID, "" + data.table.getId()));
                out.println(close(TABLE));
                out.println(close(DATA));
            }
            else{
                out.println("error");
            }
        }
        catch(NotFoundException e){
            out.println("error");
        }
        
        out.flush();
    }

    
}

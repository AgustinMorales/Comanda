package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.company.comanda.peter.server.UserManager.CodifiedData;
import com.googlecode.objectify.NotFoundException;

@Singleton
public class QRDecoderServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -1498697910467918329L;
    private UserManager userManager;
    
    
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
        final String code = (String)req.getAttribute("code");
        PrintWriter out = ServletHelper.getXmlWriter(resp);
        try{
            CodifiedData data = userManager.getData(code);
            if (data != null &&
                    data.restaurant != null &&
                    data.table != null){ 
                out.println("<Data>");
                out.println("\t<Restaurant>");
                out.println("\t\t<Name>" + data.restaurant.getName() + "</Name>");
                out.println("\t\t<Id>" + data.restaurant.getId() + "</Id>");
                out.println("\t</Restaurant>");
                out.println("\t<Table>");
                out.println("\t\t<Name>" + data.table.getName() + "</Name>");
                out.println("\t\t<Id>" + data.table.getId() + "</Id>");
                out.println("\t</Table>");
                out.println("</Data>");
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

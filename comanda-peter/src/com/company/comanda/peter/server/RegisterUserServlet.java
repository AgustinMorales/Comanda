package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class RegisterUserServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 28454201636922480L;
    
    private static final String PARAM_PHONE_NUMBER = 
            "phoneNumber";
    private static final String PARAM_PASSWORD = 
            "password";
    private static final String PARAM_VALIDATION_CODE = 
            "validationCode";
    
    
    private UserManager userManager;
    
    @Inject
    public RegisterUserServlet(UserManager userManager){
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
        String phoneNumber = req.getParameter(PARAM_PHONE_NUMBER);
        String password = req.getParameter(PARAM_PASSWORD);
        String validationCode = req.getParameter(PARAM_VALIDATION_CODE);
        
        if(phoneNumber == null ||
                password == null ||
                validationCode == null ||
                phoneNumber.length() == 0 ||
                password.length() == 0 ||
                validationCode.length() == 0){
            throw new IllegalArgumentException();
        }
        long userId = userManager.registerUser(phoneNumber, password, validationCode);
        
        PrintWriter out = ServletHelper.getXmlWriter(resp);
        out.println("<User>");
        out.println("\t<Id>" + userId + "</Id>");
        out.println("</User>");
        out.flush();
        
    }

    
}

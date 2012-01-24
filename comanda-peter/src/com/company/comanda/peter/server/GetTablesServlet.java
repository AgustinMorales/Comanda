package com.company.comanda.peter.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetTablesServlet extends HttpServlet{

    /**
     * 
     */
    private static final long serialVersionUID = 3367083994563166353L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("<TableList>");
        String[] tables = new String[]{
          "1",
          "4",
          "5",
          "6",
          "8",
        };
        //loop through items list and print each item
        for (String i : tables) 
        {
            out.println("\n\t<Table>" + i + "</Table>");
        }
        out.println("\n</TableList>");
        // Flush writer
        out.flush();
    }

}

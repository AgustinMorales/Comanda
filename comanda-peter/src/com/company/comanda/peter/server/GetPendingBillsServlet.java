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

import com.company.comanda.peter.shared.BillState;
import com.company.comanda.peter.shared.BillType;

@Singleton
public class GetPendingBillsServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(GetPendingBillsServlet.class);
	private RestaurantManager manager;
	
	
	@Inject
	public GetPendingBillsServlet(RestaurantManager manager){
		super();
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
		
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		RestaurantAgent agent = manager.getAgent(username, password);
		
		PrintWriter out = resp.getWriter();
		
		int noOfBills = agent.getBills(BillState.OPEN, BillType.DELIVERY).size();
		out.println("" + noOfBills);
		
	}

}

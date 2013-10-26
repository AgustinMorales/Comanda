package com.company.comanda.quagmire.stubs;

import javax.inject.Inject;

import com.company.comanda.quagmire.ServerConnector;

public class ConnectorStub implements ServerConnector {

	private int counter;
	
	@Inject
	public ConnectorStub(){
		
	}
	
	public String getData(String username, String password) {
		String result;
		if(counter > 0){
			result = "" + 0;
		}
		else{
			result = "" + 1;
		}
		counter++;
		return result;
	}

}

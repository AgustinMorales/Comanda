package com.company.comanda.quagmire;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerConnectorImpl implements ServerConnector {

	private static final Logger log = 
			LoggerFactory.getLogger(ServerConnectorImpl.class);
	private static final String SERVER_URL = "http://pgmdevelopment.appspot.com/getPendingBills?";
	
	@Inject
	public ServerConnectorImpl(){
		
	}

	public String getData(String username, String password) {
		String body = null;
		try{
			URL url = new URL(SERVER_URL + "username=" + username + "&password=" + password);
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			body = IOUtils.toString(in, encoding);
		}
		catch(MalformedURLException e){
			log.error("Wrong URL while getting pending bills", e);
		} catch (IOException e) {
			log.error("IO exception while getting pending bills", e);
		}
		return body;
	}

}

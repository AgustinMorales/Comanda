package com.company.comanda.quagmire;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.quagmire.stubs.ConnectorStub;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class TestGUI {

	private static final Logger log = LoggerFactory.getLogger(TestGUI.class);
	
	
	@Test
	public void testManual(){
		
		Injector injector = Guice.createInjector(new AbstractModule() {
			
			@Override
			protected void configure() {
				bind(ServerConnector.class).to(ConnectorStub.class);
				bind(QuagmireUI.class).to(QuagmmireUIImpl.class);
				install(new FactoryModuleBuilder()
		        .implement(BillsCheckerImpl.class, BillsCheckerImpl.class)
		        .build(BillsCheckerFactory.class));
			}
		});
		BillsCheckerFactory factory = injector.getInstance(BillsCheckerFactory.class);
		
		BillsCheckerImpl checker = factory.create("", "");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("First check...");
		checker.doCheck();
		
		log.debug("Second check...");
		checker.doCheck();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

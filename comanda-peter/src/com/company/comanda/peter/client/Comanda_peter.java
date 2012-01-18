package com.company.comanda.peter.client;

import java.util.List;

import com.company.comanda.peter.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Comanda_peter implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Send to server");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel rootPanel = RootPanel.get("nameFieldContainer");
		rootPanel.add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		
		VerticalPanel panel = new VerticalPanel();
		CellTable<String> cellTable = new CellTable<String>();
		rootPanel.add(panel);
		panel.add(cellTable);
		cellTable.setSize("213px", "150px");

		// Add a text column to show the name.
	    TextColumn<String> nameColumn = new TextColumn<String>() {
	      @Override
	      public String getValue(String object) {
	        return object;
	      }
	    };
	    cellTable.addColumn(nameColumn, "Name");
		
	    AsyncDataProvider<String> provider = new AsyncDataProvider<String>() {
	        @Override
	        protected void onRangeChanged(HasData<String> display) {
	          final int start = display.getVisibleRange().getStart();
	          int length = display.getVisibleRange().getLength();
	          AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
	              @Override
	              public void onFailure(Throwable caught) {
	                Window.alert(caught.getMessage());
	              }
	              @Override
	              public void onSuccess(List<String> result) {
	                updateRowData(start, result);
	              }
	            };
	            // The remote service that should be implemented
	            greetingService.getMenuItemNames(start, length, callback);
	        }
	      };
	      provider.addDataDisplay(cellTable);
	      provider.updateRowCount(200, false);
	      AsyncHandler columnSortHandler = new AsyncHandler(cellTable);
	      
	      final TextBox textBox = new TextBox();
	      rootPanel.add(textBox, 10, 236);
	      
	      final Button btnPlaceOrder = new Button("Place order");
	      rootPanel.add(btnPlaceOrder, 197, 236);
	      cellTable.addColumnSortHandler(columnSortHandler);
	    
		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				btnPlaceOrder.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		abstract class MyHandler implements ClickHandler, KeyUpHandler {
			
			
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}
			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}
			
			protected abstract void sendNameToServer();
			
		}

		
		class AddMenuItemHandler extends MyHandler {
			

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			protected void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer,
						new AsyncCallback<Void>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(Void result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML("Item added");
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		
		class PlaceOrderHandler extends MyHandler {
			

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			protected void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = textBox.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				btnPlaceOrder.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.placeOrder(textToServer,
						new AsyncCallback<Void>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(Void result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML("Order placed");
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler newMenuItemHandler = new AddMenuItemHandler();
		sendButton.addClickHandler(newMenuItemHandler);
		nameField.addKeyUpHandler(newMenuItemHandler);
		
		MyHandler placeOrderHandler = new PlaceOrderHandler();
		btnPlaceOrder.addClickHandler(placeOrderHandler);
		textBox.addKeyUpHandler(placeOrderHandler);
		
		
		
		
		
		
		
		
		
		
		
		final CellTable<String> ordersTable = new CellTable<String>();
		panel.add(ordersTable);
		ordersTable.setSize("213px", "150px");

		// Add a text column to show the name.
	    TextColumn<String> orderNameColumn = new TextColumn<String>() {
	      @Override
	      public String getValue(String object) {
	        return object;
	      }
	    };
	    ordersTable.addColumn(orderNameColumn, "OrderName");
		
	    final AsyncDataProvider<String> ordersProvider = new AsyncDataProvider<String>() {
	        @Override
	        protected void onRangeChanged(HasData<String> display) {
	          final int start = display.getVisibleRange().getStart();
	          int length = display.getVisibleRange().getLength();
	          AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
	              @Override
	              public void onFailure(Throwable caught) {
	                Window.alert(caught.getMessage());
	              }
	              @Override
	              public void onSuccess(List<String> result) {
	                updateRowData(start, result);
	              }
	            };
	            // The remote service that should be implemented
	            greetingService.getOrders(start, length, callback);
	        }
	      };
	      ordersProvider.addDataDisplay(ordersTable);
	      ordersProvider.updateRowCount(200, false);
	      AsyncHandler ordersColumnSortHandler = new AsyncHandler(ordersTable);
	      
	      class MyTimer extends Timer{
	    	  
	    	  public void run(){
	    		  Range range = ordersTable.getVisibleRange();
	    		  RangeChangeEvent.fire(ordersTable, range);
	    	  }
	      }
	      
	      Timer timer = new MyTimer();
	      
	     timer.scheduleRepeating(3000);
	}
}

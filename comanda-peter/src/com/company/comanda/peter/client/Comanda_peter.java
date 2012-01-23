package com.company.comanda.peter.client;

import java.util.List;

import com.company.comanda.peter.shared.FieldVerifier;
import com.company.comanda.peter.shared.PagedResult;
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
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
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
        final Button addMenuItemButton = new Button("Add menu item");
        final TextBox newMenuItemNameTextField = new TextBox();
        newMenuItemNameTextField.setText("GWT User");
        newMenuItemNameTextField.setName("itemName");
        final Label errorLabel = new Label();

        // We can add style names to widgets
        addMenuItemButton.addStyleName("sendButton");

        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        RootPanel rootPanel = RootPanel.get("mainDiv");
        VerticalPanel mainVerticalPanel = new VerticalPanel();
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        mainVerticalPanel.add(horizontalPanel);
        VerticalPanel firstVerticalPanel = new VerticalPanel();
        VerticalPanel secondVerticalPanel = new VerticalPanel();
        horizontalPanel.add(firstVerticalPanel);
        horizontalPanel.add(secondVerticalPanel);
        rootPanel.add(mainVerticalPanel);

        final FormPanel addMenuItemFormPanel = new FormPanel();
        addMenuItemFormPanel.setAction("/newMenuItem");

        // Because we're going to add a FileUpload widget, we'll need to set the
        // form to use the POST method, and multipart MIME encoding.
        addMenuItemFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        addMenuItemFormPanel.setMethod(FormPanel.METHOD_POST);
        
        FileUpload menuItemImageFile = new FileUpload();
        menuItemImageFile.setName("itemImage");
        
        VerticalPanel newMenuItemElementsPanel = new VerticalPanel();
        HorizontalPanel menuItemImageHP = new HorizontalPanel();
        menuItemImageHP.add(new Label("Image file: "));
        menuItemImageHP.add(menuItemImageFile);
        addMenuItemFormPanel.setWidget(newMenuItemElementsPanel);
        newMenuItemElementsPanel.add(newMenuItemNameTextField);
        newMenuItemElementsPanel.add(menuItemImageHP);
        newMenuItemElementsPanel.add(addMenuItemButton);
        newMenuItemElementsPanel.add(errorLabel);
        
        

        firstVerticalPanel.add(addMenuItemFormPanel);
        // Focus the cursor on the name field when the app loads
        newMenuItemNameTextField.setFocus(true);

        
        CellTable<String> cellTable = new CellTable<String>();
        firstVerticalPanel.add(cellTable);
        cellTable.setSize("213px", "300px");

        SimplePager menuItemsPager = new SimplePager();
        menuItemsPager.setDisplay(cellTable);
        firstVerticalPanel.add(menuItemsPager);
        menuItemsPager.setPageSize(8);
        
        // Add a text column to show the name.
        TextColumn<String> nameColumn = new TextColumn<String>() {
            @Override
            public String getValue(String object) {
                return object;
            }
        };
        cellTable.addColumn(nameColumn, "Menu items");

        AsyncDataProvider<String> provider = new AsyncDataProvider<String>() {
            @Override
            protected void onRangeChanged(HasData<String> display) {
                final int start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                AsyncCallback<PagedResult<String>> callback = new AsyncCallback<PagedResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }
                    @Override
                    public void onSuccess(PagedResult<String> result) {
                        updateRowData(start, result.getList());
                        updateRowCount(result.getTotal(), true);
                    }
                };
                // The remote service that should be implemented
                greetingService.getMenuItemNames(start, length, callback);
            }
        };
        provider.addDataDisplay(cellTable);
        AsyncHandler columnSortHandler = new AsyncHandler(cellTable);

        final TextBox textBox = new TextBox();
        secondVerticalPanel.add(textBox);

        final Button btnPlaceOrder = new Button("Place order");
        secondVerticalPanel.add(btnPlaceOrder);
        cellTable.addColumnSortHandler(columnSortHandler);
        
        btnPlaceOrder.addStyleName("sendButton");


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
                addMenuItemButton.setEnabled(true);
                btnPlaceOrder.setEnabled(true);
                addMenuItemButton.setFocus(true);
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

        addMenuItemFormPanel.addSubmitHandler(new SubmitHandler() {
            
            @Override
            public void onSubmit(SubmitEvent event) {
             // First, we validate the input.
                errorLabel.setText("");
                String textToServer = newMenuItemNameTextField.getText();
                if (!FieldVerifier.isValidName(textToServer)) {
                    errorLabel.setText("Please enter at least four characters");
                    event.cancel();
                }
                else{
                 // Then, we send the input to the server.
                    addMenuItemButton.setEnabled(false);
                    textToServerLabel.setText(textToServer);
                    serverResponseLabel.setText("");
                }
                
            }
        });

        addMenuItemFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {
            
            @Override
            public void onSubmitComplete(SubmitCompleteEvent event) {
//                if(event.getResults().equals("SUCCESS")){
//                    dialogBox.setText("Form submission");
//                    serverResponseLabel
//                    .removeStyleName("serverResponseLabelError");
//                    serverResponseLabel.setHTML("Item added");
//                    dialogBox.center();
//                    closeButton.setFocus(true);
//                }
//                else{
//                    dialogBox
//                    .setText("Form submission - Failure");
//                    serverResponseLabel
//                    .addStyleName("serverResponseLabelError");
//                    serverResponseLabel.setHTML(SERVER_ERROR);
//                    dialogBox.center();
//                    closeButton.setFocus(true);
//                }
                
            }
        });
        
        class AddMenuItemHandler implements ClickHandler {

            @Override
            public void onClick(ClickEvent event) {
                greetingService.getUploadUrl(new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Error while getting upload URL");
                        
                    }

                    @Override
                    public void onSuccess(String result) {
                        addMenuItemFormPanel.setAction(result);
                        addMenuItemFormPanel.submit();
                        
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
        addMenuItemButton.addClickHandler(new AddMenuItemHandler());

        MyHandler placeOrderHandler = new PlaceOrderHandler();
        btnPlaceOrder.addClickHandler(placeOrderHandler);
        textBox.addKeyUpHandler(placeOrderHandler);











        final CellTable<String> ordersTable = new CellTable<String>();
        secondVerticalPanel.add(ordersTable);
        ordersTable.setSize("213px", "300px");

        SimplePager ordersPager = new SimplePager();
        ordersPager.setDisplay(ordersTable);
        secondVerticalPanel.add(ordersPager);
        ordersPager.setPageSize(8);

        // Add a text column to show the name.
        TextColumn<String> orderNameColumn = new TextColumn<String>() {
            @Override
            public String getValue(String object) {
                return object;
            }
        };
        ordersTable.addColumn(orderNameColumn, "Pending orders");

        final AsyncDataProvider<String> ordersProvider = new AsyncDataProvider<String>() {
            @Override
            protected void onRangeChanged(HasData<String> display) {
                final int start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                AsyncCallback<PagedResult<String>> callback = new AsyncCallback<PagedResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }
                    @Override
                    public void onSuccess(PagedResult<String> result) {
                        updateRowData(start, result.getList());
                        updateRowCount(result.getTotal(), true);
                    }
                };
                // The remote service that should be implemented
                greetingService.getOrders(start, length, callback);
            }
        };
        ordersProvider.addDataDisplay(ordersTable);
        AsyncHandler ordersColumnSortHandler = new AsyncHandler(ordersTable);

        class MyTimer extends Timer{

            public void run(){
                Range range = ordersTable.getVisibleRange();
                RangeChangeEvent.fire(ordersTable, range);
            }
        }

        Timer timer = new MyTimer();

        timer.scheduleRepeating(3000);
        
        
        
     
        
        
        
        
        
        
        
        
        
        
     // Create a FormPanel and point it at a service.
        final FormPanel form = new FormPanel();
        form.setAction("/newMenuItem");

        // Because we're going to add a FileUpload widget, we'll need to set the
        // form to use the POST method, and multipart MIME encoding.
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);

        // Create a panel to hold all of the form widgets.
        VerticalPanel panel = new VerticalPanel();
        form.setWidget(panel);

        // Create a TextBox, giving it a name so that it will be submitted.
        final TextBox tb = new TextBox();
        tb.setName("itemName");
        panel.add(tb);

        // Create a ListBox, giving it a name and some values to be associated with
        // its options.
        ListBox lb = new ListBox();
        lb.setName("listBoxFormElement");
        lb.addItem("foo", "fooValue");
        lb.addItem("bar", "barValue");
        lb.addItem("baz", "bazValue");
        panel.add(lb);

        // Create a FileUpload widget.
        FileUpload upload = new FileUpload();
        upload.setName("uploadFormElement");
        panel.add(upload);

        // Add a 'submit' button.
        panel.add(new Button("Submit", new ClickHandler() {
          public void onClick(ClickEvent event) {
              greetingService.getUploadUrl(new AsyncCallback<String>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("Error while getting upload URL");
                    
                }

                @Override
                public void onSuccess(String result) {
                    form.setAction(result);
                    form.submit();
                    
                }
            });
            
          }
        }));

        // Add an event handler to the form.
        form.addSubmitHandler(new FormPanel.SubmitHandler() {
          public void onSubmit(SubmitEvent event) {
            // This event is fired just before the form is submitted. We can take
            // this opportunity to perform validation.
            if (tb.getText().length() == 0) {
              Window.alert("The text box must not be empty");
              event.cancel();
            }
          }
        });
        form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
          public void onSubmitComplete(SubmitCompleteEvent event) {
            // When the form submission is successfully completed, this event is
            // fired. Assuming the service returned a response of type text/html,
            // we can get the result text here (see the FormPanel documentation for
            // further explanation).
            Window.alert(event.getResults());
          }
        });
        
        horizontalPanel.add(form);
    }
}

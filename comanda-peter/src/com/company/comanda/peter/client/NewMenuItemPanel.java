package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.FieldVerifier;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.IntegerBox;

public class NewMenuItemPanel extends VerticalPanel {

    private NewMenuItemHandler newMenuItemHandler;
    
    private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);
    
    public NewMenuItemPanel(){
        final Button addMenuItemButton = new Button("Add menu item");
        final Label errorLabel = new Label();

        // We can add style names to widgets
        addMenuItemButton.addStyleName("sendButton");

        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element

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
        
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        newMenuItemElementsPanel.add(horizontalPanel);
        
        Label lblItemName = new Label("Item name");
        horizontalPanel.add(lblItemName);
        final TextBox newMenuItemNameTextField = new TextBox();
        horizontalPanel.add(newMenuItemNameTextField);
        newMenuItemNameTextField.setText("GWT User");
        newMenuItemNameTextField.setName("itemName");
        // Focus the cursor on the name field when the app loads
        newMenuItemNameTextField.setFocus(true);
        
        HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
        newMenuItemElementsPanel.add(horizontalPanel_1);
        
        Label lblItemPrice = new Label("Item price (SIN DECIMALES!!)");
        horizontalPanel_1.add(lblItemPrice);
        
        IntegerBox priceBox = new IntegerBox();
        priceBox.setName("price");
        horizontalPanel_1.add(priceBox);
        newMenuItemElementsPanel.add(menuItemImageHP);
        
        HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
        horizontalPanel_2.setSpacing(10);
        newMenuItemElementsPanel.add(horizontalPanel_2);
        horizontalPanel_2.add(addMenuItemButton);
        
        Button btnCancel = new Button("Cancel");
        btnCancel.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                newMenuItemHandler.onCancel();
            }
        });
        horizontalPanel_2.add(btnCancel);
        newMenuItemElementsPanel.add(errorLabel);
        
        btnCancel.addStyleName("sendButton");

        add(addMenuItemFormPanel);



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
            }
        });

        addMenuItemFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {
            
            @Override
            public void onSubmitComplete(SubmitCompleteEvent event) {
                addMenuItemFormPanel.reset();
                newMenuItemHandler.onNewMenuItem();
                
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


        

        // Add a handler to send the name to the server
        addMenuItemButton.addClickHandler(new AddMenuItemHandler());
    }
    
    public interface NewMenuItemHandler{
        void onNewMenuItem();
        void onCancel();
    }
    
    public void setNewMenuItemHandler(NewMenuItemHandler handler){
        this.newMenuItemHandler = handler;
    }
    
}

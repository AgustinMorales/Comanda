package com.company.comanda.peter.client;

import com.company.comanda.peter.shared.FieldVerifier;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class EditMenuPanel extends VerticalPanel {

    private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);
    
    public EditMenuPanel(){
        final Button addMenuItemButton = new Button("Add menu item");
        final TextBox newMenuItemNameTextField = new TextBox();
        newMenuItemNameTextField.setText("GWT User");
        newMenuItemNameTextField.setName("itemName");
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
        newMenuItemElementsPanel.add(newMenuItemNameTextField);
        newMenuItemElementsPanel.add(menuItemImageHP);
        newMenuItemElementsPanel.add(addMenuItemButton);
        newMenuItemElementsPanel.add(errorLabel);
        
        

        add(addMenuItemFormPanel);
        // Focus the cursor on the name field when the app loads
        newMenuItemNameTextField.setFocus(true);

        
        CellTable<String> cellTable = new CellTable<String>();
        add(cellTable);
        cellTable.setSize("213px", "300px");

        SimplePager menuItemsPager = new SimplePager();
        menuItemsPager.setDisplay(cellTable);
        add(menuItemsPager);
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
                addMenuItemButton.setEnabled(true);
                addMenuItemButton.setFocus(true);
            }
        });


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


        

        // Add a handler to send the name to the server
        addMenuItemButton.addClickHandler(new AddMenuItemHandler());
    }
}

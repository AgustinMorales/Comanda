package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class UIEditMenuItem extends Composite {

	
	private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);
	
    private static UIEditMenuItemUiBinder uiBinder = GWT
            .create(UIEditMenuItemUiBinder.class);
    @UiField TextBox tbName;
    @UiField DoubleBox dbPrice;
    @UiField FileUpload fuImageFile;
    @UiField TextArea taDescription;
    @UiField Button btnSaveChanges;
    @UiField FormPanel itemDataFormPanel;
    
    private NewMenuItemHandler newMenuItemHandler;

    interface UIEditMenuItemUiBinder extends UiBinder<Widget, UIEditMenuItem> {
    }

    public UIEditMenuItem() {
        initWidget(uiBinder.createAndBindUi(this));
        
        
    }

	@UiHandler("btnSaveChanges")
	void onBtnSaveChangesClick(ClickEvent event) {
		greetingService.getUploadUrl(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error while getting upload URL");
                
            }

            @Override
            public void onSuccess(String result) {
                itemDataFormPanel.setAction(result);
                itemDataFormPanel.submit();
                
            }
        });
	}
	
	
	public interface NewMenuItemHandler{
        void onNewMenuItem();
        void onCancel();
    }
	
	public void setNewMenuItemHandler(NewMenuItemHandler handler){
        this.newMenuItemHandler = handler;
    }
	
	public void setData(String[] data){
//        keyIdTB.setText(data[0]);
        tbName.setText(data[2]);
        dbPrice.setText(data[3]);
    }
    
    public void reset(){
        itemDataFormPanel.reset();
    }
	@UiHandler("itemDataFormPanel")
	void onItemDataFormPanelSubmitComplete(SubmitCompleteEvent event) {
		itemDataFormPanel.reset();
		newMenuItemHandler.onNewMenuItem();
	}
}

package com.company.comanda.peter.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class UIEditMenuItem extends Composite {

	
	private final GUIServiceAsync greetingService = GWT
            .create(GUIService.class);
	
    private static UIEditMenuItemUiBinder uiBinder = GWT
            .create(UIEditMenuItemUiBinder.class);
    @UiField TextBox tbName;
    @UiField DoubleBox dbPrice;
    @UiField FileUpload fuImageFile;
    @UiField TextArea taDescription;
    @UiField Button btnSaveChanges;
    @UiField FormPanel itemDataFormPanel;
    @UiField TextBox tbKeyId;
    @UiField Button btnCancel;
    @UiField Image imgItem;
    @UiField ListBox lbCategory;
    
    private NewMenuItemHandler newMenuItemHandler;

    interface UIEditMenuItemUiBinder extends UiBinder<Widget, UIEditMenuItem> {
    }

    public UIEditMenuItem() {
        initWidget(uiBinder.createAndBindUi(this));
        itemDataFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        itemDataFormPanel.setMethod(FormPanel.METHOD_POST);
        
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
//	    itemDataFormPanel.setAction("/newMenuItem");
//	    itemDataFormPanel.submit();
	}
	
	
	public interface NewMenuItemHandler{
        void onNewMenuItem();
        void onCancel();
    }
	
	public void setNewMenuItemHandler(NewMenuItemHandler handler){
        this.newMenuItemHandler = handler;
    }
	
	public void setData(String[] data){
        tbKeyId.setText(data[0]);
        String imageKey = data[1];
        if(imageKey != null){
            imgItem.setUrl(GWT.getHostPageBaseURL() + "serveBlob?id=" + data[1]);
            imgItem.setVisible(true);
        }
        tbName.setText(data[2]);
        dbPrice.setText(data[3]);
        taDescription.setText(data[4]);
    }
    
    public void reset(){
        loadCategories();
        itemDataFormPanel.reset();
        imgItem.setVisible(false);
    }
	@UiHandler("itemDataFormPanel")
	void onItemDataFormPanelSubmitComplete(SubmitCompleteEvent event) {
		itemDataFormPanel.reset();
		newMenuItemHandler.onNewMenuItem();
	}
    @UiHandler("btnCancel")
    void onBtnCancelClick(ClickEvent event) {
        itemDataFormPanel.reset();
        newMenuItemHandler.onCancel();
    }
    @UiHandler("fuImageFile")
    void onFuImageFileChange(ChangeEvent event) {
        imgItem.setVisible(false);
    }
    
    private void loadCategories(){
        greetingService.getCategories(
                new AsyncCallback<List<String[]>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Error");
                    }

                    @Override
                    public void onSuccess(List<String[]> result) {
                        lbCategory.setEnabled(false);
                        lbCategory.clear();
                        for(String[] elem : result){
                            lbCategory.addItem(elem[1], elem[0]);
                        }
                        lbCategory.setEnabled(true);
                    }
        });
    }
    
}

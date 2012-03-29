package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;

public class UIEditCategory extends Composite {

    private NewCategoryHandler handler;
    
    private final GUIServiceAsync GUIService = GWT
			.create(GUIService.class);
    
    private static UIEditCategoryUiBinder uiBinder = GWT
            .create(UIEditCategoryUiBinder.class);
    @UiField TextBox tbName;
    @UiField Button btnOK;
    @UiField Button btnCancel;

    interface UIEditCategoryUiBinder extends UiBinder<Widget, UIEditCategory> {
    }

    public UIEditCategory() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public interface NewCategoryHandler{
        void onNewCategory();
        void onCancel();
    }
    
    public void setNewCategoryHandler(NewCategoryHandler handler){
        this.handler = handler;
    }
    
    public void reset(){
    	tbName.setText("");
    }
    
    public void focus(){
    	tbName.setFocus(true);
    }
	@UiHandler("btnOK")
	void onBtnOKClick(ClickEvent event) {
		doAddOrModify();
	}
	
	private void doAddOrModify(){
		if(validate()){
			GUIService.addOrModifyCategory(null, tbName.getText(), new AsyncCallback<Void>() {
				
				@Override
				public void onSuccess(Void result) {
					handler.onNewCategory();
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error");
					
				}
			});
		}
	}
	
	@UiHandler("btnCancel")
	void onBtnCancelClick(ClickEvent event) {
		handler.onCancel();
	}
	
	private boolean validate(){
		boolean result = true;
		if(tbName.getText().length() == 0){
			Window.alert("Por favor, introduzca el nombre de la categoría");
			result = false;
		}
		return result;
	}
	@UiHandler("tbName")
	void onTbNameKeyUp(KeyUpEvent event) {
		if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
            doAddOrModify();
        }
	}
}

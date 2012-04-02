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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.event.logical.shared.ValueChangeEvent;

public class UIEditMenuItem extends Composite {


    private final GUIServiceAsync greetingService = GWT
            .create(GUIService.class);

    private static UIEditMenuItemUiBinder uiBinder = GWT
            .create(UIEditMenuItemUiBinder.class);
    @UiField TextBox tbName;
    @UiField FileUpload fuImageFile;
    @UiField TextArea taDescription;
    @UiField Button btnSaveChanges;
    @UiField FormPanel itemDataFormPanel;
    @UiField TextBox tbKeyId;
    @UiField Button btnCancel;
    @UiField Image imgItem;
    @UiField ListBox lbCategory;
    @UiField CheckBox cbSmall;
    @UiField CheckBox cbMedium;
    @UiField CheckBox cbLarge;
    @UiField CheckBox cbTapa;
    @UiField CheckBox cbHalf;
    @UiField CheckBox cbFull;
    @UiField DoubleBox dbSinglePrice;
    @UiField DoubleBox dbSmallPrice;
    @UiField DoubleBox dbMediumPrice;
    @UiField DoubleBox dbLargePrice;
    @UiField DoubleBox dbTapaPrice;
    @UiField DoubleBox dbHalfPrice;
    @UiField DoubleBox dbFullPrice;
    @UiField RadioButton rbSinglePrice;
    @UiField RadioButton rbMultiplePrice;

    private boolean editingExistingData;

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
        if(validate()){
            doCreatingItem();
            greetingService.getUploadUrl(new AsyncCallback<String>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("Error while getting upload URL");
                    doNotCreatingItem();
                }

                @Override
                public void onSuccess(String result) {
                	if(result.contains("MacBook") == false){
                		itemDataFormPanel.setAction(result);
                	}
                	else{
                		itemDataFormPanel.setEncoding(FormPanel.ENCODING_URLENCODED);
                		itemDataFormPanel.setEncoding(FormPanel.METHOD_GET);
                		itemDataFormPanel.setAction("/newMenuItem");
                	}
                	itemDataFormPanel.submit();

                }
            });
        }
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
        taDescription.setText(data[3]);
        editingExistingData = true;
        if(data[4] != null){
        	rbSinglePrice.setValue(true);
        	rbSinglePrice.setFormValue("single");
        	dbSinglePrice.setValue(Double.parseDouble(data[4]));
        }
        else{
        	rbMultiplePrice.setValue(true);
        	rbMultiplePrice.setFormValue("multiple");
        	if(data[5] != null){
            	cbSmall.setValue(true);
            	dbSmallPrice.setValue(Double.parseDouble(data[5]));
            }
            if(data[6] != null){
            	cbMedium.setValue(true);
            	dbMediumPrice.setValue(Double.parseDouble(data[6]));
            }
            if(data[7] != null){
            	cbLarge.setValue(true);
            	dbLargePrice.setValue(Double.parseDouble(data[7]));
            }
            if(data[8] != null){
            	cbTapa.setValue(true);
            	dbTapaPrice.setValue(Double.parseDouble(data[8]));
            }
            if(data[9] != null){
            	cbHalf.setValue(true);
            	dbHalfPrice.setValue(Double.parseDouble(data[9]));
            }
            if(data[10] != null){
            	cbFull.setValue(true);
            	dbFullPrice.setValue(Double.parseDouble(data[10]));
            }
        }
        
    }

    public void reset(){
        loadCategories();
        doNotCreatingItem();
        itemDataFormPanel.reset();
        imgItem.setVisible(false);
        editingExistingData = false;

    }
    @UiHandler("itemDataFormPanel")
    void onItemDataFormPanelSubmitComplete(SubmitCompleteEvent event) {
        itemDataFormPanel.reset();
        String result = event.getResults();
        if(result == null || (result.contains("SUCCESS") == false)){
        	Window.alert("Error");
        }
        newMenuItemHandler.onNewMenuItem();
        doNotCreatingItem();
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


    private boolean validate(){
        boolean result = true;
        String description = taDescription.getText();
        String name = tbName.getText();
        String imageFile = fuImageFile.getFilename();
        if(rbSinglePrice.getValue()){
        	fixPriceFormat(dbSinglePrice);
        	if(dbSinglePrice.getValue() == null){
        		Window.alert("El precio introducido no es válido");
        		result = false;
        	}
        }
        else{
        	boolean oneChecked = false;
        	if(cbSmall.getValue()){
        		oneChecked = true;
        		fixPriceFormat(dbSmallPrice);
        		if(dbSmallPrice.getValue() == null){
        			Window.alert("El precio introducido para 'Pequeña' no es válido");
        			result = false;
        		}
        	}
        	if(result && cbMedium.getValue()){
        		oneChecked = true;
        		fixPriceFormat(dbMediumPrice);
        		if(dbMediumPrice.getValue() == null){
        			Window.alert("El precio introducido para 'Mediana' no es válido");
        			result = false;
        		}
        	}
        	if(result && cbLarge.getValue()){
        		oneChecked = true;
        		fixPriceFormat(dbLargePrice);
        		if(dbLargePrice.getValue() == null){
        			Window.alert("El precio introducido para 'Grande' no es válido");
        			result = false;
        		}
        	}
        	if(result && cbTapa.getValue()){
        		oneChecked = true;
        		fixPriceFormat(dbTapaPrice);
        		if(dbTapaPrice.getValue() == null){
        			Window.alert("El precio introducido para 'Tapa' no es válido");
        			result = false;
        		}
        	}
        	if(result && cbHalf.getValue()){
        		oneChecked = true;
        		fixPriceFormat(dbHalfPrice);
        		if(dbHalfPrice.getValue() == null){
        			Window.alert("El precio introducido para '1/2 Ración' no es válido");
        			result = false;
        		}
        	}
        	if(result && cbFull.getValue()){
        		oneChecked = true;
        		fixPriceFormat(dbFullPrice);
        		if(dbFullPrice.getValue() == null){
        			Window.alert("El precio introducido para 'Ración' no es válido");
        			result = false;
        		}
        	}
        }
        
        if(result){
        	if(name == null || name.length() == 0){
        		Window.alert("Por favor, introduzca el nombre");
        		result = false;
        	}
        }
        return result;
    }

    private void fixPriceFormat(DoubleBox dbPrice){
    	dbPrice.setText(dbPrice.getText().replaceAll(",", "."));
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

    private void doCreatingItem(){
        btnSaveChanges.setEnabled(false);
        btnCancel.setEnabled(false);
        btnSaveChanges.setText("Guardando...");
    }

    private void doNotCreatingItem(){
        btnSaveChanges.setText("Guardar");
        btnSaveChanges.setEnabled(true);
        btnCancel.setEnabled(true);
    }
	
}

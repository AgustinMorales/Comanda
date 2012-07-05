package com.company.comanda.peter.client;

import java.util.List;

import com.company.comanda.peter.shared.Constants;
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
    @UiField TextBox tbExtra1;
    @UiField TextBox tbExtra2;
    @UiField TextBox tbExtra3;
    @UiField TextBox tbExtra4;
    @UiField TextBox tbExtra5;
    @UiField TextBox tbExtra6;
    @UiField TextBox tbExtra7;
    @UiField TextBox tbExtra8;
    @UiField TextBox tbExtra9;
    @UiField DoubleBox tbExtraPrice1;
    @UiField DoubleBox tbExtraPrice2;
    @UiField DoubleBox tbExtraPrice3;
    @UiField DoubleBox tbExtraPrice4;
    @UiField DoubleBox tbExtraPrice5;
    @UiField DoubleBox tbExtraPrice6;
    @UiField DoubleBox tbExtraPrice7;
    @UiField DoubleBox tbExtraPrice8;
    @UiField DoubleBox tbExtraPrice9;
    @UiField TextBox tbExtrasName;
    @UiField TextBox tbExtra10;
    @UiField TextBox tbExtra11;
    @UiField TextBox tbExtra12;
    @UiField TextBox tbExtra13;
    @UiField TextBox tbExtra14;
    @UiField TextBox tbExtra15;
    @UiField TextBox tbExtra16;
    @UiField TextBox tbExtra17;
    @UiField TextBox tbExtra18;
    @UiField TextBox tbExtra19;
    @UiField TextBox tbExtra20;
    @UiField TextBox tbExtra21;
    @UiField DoubleBox tbExtraPrice10;
    @UiField DoubleBox tbExtraPrice11;
    @UiField DoubleBox tbExtraPrice12;
    @UiField DoubleBox tbExtraPrice13;
    @UiField DoubleBox tbExtraPrice14;
    @UiField DoubleBox tbExtraPrice15;
    @UiField DoubleBox tbExtraPrice16;
    @UiField DoubleBox tbExtraPrice17;
    @UiField DoubleBox tbExtraPrice18;
    @UiField DoubleBox tbExtraPrice19;
    @UiField DoubleBox tbExtraPrice20;
    @UiField DoubleBox tbExtraPrice21;
    

    private TextBox[] tbExtras;
    private DoubleBox[] tbExtrasPrices;
    
    private boolean editingExistingData;

    private NewMenuItemHandler newMenuItemHandler;

    interface UIEditMenuItemUiBinder extends UiBinder<Widget, UIEditMenuItem> {
    }

    public UIEditMenuItem() {
        initWidget(uiBinder.createAndBindUi(this));
        itemDataFormPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        itemDataFormPanel.setMethod(FormPanel.METHOD_POST);
        tbExtras = new TextBox[Constants.NO_OF_EXTRAS_IN_UI];
        tbExtrasPrices = new DoubleBox[Constants.NO_OF_EXTRAS_IN_UI];
        tbExtras[0] = tbExtra1;
        tbExtras[1] = tbExtra2;
        tbExtras[2] = tbExtra3;
        tbExtras[3] = tbExtra4;
        tbExtras[4] = tbExtra5;
        tbExtras[5] = tbExtra6;
        tbExtras[6] = tbExtra7;
        tbExtras[7] = tbExtra8;
        tbExtras[8] = tbExtra9;
        tbExtras[9] = tbExtra10;
        tbExtras[10] = tbExtra11;
        tbExtras[11] = tbExtra12;
        tbExtras[12] = tbExtra13;
        tbExtras[13] = tbExtra14;
        tbExtras[14] = tbExtra15;
        tbExtras[15] = tbExtra16;
        tbExtras[16] = tbExtra17;
        tbExtras[17] = tbExtra18;
        tbExtras[18] = tbExtra19;
        tbExtras[19] = tbExtra20;
        tbExtras[20] = tbExtra21;
        tbExtrasPrices[0] = tbExtraPrice1;
        tbExtrasPrices[1] = tbExtraPrice2;
        tbExtrasPrices[2] = tbExtraPrice3;
        tbExtrasPrices[3] = tbExtraPrice4;
        tbExtrasPrices[4] = tbExtraPrice5;
        tbExtrasPrices[5] = tbExtraPrice6;
        tbExtrasPrices[6] = tbExtraPrice7;
        tbExtrasPrices[7] = tbExtraPrice8;
        tbExtrasPrices[8] = tbExtraPrice9;
        tbExtrasPrices[9] = tbExtraPrice10;
        tbExtrasPrices[10] = tbExtraPrice11;
        tbExtrasPrices[11] = tbExtraPrice12;
        tbExtrasPrices[12] = tbExtraPrice13;
        tbExtrasPrices[13] = tbExtraPrice14;
        tbExtrasPrices[14] = tbExtraPrice15;
        tbExtrasPrices[15] = tbExtraPrice16;
        tbExtrasPrices[16] = tbExtraPrice17;
        tbExtrasPrices[17] = tbExtraPrice18;
        tbExtrasPrices[18] = tbExtraPrice19;
        tbExtrasPrices[19] = tbExtraPrice20;
        tbExtrasPrices[20] = tbExtraPrice21;
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
        final int baseName = 12;
        final int basePrice = baseName + Constants.NO_OF_EXTRAS_IN_UI;
        final int basePriceValue = basePrice + Constants.NO_OF_EXTRAS_IN_UI;
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
        	dbSinglePrice.setValue(Double.parseDouble(data[basePriceValue]));
        }
        else{
        	rbMultiplePrice.setValue(true);
        	rbMultiplePrice.setFormValue("multiple");
        	if(data[5] != null){
            	cbSmall.setValue(true);
            	dbSmallPrice.setValue(Double.parseDouble(data[basePriceValue + 1]));
            }
            if(data[6] != null){
            	cbMedium.setValue(true);
            	dbMediumPrice.setValue(Double.parseDouble(data[basePriceValue + 2]));
            }
            if(data[7] != null){
            	cbLarge.setValue(true);
            	dbLargePrice.setValue(Double.parseDouble(data[basePriceValue + 3]));
            }
            if(data[8] != null){
            	cbTapa.setValue(true);
            	dbTapaPrice.setValue(Double.parseDouble(data[basePriceValue + 4]));
            }
            if(data[9] != null){
            	cbHalf.setValue(true);
            	dbHalfPrice.setValue(Double.parseDouble(data[basePriceValue + 5]));
            }
            if(data[10] != null){
            	cbFull.setValue(true);
            	dbFullPrice.setValue(Double.parseDouble(data[basePriceValue + 6]));
            }
        }
        if(data[11] != null){
            tbExtrasName.setText(data[11]);
        }
        
        for(int i=0;i<Constants.NO_OF_EXTRAS_IN_UI;i++){
            String name = data[baseName + i];
            if(name != null){
                tbExtras[i].setText(name);
                tbExtrasPrices[i].setText(data[basePrice + i]);
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
	
    public void setCategory(int index){
        lbCategory.setSelectedIndex(index);
    }
}

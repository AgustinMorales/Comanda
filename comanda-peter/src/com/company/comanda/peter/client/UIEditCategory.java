package com.company.comanda.peter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UIEditCategory extends Composite {

    private NewCategoryHandler handler;
    
    private static UIEditCategoryUiBinder uiBinder = GWT
            .create(UIEditCategoryUiBinder.class);

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
    	
    }
}

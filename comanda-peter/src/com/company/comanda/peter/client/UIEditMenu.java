package com.company.comanda.peter.client;

import java.util.List;
import java.util.Set;

import com.company.comanda.peter.client.UIEditMenuItem.NewMenuItemHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

public class UIEditMenu extends Composite {

    
    private static UIEditMenuUiBinder uiBinder = GWT
            .create(UIEditMenuUiBinder.class);
    private final GUIServiceAsync greetingService = GWT
            .create(GUIService.class);
    @UiField Button btnNewItem;
    @UiField Button btnEditItem;
    @UiField Button btnDeleteItem;
    @UiField TabLayoutPanel tabPanelCategories;
    @UiField Label lblLoading;
    private MultiSelectionModel<String[]> selectionModel;
    private DialogBox dialogBox;
    private UIEditMenuItem newMenuItemPanel;
    private UIMenuTable[] menuTables;

    interface UIEditMenuUiBinder extends UiBinder<Widget, UIEditMenu> {
    }

    public UIEditMenu() {
        initWidget(uiBinder.createAndBindUi(this));
        configureMenuItemsTable();
        configureMenuItemDialogBox();
    }

    protected void configureMenuItemDialogBox(){
    	
    	dialogBox = new DialogBox();
        newMenuItemPanel = new UIEditMenuItem();
        dialogBox.setWidget(newMenuItemPanel);
        
        newMenuItemPanel.setNewMenuItemHandler(new NewMenuItemHandler() {

            @Override
            public void onNewMenuItem() {
                selectionModel.clear();
                refreshTable();
                dialogBox.hide();

            }

            @Override
            public void onCancel() {
                dialogBox.hide();

            }
        });
    }
    
    protected void configureMenuItemsTable(){
        
        
        
        selectionModel = new MultiSelectionModel<String[]>();
        
        
        
        greetingService.getCategories(new AsyncCallback<List<String[]>>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error");
                
            }

            @Override
            public void onSuccess(List<String[]> result) {
                menuTables = new UIMenuTable[result.size()];
                int counter = 0;
                for(String[] elem: result){
                    UIMenuTable newMenuTable = new UIMenuTable();
                    menuTables[counter] = newMenuTable;
                    newMenuTable.setSelectionModel(selectionModel);
                    tabPanelCategories.add(newMenuTable, elem[1]);
                    counter++;
                    lblLoading.setVisible(false);
                    tabPanelCategories.setVisible(true);
                    refreshTable();
                }
                
            }
        });
        
        
        selectionModel.addSelectionChangeHandler(new Handler() {
            
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                int size = selectionModel.getSelectedSet().size();
                switch (size) {
                case 0:
                    btnDeleteItem.setEnabled(false);
                    btnEditItem.setEnabled(false);
                    break;
                case 1:
                    btnDeleteItem.setEnabled(true);
                    btnEditItem.setEnabled(true);
                    break;
                default:
                    btnDeleteItem.setEnabled(true);
                    btnEditItem.setEnabled(false);
                    break;
                }
                
            }
        });
    }


	@UiHandler("btnNewItem")
	void onBtnNewItemClick(ClickEvent event) {
		newMenuItemPanel.reset();
        dialogBox.show();
        dialogBox.center();
	}
	
	public void refreshTable(){
        int selected = tabPanelCategories.getSelectedIndex();
        menuTables[selected].refreshTable();
    }
	@UiHandler("btnEditItem")
	void onBtnEditItemClick(ClickEvent event) {
		Set<String[]> selectedSet = selectionModel.getSelectedSet();
        if(selectedSet.size() == 1){
            newMenuItemPanel.reset();
            newMenuItemPanel.setData(selectedSet.iterator().next());
            dialogBox.show();
            dialogBox.center();
        }
	}
	@UiHandler("btnDeleteItem")
	void onBtnDeleteItemClick(ClickEvent event) {
		Set<String[]> selectedSet = selectionModel.getSelectedSet();
        int size = selectedSet.size();
        long[] keysToDelete = new long[size];
        int counter = 0;
        for(String[] item : selectedSet){
            keysToDelete[counter] = Long.parseLong(item[0]);
            counter++;
        };
        greetingService.deleteMenuItems(keysToDelete, new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error while deleting");
                
            }

            @Override
            public void onSuccess(Void result) {
                refreshTable();
                
            }
        });
        
        selectionModel.clear();
	}
    @UiHandler("tabPanelCategories")
    void onTabPanelCategoriesSelection(
            @SuppressWarnings("rawtypes") SelectionEvent event) {
        refreshTable();
    }
}

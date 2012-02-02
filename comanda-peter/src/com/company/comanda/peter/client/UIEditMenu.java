package com.company.comanda.peter.client;

import java.util.Set;

import com.company.comanda.peter.client.NewMenuItemPanel.NewMenuItemHandler;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class UIEditMenu extends Composite {

    public static final int PAGE_SIZE = 20;
    
    private static UIEditMenuUiBinder uiBinder = GWT
            .create(UIEditMenuUiBinder.class);
    private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);
    @UiField Button btnNewItem;
    @UiField Button btnEditItem;
    @UiField Button btnDeleteItem;
    @UiField SimplePager menuItemsPager;
    @UiField CellTable<String[]> menuItemsTable;
    private MultiSelectionModel<String[]> selectionModel;
    private DialogBox dialogBox;
    private NewMenuItemPanel newMenuItemPanel;

    interface UIEditMenuUiBinder extends UiBinder<Widget, UIEditMenu> {
    }

    public UIEditMenu() {
        initWidget(uiBinder.createAndBindUi(this));
        configureMenuItemsTable();
        configureMenuItemDialogBox();
    }

    protected void configureMenuItemDialogBox(){
    	
    	dialogBox = new DialogBox();
        newMenuItemPanel = new NewMenuItemPanel();
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
        menuItemsTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<String[]> createCheckboxManager());
        Column<String[], Boolean> checkColumn = new Column<String[], Boolean>(
                new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(String[] object) {
                // Get the value from the selection model.
                return selectionModel.isSelected(object);
            }
        };
        
        menuItemsTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        menuItemsTable.setColumnWidth(checkColumn, 40, Unit.PX);
        
        
        // Add a text column to show the name.
        TextColumn<String[]> nameColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[2];
            }
        };
        menuItemsTable.addColumn(nameColumn, "Name");
        
        TextColumn<String[]> priceColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[3];
            }
        };
        menuItemsTable.addColumn(priceColumn, "Price");

        AsyncDataProvider<String[]> provider = new AsyncDataProvider<String[]>() {
            @Override
            protected void onRangeChanged(HasData<String[]> display) {
                final int start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                AsyncCallback<PagedResult<String[]>> callback = new AsyncCallback<PagedResult<String[]>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }
                    @Override
                    public void onSuccess(PagedResult<String[]> result) {
                        updateRowData(start, result.getList());
                        updateRowCount(result.getTotal(), true);
                    }
                };
                // The remote service that should be implemented
                greetingService.getMenuItems(start, length, callback);
            }
        };
        provider.addDataDisplay(menuItemsTable);
        
        menuItemsPager.setDisplay(menuItemsTable);
        menuItemsPager.setPageSize(PAGE_SIZE);
        
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
        Range range = menuItemsTable.getVisibleRange();
        RangeChangeEvent.fire(menuItemsTable, range);
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
	}
}

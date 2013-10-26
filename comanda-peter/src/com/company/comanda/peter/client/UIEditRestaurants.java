package com.company.comanda.peter.client;

import java.util.List;
import java.util.Set;

import com.company.comanda.peter.client.UIEditCategory.NewCategoryHandler;
import com.company.comanda.peter.client.UIEditRestaurant.NewRestaurantHandler;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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

public class UIEditRestaurants extends Composite {

    private final AdminServiceAsync adminService = GWT
            .create(AdminService.class);
    
    private static final int PAGE_SIZE = 20;
    @UiField Button btnNewRestaurant;
    @UiField Button btnEditRestaurant;
    @UiField Button btnDeleteRestaurant;
    @UiField CellTable restaurantsTable;
    @UiField SimplePager restaurantsPager;
    @UiField Button btnCopy;
    private DialogBox dialogBox;
    private UIEditRestaurant editRestaurantPanel;
    private MultiSelectionModel<String[]> selectionModel;
    private boolean configured;
    
    
    private static UIEditRestaurantsUiBinder uiBinder = GWT
            .create(UIEditRestaurantsUiBinder.class);

    interface UIEditRestaurantsUiBinder extends
            UiBinder<Widget, UIEditRestaurants> {
    }

    public UIEditRestaurants() {
        initWidget(uiBinder.createAndBindUi(this));
        configureRestaurantDialogBox();
        configureRestaurantsTable();
    }

    protected void configureRestaurantDialogBox(){

        dialogBox = new DialogBox();
        editRestaurantPanel = new UIEditRestaurant();
        dialogBox.setWidget(editRestaurantPanel);

        editRestaurantPanel.setNewRestaurantHandler(new NewRestaurantHandler() {
            
            @Override
            public void onNewRestaurant() {
                selectionModel.clear();
                refreshTable();
                dialogBox.hide();
            }
            
            @Override
            public void onCancel() {
                dialogBox.hide();
                selectionModel.clear();
            }
        });
    }
    
    
    public void configureRestaurantsTable(){
        
        selectionModel = new MultiSelectionModel<String[]>();
        
        restaurantsTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<String[]> createCheckboxManager());
        Column<String[], Boolean> checkColumn = new Column<String[], Boolean>(
                new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(String[] object) {
                // Get the value from the selection model.
                return selectionModel.isSelected(object);
            }
        };

        restaurantsTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        restaurantsTable.setColumnWidth(checkColumn, 40, Unit.PX);
     // Add a text column to show the name.
        TextColumn<String[]> restaurantNameColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[1];
            }
        };
        
        
        restaurantsTable.addColumn(restaurantNameColumn, "Nombre");

        TextColumn<String[]> loginColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[2];
            }
        };
        restaurantsTable.addColumn(loginColumn, "Login");

        TextColumn<String[]> addressColumn = new TextColumn<String[]>() {
            @Override
            public String getValue(String[] object) {
                return object[3];
            }
        };
        restaurantsTable.addColumn(addressColumn, "Dirección");
        
        AsyncDataProvider<String[]> provider = new AsyncDataProvider<String[]>() {
            @Override
            protected void onRangeChanged(HasData<String[]> display) {
                final int start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                AsyncCallback<PagedResult<String[]>> callback = 
                        new AsyncCallback<PagedResult<String[]>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }
                    @Override
                    public void onSuccess(PagedResult<String[]> result) {
                        updateRowData(0, result.getList());
                        updateRowCount(result.getTotal(), true);
                    }
                };
                // The remote service that should be implemented
                adminService.getRestaurants(start, length, callback);
            }
        };

        provider.addDataDisplay(restaurantsTable);




        selectionModel.addSelectionChangeHandler(new Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                int size = selectionModel.getSelectedSet().size();
                switch (size) {
                case 0:
                    btnDeleteRestaurant.setEnabled(false);
                    btnEditRestaurant.setEnabled(false);
                    btnCopy.setEnabled(false);
                    break;
                case 1:
                    btnDeleteRestaurant.setEnabled(true);
                    btnEditRestaurant.setEnabled(true);
                    btnCopy.setEnabled(true);
                    break;
                default:
                    btnDeleteRestaurant.setEnabled(true);
                    btnEditRestaurant.setEnabled(false);
                    btnCopy.setEnabled(false);
                    break;
                }

            }
        });
        
        restaurantsPager.setDisplay(restaurantsTable);
        restaurantsPager.setPageSize(PAGE_SIZE);
        configured = true;
    }
    
    public void refreshTable(){
        if(configured == false){
            configureRestaurantsTable();
        }
        else{
            Range range = restaurantsTable.getVisibleRange();
            RangeChangeEvent.fire(restaurantsTable, range);
        }
    }
    
    @UiHandler("btnNewRestaurant")
    void onBtnNewRestaurantClick(ClickEvent event) {
        editRestaurantPanel.reset();
        dialogBox.center();
    }
    @UiHandler("btnEditRestaurant")
    void onBtnEditRestaurantClick(ClickEvent event) {
        editRestaurantPanel.reset();
        Set<String[]> selected = selectionModel.getSelectedSet();
        if(selected.size() == 1){
            editRestaurantPanel.setData(selected.iterator().next());
            dialogBox.center();
        }
        
    }
    @UiHandler("btnCopy")
    void onBtnCopyClick(ClickEvent event) {
        editRestaurantPanel.reset();
        Set<String[]> selected = selectionModel.getSelectedSet();
        if(selected.size() == 1){
            editRestaurantPanel.setData(selected.iterator().next());
            editRestaurantPanel.setCopy();
            dialogBox.center();
        }
        
    }
}

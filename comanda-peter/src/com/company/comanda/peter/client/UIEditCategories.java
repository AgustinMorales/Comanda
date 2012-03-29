package com.company.comanda.peter.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.company.comanda.peter.client.UIEditCategory.NewCategoryHandler;
import com.company.comanda.peter.client.UIEditMenuItem.NewMenuItemHandler;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UIEditCategories extends Composite {

	public static final int PAGE_SIZE = 20;

	private static UIEditCategoriesUiBinder uiBinder = GWT
			.create(UIEditCategoriesUiBinder.class);
	private final GUIServiceAsync GUIService = GWT
			.create(GUIService.class);
	@UiField Button btnNewItem;
	@UiField Button btnDeleteItem;
	@UiField Label lblLoading;
	@UiField CellTable categoriesTable;
	@UiField VerticalPanel categoriesPanel;
	private MultiSelectionModel<String[]> selectionModel;
	private DialogBox dialogBox;
	private UIEditCategory newCategoryPanel;
	private UIMenuTable[] menuTables;
	
	private boolean configured;

	interface UIEditCategoriesUiBinder extends UiBinder<Widget, UIEditCategories> {
	}

	public UIEditCategories() {
		initWidget(uiBinder.createAndBindUi(this));
		configureCategoriesTable();
		configureCategoryDialogBox();
	}

	protected void configureCategoryDialogBox(){

		dialogBox = new DialogBox();
		newCategoryPanel = new UIEditCategory();
		dialogBox.setWidget(newCategoryPanel);

		newCategoryPanel.setNewCategoryHandler(new NewCategoryHandler() {

			@Override
			public void onNewCategory() {
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

	protected void configureCategoriesTable(){



		selectionModel = new MultiSelectionModel<String[]>();



		categoriesTable.setSelectionModel(selectionModel,
				DefaultSelectionEventManager.<String[]> createCheckboxManager());
		Column<String[], Boolean> checkColumn = new Column<String[], Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(String[] object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};

		categoriesTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		categoriesTable.setColumnWidth(checkColumn, 40, Unit.PX);


		// Add a text column to show the name.
		TextColumn<String[]> nameColumn = new TextColumn<String[]>() {
			@Override
			public String getValue(String[] object) {
				return object[1];
			}
		};
		categoriesTable.addColumn(nameColumn, "Nombre");


		AsyncDataProvider<String[]> provider = new AsyncDataProvider<String[]>() {
			@Override
			protected void onRangeChanged(HasData<String[]> display) {
				AsyncCallback<List<String[]>> callback = new AsyncCallback<List<String[]>>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
					@Override
					public void onSuccess(List<String[]> result) {
						updateRowData(0, result);
						updateRowCount(result.size(), true);
						lblLoading.setVisible(false);
						categoriesPanel.setVisible(true);
					}
				};
				// The remote service that should be implemented
				GUIService.getCategories(callback);
			}
		};

		provider.addDataDisplay(categoriesTable);




		selectionModel.addSelectionChangeHandler(new Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				int size = selectionModel.getSelectedSet().size();
				switch (size) {
				case 0:
					btnDeleteItem.setEnabled(false);
					break;
				case 1:
					btnDeleteItem.setEnabled(true);
					break;
				default:
					btnDeleteItem.setEnabled(true);
					break;
				}

			}
		});
		
		configured = true;
	}


	@UiHandler("btnNewItem")
	void onBtnNewItemClick(ClickEvent event) {
		newCategoryPanel.reset();
		dialogBox.center();
		newCategoryPanel.focus();
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
		GUIService.removeCategories(keysToDelete, new AsyncCallback<Void>() {

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


	public void refreshTable(){
		if(configured == false){
			configureCategoriesTable();
		}
		else{
			Range range = categoriesTable.getVisibleRange();
			RangeChangeEvent.fire(categoriesTable, range);
		}
	}
}

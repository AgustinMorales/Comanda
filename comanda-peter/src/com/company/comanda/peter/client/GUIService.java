package com.company.comanda.peter.client;

import java.util.List;

import com.company.comanda.peter.shared.BillState;
import com.company.comanda.peter.shared.BillType;
import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.PagedResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GUIService extends RemoteService {
	PagedResult<String[]> getMenuItems(int start, int length, Long categoryId);
	PagedResult<String[]> getOrders(int start, int length, OrderState state, 
	        String tableName);
	public String getUploadUrl();
	void acceptOrder(String orderKey);
	void deleteMenuItems(long[] keyIds);
	boolean login(String username, String password);
	void newRestaurant(String name, String password, 
	        String address);
	void addTable(String tablename);
	List<String[]> getTables();
	List<String[]> getCategories();
	PagedResult<String[]> getBills(int start, int length, 
	        BillState state, BillType type);
}

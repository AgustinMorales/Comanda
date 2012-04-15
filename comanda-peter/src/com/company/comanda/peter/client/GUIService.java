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
	PagedResult<String[]> getOrders(int start, int length, 
	        BillType billType, OrderState state, 
	        String tableKeyString,
	        String billKeyString);
	public String getUploadUrl();
	void acceptOrder(String orderKey);
	void deleteMenuItems(long[] keyIds);
	String login(String username, String password);
	void addTable(String tablename);
	List<String[]> getTables();
	PagedResult<String[]> getCategories(int start, int length);
	List<String[]> getCategories();
	void addOrModifyCategory(Long id, String name);
	void removeCategories(long[] ids);
	PagedResult<String[]> getBills(int start, int length, 
	        BillState state, BillType type);
	void changeBillState(String billKeyString, BillState newState, 
	        Integer deliveryDelay);
	String getUploadUrlForNewRestaurant();
	String login(String token);
}

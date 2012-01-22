package com.company.comanda.peter.shared;

import java.io.Serializable;
import java.util.List;

public class PagedResult<T> implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 3762629316322844919L;
    private List<T> list;
    private int total;
    
    public PagedResult(){
        super();
    }
    
    public PagedResult(List<T> list, int total){
        super();
        this.list = list;
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    
    
}

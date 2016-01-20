package com.felix.nsf;

import java.util.List;

public class Pagination {
	
	@Need2JSON
	private int limit; 
	
	@Need2JSON
	private int start = 0;
	  
	@Need2JSON
    private List<?> items; 

	@Need2JSON
    private int totalCount; 

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}

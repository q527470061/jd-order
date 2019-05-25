package com.jd.order.pojo;

import java.util.List;

import com.jd.pojo.OrderCustom;

public class OrderRestult {

	// 商品列表
	private List<OrderCustom> orderCustoms;
	// 总记录数
	private long recordCount;
	// 总页数
	private long pageCount;

	public List<OrderCustom> getOrderCustoms() {
		return orderCustoms;
	}

	public void setOrderCustoms(List<OrderCustom> orderCustoms) {
		this.orderCustoms = orderCustoms;
	}

	public long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}

	public long getPageCount() {
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

	public long getCurPage() {
		return curPage;
	}

	public void setCurPage(long curPage) {
		this.curPage = curPage;
	}

	// 当前页
	private long curPage;
}

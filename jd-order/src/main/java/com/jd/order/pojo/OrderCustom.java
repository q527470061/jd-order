package com.jd.order.pojo;

import java.util.List;

import com.jd.pojo.Order;
import com.jd.pojo.OrderItem;
import com.jd.pojo.OrderShipping;

/*
 * 订单order的扩展类
 */
public class OrderCustom extends Order {

	private List<OrderItem> orderItems;
	private OrderShipping orderShipping;
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public OrderShipping getOrderShipping() {
		return orderShipping;
	}
	public void setOrderShipping(OrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
}

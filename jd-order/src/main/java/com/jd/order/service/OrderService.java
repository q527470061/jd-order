package com.jd.order.service;

import java.util.List;

import com.jd.pojo.JDResult;
import com.jd.pojo.Order;
import com.jd.pojo.OrderItem;
import com.jd.pojo.OrderShipping;

public interface OrderService {
	
	//创建订单
	JDResult createOrder(Order order, List<OrderItem> itemList, OrderShipping orderShipping);
	//获取订单列表
	JDResult getOrderList(long userId, Integer page, Integer rows);
	//删除订单
	JDResult deleteOrder(String orderId);
	// 修改订单状态
	// 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
	JDResult updateOrderStatus(String orderId, Integer status);
}

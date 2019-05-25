package com.jd.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jd.order.pojo.OrderCustom;
import com.jd.order.service.OrderService;
import com.jd.pojo.JDResult;
import com.jd.util.ExceptionUtil;

@RequestMapping("/order")
@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/create")
	public JDResult createOrder(@RequestBody OrderCustom orderCustom) {
		try {
			JDResult result = orderService.createOrder(orderCustom, orderCustom.getOrderItems(),
					orderCustom.getOrderShipping());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return JDResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	@RequestMapping("/list/{userId}/{page}/{rows}")
	public JDResult getOrderList(@PathVariable long userId, @PathVariable Integer page, @PathVariable Integer rows) {
		return orderService.getOrderList(userId, page, rows);
	}

	// 修改订单状态
	// 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
	@CrossOrigin("*")
	@RequestMapping("/update/status/{orderId}/{status}")
	public JDResult updateOrderStatus(@PathVariable String orderId, @PathVariable Integer status) {
		return orderService.updateOrderStatus(orderId, status);
	}

	@CrossOrigin("*")
	@RequestMapping("/delete/{orderId}")
	public JDResult deleteOrder(@PathVariable String orderId) {
		return orderService.deleteOrder(orderId);
	}
}

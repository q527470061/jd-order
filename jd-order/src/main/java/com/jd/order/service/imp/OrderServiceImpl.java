package com.jd.order.service.imp;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jd.mapper.OrderCustomMapper;
import com.jd.mapper.OrderItemMapper;
import com.jd.mapper.OrderMapper;
import com.jd.mapper.OrderShippingMapper;
import com.jd.order.dao.JedisClient;
import com.jd.order.pojo.OrderRestult;
import com.jd.pojo.OrderCustom;
import com.jd.pojo.OrderExample;
import com.jd.pojo.OrderExample.Criteria;
import com.jd.order.service.OrderService;
import com.jd.pojo.JDResult;
import com.jd.pojo.Order;
import com.jd.pojo.OrderItem;
import com.jd.pojo.OrderItemExample;
import com.jd.pojo.OrderShipping;
import com.jd.pojo.OrderShippingExample;
import com.jd.util.JsonUtils;

/**
 * 订单管理Service
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient jedisClient;

	@Value("${ORDER_KEY}")
	private String ORDER_KEY;
	@Value("${ORDER_INIT_ID}")
	private String ORDER_INIT_ID;
	@Value("${ORDER_ITEM_KEY}")
	private String ORDER_ITEM_KEY;

	@Override
	public JDResult createOrder(Order order, List<OrderItem> itemList, OrderShipping orderShipping) {
		/**
		 * 订单id的生成策略：使用redis中的incr,由于redis的单线程， 保证生成的id不会重复，且短可读性强
		 * 对于物流表的id，本方法的业务采用与订单id相同
		 * 
		 * @倚窗听雨
		 */
		// 插入订单表
		// 首先判断是否有order_key的 此key
		String string = jedisClient.get(ORDER_KEY);
		if (StringUtils.isBlank(string)) {
			jedisClient.set(ORDER_KEY, ORDER_INIT_ID);
		}
		long orderId = jedisClient.incr(ORDER_KEY);
		order.setOrderId(orderId + "");
		// 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		order.setStatus(1);
		Date date = new Date();
		order.setCreateTime(date);
		order.setUpdateTime(date);
		// 0：未评价 1：已评价
		order.setBuyerRate(0);
		orderMapper.insert(order);

		// 插入订单明细
		for (OrderItem orderItem : itemList) {
			long orderDetailId = jedisClient.incr(ORDER_ITEM_KEY);
			orderItem.setId(orderDetailId + "");
			orderItem.setOrderId(orderId + "");
			orderItemMapper.insert(orderItem);
		}

		// 插入物流表
		orderShipping.setOrderId(orderId + "");
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		orderShippingMapper.insert(orderShipping);

		return JDResult.ok(orderId);
	}

	// 按页查询
	@Override
	/*
	 * public JDResult getOrderList(long userId, Integer page, Integer rows) { //
	 * 获取订单与明细的管理查询 try { // 分页处理 if (page < 1) page = 1; PageHelper.startPage(page,
	 * 2); List<OrderCustom> orderCustoms =
	 * orderCustomMapper.getOrderCupstomByUserId(userId); OrderRestult result = new
	 * OrderRestult(); result.setOrderCustoms(orderCustoms); PageInfo<OrderCustom>
	 * pageInfo = new PageInfo<>(orderCustoms);
	 * result.setRecordCount(pageInfo.getTotal()); // 总页数
	 * result.setPageCount(pageInfo.getPages()); return JDResult.ok(result); } catch
	 * (Exception e) { e.printStackTrace(); return JDResult.build(500,
	 * e.getMessage()); } }
	 */

	public JDResult getOrderList(long userId, Integer page, Integer rows) {
		try {
			// 分页处理
			if (page < 1)
				page = 1;
			OrderExample example = new OrderExample();
			example.setOrderByClause("order_id desc");
			Criteria criteria = example.createCriteria();
			criteria.andUserIdEqualTo(userId);
			PageHelper.startPage(page, rows);
			List<Order> orders = orderMapper.selectByExample(example);
			List<OrderCustom> orderCustoms = JsonUtils.jsonToList(JsonUtils.objectToJson(orders), OrderCustom.class);
			for (OrderCustom orderCustom : orderCustoms) {
				OrderItemExample orderItemExample = new OrderItemExample();
				com.jd.pojo.OrderItemExample.Criteria criteria2 = orderItemExample.createCriteria();
				criteria2.andOrderIdEqualTo(orderCustom.getOrderId());
				List<OrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);
				orderCustom.setOrderItems(orderItems);
			}

			OrderRestult result = new OrderRestult();
			result.setOrderCustoms(orderCustoms);
			PageInfo<Order> pageInfo = new PageInfo<>(orders);
			result.setRecordCount(pageInfo.getTotal()); // 总页数
			result.setPageCount(pageInfo.getPages());
			result.setCurPage(page);
			return JDResult.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			return JDResult.build(500, e.getMessage());
		}
	}

	@Override
	public JDResult deleteOrder(String orderId) {
		/**
		 * 不想做了，不做假删除了
		 */
		try {
			// 删除订单
			orderMapper.deleteByPrimaryKey(orderId);
			// 刪除訂單相關的明细
			OrderItemExample orderItemExample = new OrderItemExample();
			com.jd.pojo.OrderItemExample.Criteria criteria = orderItemExample.createCriteria();
			criteria.andOrderIdEqualTo(orderId);
			orderItemMapper.deleteByExample(orderItemExample);
			// 删除订单相关物流
			OrderShippingExample orderShippingExample = new OrderShippingExample();
			com.jd.pojo.OrderShippingExample.Criteria criteria2 = orderShippingExample.createCriteria();
			criteria2.andOrderIdEqualTo(orderId);
			orderShippingMapper.deleteByExample(orderShippingExample);
			return JDResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return JDResult.build(500, e.getMessage());
		}
	}

	// 修改訂單狀態，咋老是繁体字呀
	@Override
	public JDResult updateOrderStatus(String orderId, Integer status) {
		try {
			Order order = orderMapper.selectByPrimaryKey(orderId);
			order.setStatus(status);
			// 更新狀態相关信息
			Date date = new Date();
			order.setUpdateTime(date);
			// 付款
			if (status == 2) {
				order.setPaymentTime(date);
			}
			// 发货
			if (status == 4) {
				order.setConsignTime(date);
			}
			// 交易成功
			if (status == 5) {
				order.setEndTime(date);
			}
			// 訂單取消，交易关闭
			if (status == 6) {
				order.setCloseTime(date);
			}
			orderMapper.updateByPrimaryKeySelective(order);
			return JDResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return JDResult.build(500, e.getMessage());
		}
	}

}

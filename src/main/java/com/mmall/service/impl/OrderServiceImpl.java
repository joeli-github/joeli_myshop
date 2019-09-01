package com.mmall.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmall.common.Const;
import com.mmall.dao.OrderItemMapper;
import com.mmall.dao.OrderMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Order;
import com.mmall.pojo.OrderItem;
import com.mmall.pojo.Product;
import com.mmall.service.IOrderService;
import com.mmall.util.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

@Service("iOrderService")
@Slf4j
public class OrderServiceImpl implements IOrderService{

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private ProductMapper productMapper;
	@Override
	public void closeOrder(int hour) {

		Date closeDataTime = DateUtils.addHours(new Date(), -hour);
		List<Order> orderList = orderMapper.selectOrderStatusByCreateTime(Const.OrderStatusEnum.NO_PAY.getCode(), DateTimeUtil.dateToStr(closeDataTime));
		for (Order order : orderList) {
			List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(order.getOrderNo());
			for (OrderItem orderItem : orderItemList) {
				//一定要用主键where条件，防止锁表。同时必须是支持MySQL的InnoDB.
				Integer stock = productMapper.selectStockByProductId(orderItem.getProductId());
				//考虑到已生成的订单里的商品，被删除的情况
				if (stock==null) {
					continue;
				}
				Product product = new Product();
				product.setId(orderItem.getProductId());
				product.setStock(stock+orderItem.getQuantity());
				productMapper.updateByPrimaryKeySelective(product);
			}
			orderMapper.closeOrderByOrderId(order.getId());
			log.info("关闭订单 OrderNo: {}",order.getOrderNo());
		}
	}

}

package cn.e3mall.order.service;

import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.untils.E3Result;

public interface OrderService {
	E3Result createOrder(OrderInfo orderInfo);
}

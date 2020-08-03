package com.fh.order.service;

import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {

    ServerResponse buildOrder(List<Cart> cartList, Integer payType, Integer addressId, HttpServletRequest request);

    ServerResponse queryMyOrder();
}

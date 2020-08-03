package com.fh.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.annotation.Idempotent;
import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;
import com.fh.order.service.OrderService;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("buildOrder")
    @Idempotent
    public ServerResponse buildOrder(String listStr, Integer payType,Integer addressId, HttpServletRequest request){
        List<Cart> cartList = new ArrayList<>();
        if (StringUtils.isNotEmpty(listStr)){
            //将前端传过来的json格式数据转为list
            cartList = JSONObject.parseArray(listStr, Cart.class);
        }else {
            return ServerResponse.error("请选择商品");
        }
        //下单
        return orderService.buildOrder(cartList,payType,addressId,request);
    }

    //查询我的订单
    @RequestMapping("queryMyOrder")
    public ServerResponse queryMyOrder(){
        return orderService.queryMyOrder();
    }


    @RequestMapping("getToken")
    public ServerResponse getToken(){
        String mtoken = UUID.randomUUID().toString();
        RedisUtil.set(mtoken,mtoken);
        return ServerResponse.success(mtoken);
    }
}

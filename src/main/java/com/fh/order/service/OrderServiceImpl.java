package com.fh.order.service;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.member.model.Member;
import com.fh.order.mapper.OrderInfoMapper;
import com.fh.order.mapper.OrderMapper;
import com.fh.order.model.Order;
import com.fh.order.model.OrderInfo;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.BigDecimalUtil;
import com.fh.util.IdUtil;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private ProductService productService;

    @Override
    public ServerResponse buildOrder(List<Cart> cartList, Integer payType, Integer addressId, HttpServletRequest request) {

        Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        String orderId = IdUtil.createId();
        //订单详情
        List<OrderInfo> orderInfoList = new ArrayList<>();
        //库存不足的集合
        List<String> stockNotFull  = new ArrayList<>();
        //商品总价格
        BigDecimal totalPrice = new BigDecimal("0.00");


        for (Cart cart : cartList) {
            Product product = productService.selectProductById(cart.getProductId());
            if (product.getStock() < cart.getCount()){
                //库存不足
                stockNotFull.add(cart.getName());
            }
            //判断库存是否充足 减库存
            Long res = productService.updateStoct(product.getId(),cart.getCount());
            if (res==1){
                //生成订单
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setName(cart.getName());
                orderInfo.setFilePath(cart.getFilePath());
                orderInfo.setPrice(cart.getPrice());
                orderInfo.setOrderId(orderId);
                orderInfo.setProductId(cart.getProductId());
                orderInfo.setCount(cart.getCount());
                orderInfoList.add(orderInfo);
                BigDecimal subTotal = BigDecimalUtil.mul(cart.getPrice().toString(), cart.getCount() + "");
               totalPrice = BigDecimalUtil.add(subTotal, totalPrice);
            }else {
                //库存不足
                stockNotFull.add(cart.getName());
            }
        }

        //生成订单 先判断是否有库存不足的商品
        if (orderInfoList !=null && orderInfoList.size() == cartList.size()){
            //库存都足时  保存订单详信息
            for (OrderInfo orderInfo : orderInfoList) {
                //保存到数据库
                orderInfoMapper.insert(orderInfo);
                //更新redis
                //Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
                String cartJson = RedisUtil.hget(SystemConstant.CART_KEY + member.getId(),orderInfo.getProductId().toString());
                if (StringUtils.isNotEmpty(cartJson)){
                    Cart cart1 = JSONObject.parseObject(cartJson, Cart.class);
                    if (cart1.getCount() <= orderInfo.getCount()){
                        //删除购物车中该商品
                        RedisUtil.hdel(SystemConstant.CART_KEY + member.getId(), orderInfo.getProductId().toString());
                    }else {
                        //更新购物车中商品
                        cart1.setCount(cart1.getCount()-orderInfo.getCount());
                        //转为json格式字符串
                        String s = JSONObject.toJSONString(cart1);
                        //更新redis    购物车
                        RedisUtil.hset(SystemConstant.CART_KEY + member.getId(),orderInfo.getProductId().toString(),s);
                    }
                }
            }
            //生成订单
            Order order = new Order();
            order.setCreateDate(new Date());
            order.setAddressId(addressId);
            order.setPayType(payType);
            order.setId(orderId);
            order.setTotalPrice(totalPrice);
            order.setMemberId(member.getId());
            order.setStatus(SystemConstant.ORDER_STATUS_WAIT);
            //添加到数据库
            orderMapper.insert(order);
            return ServerResponse.success(orderId);
        }else {
            return ServerResponse.error(stockNotFull);
        }
    }

    @Override
    public ServerResponse queryMyOrder() {
        List<Order> list = orderMapper.queryMyOrder();
        return ServerResponse.success(list);
    }


}

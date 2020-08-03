package com.fh.cart.controller;

import com.fh.annotation.Ignore;
import com.fh.cart.service.CartService;
import com.fh.common.ServerResponse;
import com.fh.member.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @RequestMapping("buy")
    public ServerResponse buy(Integer productId, Integer count, HttpServletRequest request){
        return cartService.buy(productId, count, request);
    }

    @RequestMapping("queryCartProductCount")
    @Ignore
    public ServerResponse queryCartProductCount(HttpServletRequest request){
        return cartService.queryCartProductCount(request);
    }

    @RequestMapping("queryList")
    public ServerResponse queryList(HttpServletRequest request){
        return cartService.queryList(request);
    }

    @RequestMapping("deleteProduct/{productId}")
    public ServerResponse deleteProduct(HttpServletRequest request, @PathVariable("productId") Integer productId){
        return cartService.deleteProduct(request,productId);
    }

    @RequestMapping("deleteProductBath")
    public ServerResponse deleteProductBath(HttpServletRequest request,@RequestParam("idList") List<Integer> list){
        return cartService.deleteProductBath(request,list);

    }

}

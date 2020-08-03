package com.fh.cart.service;

import com.fh.common.ServerResponse;
import com.fh.member.model.Member;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CartService {
    ServerResponse buy(Integer productId, Integer count, HttpServletRequest request);

    ServerResponse queryCartProductCount(HttpServletRequest request);

    ServerResponse queryList(HttpServletRequest request);

    ServerResponse deleteProduct(HttpServletRequest request, Integer productId);

    ServerResponse deleteProductBath(HttpServletRequest request, List<Integer> list);
}

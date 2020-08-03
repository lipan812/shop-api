package com.fh.product.controller;

import com.fh.annotation.Ignore;
import com.fh.common.ServerResponse;
import com.fh.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;
    //查询热销商品
    @RequestMapping("queryHotProductList")
    @Ignore
    public ServerResponse queryHotProductList(){
        return productService.queryHotProductList();
    }
    //查询所有商品
    @RequestMapping("queryProductList")
    @Ignore
    public ServerResponse queryProductList(){
        return productService.queryProductList();
    }
    @RequestMapping("queryProductListPage")
    @Ignore
    public ServerResponse queryProductListPage(long currentPage,long pageSize){
        return productService.queryProductListPage(currentPage,pageSize);
    }
}

package com.fh.product.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ServerResponse;
import com.fh.product.mapper.ProductMapper;
import com.fh.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse queryHotProductList() {
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.eq("isHot",1);
        List<Product> isHotProductList = productMapper.selectList(productQueryWrapper);
        return ServerResponse.success(isHotProductList);
    }

    @Override
    public ServerResponse queryProductList() {
        List<Product> productList = productMapper.selectList(null);
        return ServerResponse.success(productList);
    }

    @Override
    public ServerResponse queryProductListPage(long currentPage, long pageSize) {
        long start = (currentPage-1)*pageSize;
        //查询总条数
        long totalCount = productMapper.queryTotalCount();
        List<Product> list = productMapper.queryList(start,pageSize);
        long totalPage = totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
        Map map = new HashMap<>();
        map.put("list",list);
        map.put("totalPage",totalPage);
        return ServerResponse.success(map);
    }

    @Override
    public Product selectProductById(Integer productId) {
        Product selectById = productMapper.selectById(productId);
        return selectById;
    }

    @Override
    public Long updateStoct(Integer id, int count) {
        return productMapper.updateStoct(id,count);
    }
}

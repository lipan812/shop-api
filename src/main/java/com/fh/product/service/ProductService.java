package com.fh.product.service;

import com.fh.common.ServerResponse;
import com.fh.product.model.Product;

public interface ProductService {
    ServerResponse queryHotProductList();

    ServerResponse queryProductList();

    ServerResponse queryProductListPage(long currentPage, long pageSize);

    Product selectProductById(Integer productId);

    Long updateStoct(Integer id, int count);
}

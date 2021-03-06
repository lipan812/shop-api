package com.fh.category.controller;

import com.fh.annotation.Ignore;
import com.fh.common.ServerResponse;
import com.fh.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("queryList")
    @Ignore
    private ServerResponse queryList(){
        return categoryService.queryList();
    }
}

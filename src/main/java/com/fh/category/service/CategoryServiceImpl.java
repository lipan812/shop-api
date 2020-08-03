package com.fh.category.service;


import com.alibaba.fastjson.JSONArray;
import com.fh.common.ServerResponse;
import com.fh.category.mapper.CategoryMapper;
import com.fh.common.SystemConstant;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse queryList() {
        boolean exist = RedisUtil.exist(SystemConstant.CATEGORY_QUERYLIST);
        if (exist){
            String s = RedisUtil.get(SystemConstant.CATEGORY_QUERYLIST);
            List<Map> parseArray = JSONArray.parseArray(s, Map.class);
            return ServerResponse.success(parseArray);
        }
        List<Map<String ,Object>>  allList =   categoryMapper.queryList();
        List<Map<String ,Object>>  parentList = new ArrayList<Map<String, Object>>();
        for (Map  map : allList) {
            if(map.get("pid").equals(0)){
                parentList.add(map);
            }
        }
        selectChildren(parentList,allList);
        String jsonString = JSONArray.toJSONString(parentList);
        RedisUtil.set(SystemConstant.CATEGORY_QUERYLIST,jsonString);
        return ServerResponse.success(parentList);
    }

    public void selectChildren(List<Map<String ,Object>>  parentList, List<Map<String ,Object>>  allList){
        for (Map<String, Object> pmap : parentList) {
            List<Map<String ,Object>>  childrenList = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> amap : allList) {
                if(pmap.get("id").equals(amap.get("pid"))){
                    childrenList.add(amap);
                }
            }
            if(childrenList!=null && childrenList.size()>0){
                pmap.put("children",childrenList);
                selectChildren(childrenList,allList);
            }

        }
    }
}

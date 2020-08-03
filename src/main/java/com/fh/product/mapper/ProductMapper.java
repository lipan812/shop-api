package com.fh.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.product.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    @Select("SELECT COUNT(*) FROM t_product")
    long queryTotalCount();
    @Select("SELECT * FROM t_product ORDER BY id DESC LIMIT #{start},#{pageSize}")
    List<Product> queryList(@Param("start") long start, @Param("pageSize") long pageSize);

    Long updateStoct(@Param("id") Integer id,@Param("count") int count);
}

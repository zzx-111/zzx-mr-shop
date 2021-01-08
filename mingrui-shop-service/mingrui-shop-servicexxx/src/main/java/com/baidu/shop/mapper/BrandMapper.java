package com.baidu.shop.mapper;

import com.baidu.shop.entity.BrandEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper  extends Mapper<BrandEntity> {
    @Select(value = "select a.id,a.name from tb_brand a where a.id in (select brand_id from tb_category_brand  b where b.category_id=#{cid})")
    List<BrandEntity> getBrandNameByCid(Integer cid);
}

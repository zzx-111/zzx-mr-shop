package com.baidu.shop.mapper;

import com.baidu.shop.entity.CategoryBrandEntity;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * 2 * @ClassName CategoryBrandMapper
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2020/12/28
 * 6 * @Version V1.0
 * 7
 **/
public interface CategoryBrandMapper  extends Mapper<CategoryBrandEntity>, InsertListMapper<CategoryBrandEntity> {
}

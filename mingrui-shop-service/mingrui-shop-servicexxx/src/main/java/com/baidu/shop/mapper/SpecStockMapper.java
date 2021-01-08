package com.baidu.shop.mapper;

import com.baidu.shop.entity.SpecStockEntity;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface SpecStockMapper extends Mapper<SpecStockEntity>, DeleteByIdListMapper<SpecStockEntity,Long> {
}

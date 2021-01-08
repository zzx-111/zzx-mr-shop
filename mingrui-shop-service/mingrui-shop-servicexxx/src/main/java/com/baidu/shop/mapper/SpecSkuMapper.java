package com.baidu.shop.mapper;

import com.baidu.shop.dto.SpecSkuDTO;
import com.baidu.shop.entity.SpecSkuEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecSkuMapper extends Mapper<SpecSkuEntity>, DeleteByIdListMapper<SpecSkuEntity,Long> {
    @Select(value = "select sku.*,stock.stock from tb_sku sku,tb_stock stock where sku.id=stock.sku_id and sku.spu_id=#{spuId}")
    List<SpecSkuDTO> getSkuAndStockBySpuId(Integer spuId);
}

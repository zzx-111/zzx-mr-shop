package com.baidu.shop.dto;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 2 * @ClassName SpecStockEntity
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/1/7
 * 6 * @Version V1.0
 * 7
 **/

@Data
@ApiModel(value = "库存传输DTO")
public class SpecStockDTO {


    @ApiModelProperty(value = "库存对应的商品sku id")
    @NotNull(groups = MingruiOperation.update.class,message = "skuId不能为null")
    private Long skuId;
    @ApiModelProperty(value = "可秒杀库存")
    private Integer seckillStock;
    @ApiModelProperty(value = "秒杀总数量")
    private Integer seckillTotal;
    @ApiModelProperty(value = "库存数量")
    @NotEmpty(groups = {MingruiOperation.update.class,MingruiOperation.add.class},message = "库存不能为空")
    private Integer stock;
    



}

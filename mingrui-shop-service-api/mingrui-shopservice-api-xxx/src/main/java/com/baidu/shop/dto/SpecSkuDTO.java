package com.baidu.shop.dto;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 2 * @ClassName SpecSkuEntity
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/1/7
 * 6 * @Version V1.0
 * 7
 **/
@ApiModel(value = "商品sku传输DTO")
@Data
public class SpecSkuDTO extends BaseDTO {
    @ApiModelProperty(value = " sku id ")
    @NotNull(message = "sku id不能为空",groups = {MingruiOperation.update.class})
    private Long id;

    @ApiModelProperty(value = "商品spuId ")
    @NotNull(message = "商品spuId不能为空",groups = {MingruiOperation.update.class,MingruiOperation.add.class})
    private Integer spuId;

    @NotEmpty(message = "商品标题不能为空",groups = {MingruiOperation.add.class})
    @ApiModelProperty(value = " 商品标题 ")
    private String title;

    @NotEmpty(message = "商品的图片不能为空",groups = {MingruiOperation.add.class})
    @ApiModelProperty(value = " 商品的图片，多个图片以‘,’分割")
    private String images;

    @NotEmpty(message = "销售价格不能为空",groups = {MingruiOperation.add.class,MingruiOperation.update.class})
    @ApiModelProperty(value = "销售价格，单位为分")
    private Integer price;

    @NotEmpty(message = "特有规格属性在spu属性模板中的对应下标组合不能为空",groups = {MingruiOperation.add.class})
    @ApiModelProperty(value = " 特有规格属性在spu属性模板中的对应下标组合 ")
    private  String indexes;

    @NotEmpty(message = "sku的特有规格参数键值对不能为空",groups = {MingruiOperation.add.class})
    @ApiModelProperty(value = " sku的特有规格参数键值对，json格式，反序列化时请使用linkedHashMap，保证有序 ")
    private String ownSpec;

    @NotNull(message = "是否有效不能为空",groups = {MingruiOperation.add.class,MingruiOperation.update.class})
    @ApiModelProperty(value = " 是否有效，0无效，1有效 ")
    private Boolean enable;

    @ApiModelProperty(value = " 添加时间 ")
    private Date  createTime;

    @ApiModelProperty(value = " 最后修改时间 ")
    private Date lastUpdateTime;

    @NotNull(message = "库存剩余不能为空",groups = {MingruiOperation.add.class,MingruiOperation.update.class})
    @ApiModelProperty(value= "库存剩余")
    private Integer stock;
}

package com.baidu.shop.dto;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 2 * @ClassName GoodsEntity
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/1/5
 * 6 * @Version V1.0
 * 7
 **/
@ApiModel(value = "商品参数DTO")
@Data
public class GoodsDTO  extends  BaseDTO{
    @ApiModelProperty(value = "spuId")
    @NotNull(groups = {MingruiOperation.update.class})
    private Integer id;

    @ApiModelProperty(value = "标题")
    @NotEmpty(groups = {MingruiOperation.update.class,MingruiOperation.add.class})
    private String title;

    @ApiModelProperty(value = "子标题")
    private String subTitle;

    @ApiModelProperty(value = "一级类目id")
    @NotNull(groups = {MingruiOperation.update.class,MingruiOperation.add.class})
    private Integer cid1;

    @ApiModelProperty(value = "二级类目id")
    @NotNull(groups = {MingruiOperation.update.class,MingruiOperation.add.class})
    private Integer cid2;

    @ApiModelProperty(value = "三级类目id")
    @NotNull(groups = {MingruiOperation.update.class,MingruiOperation.add.class})
    private Integer cid3;

    @ApiModelProperty(value = "商品所属品牌id")
    @NotNull(groups = {MingruiOperation.update.class,MingruiOperation.add.class})
    private Integer brandId;

    @ApiModelProperty(value = "是否上架，0下架，1上架")
    @NotNull(groups = {MingruiOperation.update.class,MingruiOperation.add.class})
    private Integer saleable;

    @ApiModelProperty(value = "是否有效，0已删除，1有效")
    @NotNull(groups = {MingruiOperation.update.class,MingruiOperation.add.class})
    private Integer valid;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改时间")
    private Date lostUpdateTime;
}

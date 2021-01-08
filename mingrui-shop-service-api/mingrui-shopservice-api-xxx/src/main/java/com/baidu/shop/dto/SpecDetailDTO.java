package com.baidu.shop.dto;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 2 * @ClassName SpecDetailEntity
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/1/7
 * 6 * @Version V1.0
 * 7
 **/

@Data
@ApiModel(value = "商品标题传输DTO")
public class SpecDetailDTO extends BaseDTO {
    @ApiModelProperty(value = "商品spuId")
    @NotNull(message = "商品spuId不能为空",groups = {MingruiOperation.update.class})
    private Integer spuId;

    @ApiModelProperty(value = "商品描述信息")
    @NotEmpty(message = "通用规格参数数据不能为空",groups = {MingruiOperation.add.class})
    private String description;

    @ApiModelProperty(value = "通用规格参数数据")
    @NotEmpty(message = "通用规格参数数据不能为空",groups = {MingruiOperation.add.class})
    private String genericSpec;

    @ApiModelProperty(value = "特有规格参数及可选值信息，json格式")
    @NotEmpty(message = "特有规格参数及可选值信息不能为空",groups = {MingruiOperation.add.class})
    private String specialSpec;

    @ApiModelProperty(value = "包装清单")
    @NotEmpty(message = "包装清单不能为空",groups = {MingruiOperation.add.class})
    private  String packingList;

    @ApiModelProperty(value = "售后服务")
    @NotEmpty(message = "售后服务不能为空",groups = {MingruiOperation.add.class})
    private String afterService;

}

package com.baidu.shop.dto;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 2 * @ClassName BrandEntity
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2020/12/25
 * 6 * @Version V1.0
 * 7
 **/

@ApiModel(value = "品牌管理实体类")
@Data
public class BrandDTO  extends BaseDTO {
    @ApiModelProperty(value = "品牌管理id")
    @NotNull(groups = {MingruiOperation.update.class},message = "品牌id不能为空")
    private Integer id;
    @ApiModelProperty(value = "品牌管理名称")
    @NotEmpty(groups = {MingruiOperation.update.class,MingruiOperation.add.class},message = "品牌管理名称不能为空")
    private String name;
    @ApiModelProperty(value = "品牌管理图片")
    private String image;
    @ApiModelProperty(value = "品牌管理排序字段")
    private String letter;
    @ApiModelProperty(value = "属于那个分类下的品牌")
    private String category;

}

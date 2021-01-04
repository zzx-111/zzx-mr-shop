package com.baidu.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    private Integer id;
    @ApiModelProperty(value = "品牌管理名称")
    private String name;
    @ApiModelProperty(value = "品牌管理图片")
    private String image;
    @ApiModelProperty(value = "品牌管理排序字段")
    private String letter;
    @ApiModelProperty(value = "属于那个分类下的品牌")
    private String category;

}

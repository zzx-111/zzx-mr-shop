package com.baidu.shop.dto;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 2 * @ClassName SpecParamDTO
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/1/4
 * 6 * @Version V1.0
 * 7
 **/
@ApiModel(value = "反规格参数传输DTO")
@Data
public class SpecParamDTO {
    @ApiModelProperty(value = "主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.update.class})
    private Integer id;

    @ApiModelProperty(value = "分类id",example = "1")
    @NotNull(message = "分类id不能为空",groups = {MingruiOperation.update.class})
    private Integer cid;

    @ApiModelProperty(value = "规格组id",example = "1")
    @NotNull(message = "规格组id不能为空",groups = {MingruiOperation.update.class})
    private Integer groupId;

    @ApiModelProperty(value = "规格组参数名称",example = "1")
    @NotEmpty(message = "规格组参数名称不能为空",groups = {MingruiOperation.add.class})
    private String name;

    @ApiModelProperty(value = "是否是数字类型参数，1->true或0->false",example = "0")
    @NotNull(message = "是否是数字类型参数不能为空",groups = {MingruiOperation.add.class,MingruiOperation.update.class})
    private Boolean numeric;

    @ApiModelProperty(value = "数字类型参数的单位，非数字类型可以为空",example = "1")
    @NotEmpty(message = "数字类型参数的单位不能为空",groups = {MingruiOperation.add.class})
    private String unit;

    @ApiModelProperty(value = "是否是sku通用属性，1->true或0->false",example = "0")
    @NotNull(message = "是否是sku通用属性不能为空",groups = {MingruiOperation.add.class,MingruiOperation.update.class})
    private Boolean generic;

    @ApiModelProperty(value = "是否用于搜索过滤，true或false",example = "0")
    @NotNull(message = "是否用于搜索过滤不能为空",groups = {MingruiOperation.add.class,MingruiOperation.update.class})
    private Boolean searching;

    @ApiModelProperty(value = "数值类型参数",example = "1")
    @NotEmpty(message = "数值类型参数不能为空",groups = {MingruiOperation.add.class})
    private String segments;
}

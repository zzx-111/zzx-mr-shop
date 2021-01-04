package com.baidu.shop.entity;

import com.baidu.shop.dto.BaseDTO;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 2 * @ClassName SpecGroupEntity
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/1/4
 * 6 * @Version V1.0
 * 7
 **/

@Data
@ApiModel(value = "规格分组参数组")
public class SpecGroupDTO extends BaseDTO {

    @ApiModelProperty(value = "规格分组id")
    @NotNull(message = "当前分组id不能为空" ,groups = {MingruiOperation.update.class} )
    private Integer id;
    @ApiModelProperty(value = "分类id")
    @NotNull(message = "当前分类id不能为空" ,groups = {MingruiOperation.update.class,MingruiOperation.add.class} )
    private Integer cid;
    @NotNull(message = "当前name不能为空" ,groups = {MingruiOperation.update.class,MingruiOperation.add.class} )
    @ApiModelProperty(value ="规格分组名称")
    private String name;
}

package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 2 * @ClassName CategoryEntity
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2020/12/22
 * 6 * @Version V1.0
 * 7
 **/
@Data
@ApiModel( value = "分类实体类")
@Table(name = "tb_category")
public class CategoryEntity {
    @Id
    @Column(name ="id",length = 255)
    @ApiModelProperty(value = "类目id")
    @NotNull(message = "当前类目id不能为null",groups = {MingruiOperation.update.class})
    private Integer id;
    @ApiModelProperty(value = "类目名称")
    @Column(name="name",length = 255)
    @NotEmpty(message = "当前类目名称格式不正确或者为null",groups = {MingruiOperation.add.class,MingruiOperation.update.class})
    private String name;
    @ApiModelProperty(value = "父类目id,顶级类目填0")
    @Column(name="parent_id",length = 255)
    @NotNull(message = "当前父类目id不能为null",groups = {MingruiOperation.add.class})
    private Integer parentId;
    @ApiModelProperty(value ="是否为父节点，0为否，1为是")
    @Column(name="is_parent",length = 255)
    @NotNull(message = "当前父节点不能为null",groups = {MingruiOperation.add.class})
    private Integer isParent;
    @Column(name="sort",length = 255)
    @ApiModelProperty(value ="排序指数，越小越靠前")
    @NotNull(message = "当前排序字段不能为null",groups = {MingruiOperation.add.class})
    private Integer sort;

}

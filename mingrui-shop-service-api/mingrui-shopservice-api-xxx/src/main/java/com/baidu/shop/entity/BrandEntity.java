package com.baidu.shop.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;

/**
 * 2 * @ClassName BrandEntity
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2020/12/25
 * 6 * @Version V1.0
 * 7
 **/
@Table(name = "tb_brand")
@ApiModel(value = "品牌管理实体类")
@Data
public class BrandEntity {
    @Id
    @Column(name = "id",length = 255)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name",length = 255)
    private String name;
    @Column(name = "image",length = 255)
    private String image;
    @Column(name ="letter",length = 255)
    private String letter;

}

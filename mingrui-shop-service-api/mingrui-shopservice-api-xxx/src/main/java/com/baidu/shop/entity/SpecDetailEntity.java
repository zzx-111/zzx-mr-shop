package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 2 * @ClassName SpecDetailEntity
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/1/7
 * 6 * @Version V1.0
 * 7
 **/
@Table(name = "tb_spu_detail")
@Data
public class SpecDetailEntity {
    @Id
    private Integer spuId;

    private String description;

    private String genericSpec;

    private String specialSpec;

    private  String packingList;

    private String afterService;

}

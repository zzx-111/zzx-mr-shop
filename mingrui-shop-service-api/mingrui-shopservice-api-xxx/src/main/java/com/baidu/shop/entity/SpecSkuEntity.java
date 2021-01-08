package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 2 * @ClassName SpecSkuEntity
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/1/7
 * 6 * @Version V1.0
 * 7
 **/
@Table(name ="tb_sku")
@Data
public class SpecSkuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer spuId;

    private String title;

    private String images;

    private Integer price;

    private  String indexes;

    private String ownSpec;

    private Boolean enable;

    private Date  createTime;

    private Date lastUpdateTime;
}

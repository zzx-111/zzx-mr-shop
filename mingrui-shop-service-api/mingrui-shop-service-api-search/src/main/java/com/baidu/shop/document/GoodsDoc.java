package com.baidu.shop.document;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 2 * @ClassName GoodsDoc
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/4
 * 6 * @Version V1.0
 * 7
 **/
@Document(indexName = "goods",shards = 1,replicas = 0)
@Data
public class GoodsDoc {
    @Id
    private Long id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String brandName;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String categoryName;
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;
    private Long brandId;
    private Long cid1;
    private Long cid2;
    private Long cid3;
    private Date createTime;
    private List<Long> price;
    @Field(type = FieldType.Keyword, index = false)
    private String skus;
    //规格
    private Map<String, Object> specs;
}

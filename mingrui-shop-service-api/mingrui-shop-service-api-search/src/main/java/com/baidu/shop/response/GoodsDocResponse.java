package com.baidu.shop.response;

import com.baidu.shop.base.Result;
import com.baidu.shop.document.GoodsDoc;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.status.HTTPStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 2 * @ClassName GoodsDocResponse
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/6
 * 6 * @Version V1.0
 * 7
 **/
@Data
@NoArgsConstructor
public class GoodsDocResponse extends Result<List<GoodsDoc>> {

    private Long total;

    private Long totalPage;


    private List<CategoryEntity> categoryEntityList;

    private List<BrandEntity> brandEntityList;

    private  Map<String, List<String>>  specAggMap;

    public GoodsDocResponse(Long total, Long totalPage, List<CategoryEntity> categoryEntityList, List<BrandEntity> brandEntityList, List<GoodsDoc> goodsDocs, Map<String, List<String>> map){
        super(HTTPStatus.OK,"",goodsDocs);
        this.total=total;
        this.totalPage=totalPage;
        this.categoryEntityList=categoryEntityList;
        this.brandEntityList=brandEntityList;
        this.specAggMap=map;

    }
}

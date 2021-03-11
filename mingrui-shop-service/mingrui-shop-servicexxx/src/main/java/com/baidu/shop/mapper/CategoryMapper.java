package com.baidu.shop.mapper;

import com.baidu.shop.entity.CategoryEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<CategoryEntity>, SelectByIdListMapper<CategoryEntity,Integer> {
    @Select(value = "select id,name from tb_category where id in (select category_id from tb_category_brand where brand_id=#{brandId})")
    List<CategoryEntity> getBrandByBrandId(Integer brandId);

    @Select(value="<script>"+"  select group_concat(name Separator '/') from tb_category where id in" +
            "<foreach item='item' index='index' collection='asList' open='(' separator=',' close=')'>" +
            "            #{item}" +
            "            </foreach>  "+"</script>")
    String selectCategoryNameById(@Param("asList") List<Integer> asList);







}

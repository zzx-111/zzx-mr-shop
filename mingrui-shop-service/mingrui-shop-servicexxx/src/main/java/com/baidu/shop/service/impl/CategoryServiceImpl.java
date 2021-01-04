package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.service.CategoryService;
import com.baidu.shop.utils.ObjectEqUtil;
import com.google.gson.JsonObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 2 * @ClassName CategoryServiceImpl
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2020/12/22
 * 6 * @Version V1.0
 * 7
 **/
@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private CategoryBrandMapper categoryBrandMapper;



    @Override
    public Result<List<CategoryEntity>> getCategoryByPid(Integer pid) {

        CategoryEntity entity = new CategoryEntity();
        entity.setParentId(pid);

        return this.setResultSuccess(categoryMapper.select(entity));
    }

    @Transactional
    @Override
    public Result<JSONObject> deleteCategoryById(Integer id) {

        if(ObjectEqUtil.isNull(id) || id <0){
            return  this.setResultError("删除失败当前数据不存在");
        }

        //根据分类id查询分类品牌关系表,判断是否有数据绑定.有就不能被删除.没有删除.
        Example categoryBrandExample = new Example(CategoryBrandEntity.class);
        categoryBrandExample.createCriteria().andEqualTo("categoryId",id);
        List<CategoryBrandEntity> categoryBrandList = categoryBrandMapper.selectByExample(categoryBrandExample);


        if(categoryBrandList.size()>0) return this.setResultError("当前分类下有品牌无法被删除");
        //根据当前id查询出当前数据
        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(id);
        if(ObjectEqUtil.isNull(categoryEntity)){

            if(ObjectEqUtil.isNotNull(categoryEntity.getParentId())){

                //如果当前节点是父节点的话不能被删除
                if(categoryEntity.getParentId()==1) return this.setResultError("当前节点为父级节点不能被删除");


                //通过当前节点的parentId查询当前parentId下是否还有其他节点.如果存在其他节点就不改变父级节点的状态

                Example example = new Example(CategoryEntity.class);
               example.createCriteria().andEqualTo("parentId", categoryEntity.getParentId());

                List<CategoryEntity> categoryList = categoryMapper.selectByExample(example);
                //如果当前父级节点下没有其他节点就修改当前节点下父级节点的状态
                if(categoryList.size() <=1) {
                    CategoryEntity categoryEntity2 = new CategoryEntity();
                    categoryEntity2.setIsParent(0);
                    categoryEntity2.setId(categoryEntity.getParentId());
                    categoryMapper.updateByPrimaryKeySelective(categoryEntity2);
                }


            }
        }
        categoryMapper.deleteByPrimaryKey(id);

        return this.setResultSuccess("删除成功");
    }
    @Transactional
    @Override
    public Result<JsonObject> editCategoryById(CategoryEntity categoryEntity) {

        try {
            categoryMapper.updateByPrimaryKeySelective(categoryEntity);
        } catch (Exception e) {
            this.setResultError(e.getMessage());
            e.printStackTrace();
        }
        return this.setResultSuccess("更新成功");
    }
    @Transactional
    @Override
    public Result<JsonObject> saveCategoryById(CategoryEntity categoryEntity) {
        if(null!=categoryEntity){
            if(categoryEntity.getIsParent()!=null){
                if(categoryEntity.getParentId()!=1){
                    CategoryEntity entity = new CategoryEntity();
                    entity.setId(categoryEntity.getParentId());
                    entity.setIsParent(1);
                    categoryMapper.updateByPrimaryKeySelective(entity);
                }
            }
        }
        this.categoryMapper.insertSelective(categoryEntity);
        return this.setResultSuccess("新增成功");
    }

    @Override
    public Result<List<CategoryEntity>> getBrandByBrandId(Integer brandId) {
        List<CategoryEntity> list=categoryMapper.getBrandByBrandId(brandId);
        return this.setResultSuccess(list);
    }


}

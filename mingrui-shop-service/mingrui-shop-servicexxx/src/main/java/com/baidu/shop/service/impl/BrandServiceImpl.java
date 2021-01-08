package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.mapper.BrandMapper;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.service.BrandService;
import com.baidu.shop.utils.ObjectEqUtil;
import com.baidu.shop.utils.PinyinUtil;
import com.baidu.shop.utils.TenXunBeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 2 * @ClassName BrandServiceImpl
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2020/12/25
 * 6 * @Version V1.0
 * 7
 **/
@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {

    @Resource
    private BrandMapper brandMapper;
    @Resource
    private CategoryBrandMapper categoryBrandMapper;

    @Override
    public Result<List<BrandEntity>> getBrandList(BrandDTO brandDTO) {
        //分页
        PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());
        //根据 sort字段判断是否需要排序.
        if(!StringUtils.isEmpty(brandDTO.getSort())){
            PageHelper.orderBy(brandDTO.getSort()+"  "+(Boolean.valueOf(brandDTO.getOrder())?"desc":"asc"));
        }

        //模糊查询
        Example example = new Example(BrandEntity.class);

        //BeanUtils.copyProperties(目标对象,要复制的对象);
        example.createCriteria().andLike("name","%"+brandDTO.getName()+"%");
        //查询所有数据
        List<BrandEntity> list = brandMapper.selectByExample(example);
        //分页总条数
        PageInfo<BrandEntity> objectPageInfo = new PageInfo<>(list);
        return this.setResultSuccess(objectPageInfo);
    }
    @Transactional
    @Override
    public Result<JsonObject> saveBrand(BrandDTO brandDTO) {

        if(ObjectEqUtil.isNull(brandDTO)){
            return this.setResultError("数据不能为空");

        }
        if(StringUtils.isEmpty(brandDTO.getCategory())){
            return this.setResultError("数据不能为空");
        }
        //根据 name 获取letter首字母
        brandDTO.setLetter(String.valueOf(PinyinUtil.getUpperCase(String.valueOf(brandDTO.getName().toCharArray()[0]),false).toCharArray()[0]));
        //拷贝数据到brandEntity
        BrandEntity brandEntity = TenXunBeanUtil.copyProperties(brandDTO, BrandEntity.class);
        brandMapper.insert(brandEntity);

        this.insertListCategoryIdByBrand(brandDTO,brandEntity);


        return this.setResultSuccess();
    }

    @Override
    public Result<JsonObject> editBrand(BrandDTO brandDTO) {
        BrandEntity brandEntity = TenXunBeanUtil.copyProperties(brandDTO, BrandEntity.class);
        brandDTO.setLetter(String.valueOf(PinyinUtil.getUpperCase(String.valueOf(brandDTO.getName().toCharArray()[0]),false).toCharArray()[0]));
        brandMapper.updateByPrimaryKeySelective(brandEntity);
        //根据brandId删除中间表数据
       this.deleteCategoryBrandByBrandId(brandDTO.getId());
        this.insertListCategoryIdByBrand(brandDTO,brandEntity);

        return this.setResultSuccess();
    }

    @Override
    public Result<JsonObject> deleteBrand(Integer id) {
        //根据id删除品牌
        brandMapper.deleteByPrimaryKey(id);
        //根据品牌删除中间表到数据
        this.deleteCategoryBrandByBrandId(id);

        return this.setResultSuccess();
    }

    @Override
    public Result<List<BrandEntity>> getBrandNameByCid(Integer cid) {
        List<BrandEntity> list=brandMapper.getBrandNameByCid(cid);
        return this.setResultSuccess(list);
    }

    private void deleteCategoryBrandByBrandId(Integer id){
        Example example = new Example(CategoryBrandEntity.class);
        example.createCriteria().andEqualTo("brandId",id);
        categoryBrandMapper.deleteByExample(example);
    }

    private void insertListCategoryIdByBrand(BrandDTO brandDTO,BrandEntity brandEntity){
        if(brandDTO.getCategory().contains(",")){
            String[] strings = brandDTO.getCategory().split(",");
            List<CategoryBrandEntity> categoryBrandList = Arrays.asList(strings).stream().map(c ->  new CategoryBrandEntity(Integer.parseInt(c),brandEntity.getId())).collect(Collectors.toList());

            categoryBrandMapper.insertList(categoryBrandList);
        }else{
            CategoryBrandEntity categoryBrandEntity=new CategoryBrandEntity();
            categoryBrandEntity.setCategoryId(Integer.parseInt(brandDTO.getCategory()));
            categoryBrandEntity.setBrandId(brandEntity.getId());
            categoryBrandMapper.insertSelective(categoryBrandEntity);
        }
    }
}

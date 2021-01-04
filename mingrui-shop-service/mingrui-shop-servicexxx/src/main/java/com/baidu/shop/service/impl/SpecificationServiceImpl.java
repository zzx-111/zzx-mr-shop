package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.SpecGroupDTO;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.mapper.SpecGroupMapper;
import com.baidu.shop.service.SpecificationService;
import com.baidu.shop.utils.ObjectEqUtil;
import com.baidu.shop.utils.TenXunBeanUtil;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 2 * @ClassName SpecificationServiceImpl
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/1/4
 * 6 * @Version V1.0
 * 7
 **/
@RestController
public class SpecificationServiceImpl extends BaseApiService implements SpecificationService {

    @Resource
    private SpecGroupMapper specGroupMapper;
    //规格分组查询
    @Override
    public Result<List<SpecGroupEntity>> getSpecList(SpecGroupDTO specGroupDTO) {
        //判断规格分组id是否为空
        if(ObjectEqUtil.isNull(specGroupDTO.getCid())) return  this.setResultError("参数错误");

        Example example = new Example(SpecGroupEntity.class);
        example.createCriteria().andEqualTo("cid", TenXunBeanUtil.copyProperties(specGroupDTO,SpecGroupEntity.class).getCid());
        List<SpecGroupEntity> list = specGroupMapper.selectByExample(example);

        return this.setResultSuccess(list);
    }
    //分组新增
    @Override
    public Result<JsonObject> saveSpec(SpecGroupDTO specGroupDTO) {
        specGroupMapper.insertSelective(TenXunBeanUtil.copyProperties(specGroupDTO,SpecGroupEntity.class));
        return this.setResultSuccess();
    }
    //分组修改
    @Override
    public Result<JsonObject> editSpec(SpecGroupDTO specGroupDTO) {

        SpecGroupEntity specGroupEntity = TenXunBeanUtil.copyProperties(specGroupDTO, SpecGroupEntity.class);
        System.out.println(specGroupEntity);
        specGroupMapper.updateByPrimaryKeySelective(specGroupEntity);
        return this.setResultSuccess();
    }
    //分组删除
    @Override
    public Result<JsonObject> deleteSpec(Integer id) {
        specGroupMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }
}

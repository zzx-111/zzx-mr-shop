package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.GoodsDTO;
import com.baidu.shop.entity.GoodsEntity;
import com.baidu.shop.mapper.GoodsMapper;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.utils.ObjectEqUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 2 * @ClassName GoodsServiceImpl
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/1/5
 * 6 * @Version V1.0
 * 7
 **/
@RestController
public class GoodsServiceImpl extends BaseApiService implements GoodsService {
    @Resource
    private GoodsMapper goodsMapper;


    @Override
    public Result<List<GoodsEntity>> getGoodsList(GoodsDTO goodsDTO) {

        if(ObjectEqUtil.isNull(goodsDTO.getPage())|| ObjectEqUtil.isNull(goodsDTO.getRows())) return this.setResultError("分页参数错误");

        PageHelper.startPage(goodsDTO.getPage(),goodsDTO.getRows());
        Example example = new Example(GoodsEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(ObjectEqUtil.isNotNull(goodsDTO.getSaleable()) && goodsDTO.getSaleable() <2){
            criteria.andEqualTo("saleable",goodsDTO.getSaleable());
        }
        if(StringUtils.isEmpty(goodsDTO.getTitle())){
            criteria.andLike("title","%"+goodsDTO.getTitle()+"%");
        }
        List<GoodsEntity> goodsEntities = goodsMapper.selectByExample(example);
        PageInfo<GoodsEntity> pageInfo =new PageInfo<>(goodsEntities);
        return this.setResultSuccess(pageInfo);
    }
}

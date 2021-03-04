package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.GoodsDTO;
import com.baidu.shop.dto.SpecDetailDTO;
import com.baidu.shop.dto.SpecSkuDTO;
import com.baidu.shop.entity.*;
import com.baidu.shop.mapper.*;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.ObjectEqUtil;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private BrandMapper brandMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SpecSkuMapper specSkuMapper;


    @Resource
    private SpecStockMapper specStockMapper;

    @Resource
    private  SpecDetailMapper specDetailMapper;


    @Override
    public Result<List<GoodsDTO>> getGoodsList(GoodsDTO goodsDTO) {

        //判断分页参数是否正确
        if(ObjectEqUtil.isNull(goodsDTO.getPage())|| ObjectEqUtil.isNull(goodsDTO.getRows())) return this.setResultError("分页参数错误");
        PageHelper.startPage(goodsDTO.getPage(),goodsDTO.getRows());
        if(!StringUtils.isEmpty(goodsDTO.getSort()) && !StringUtils.isEmpty(goodsDTO.getOrder())){
                PageHelper.orderBy(goodsDTO.getSort()+"  "+(Boolean.valueOf(goodsDTO.getOrder())?"desc":"asc"));
        }
        //创建example对象进行条件查询
        Example example = new Example(GoodsEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(ObjectEqUtil.isNotNull(goodsDTO.getSaleable()) && goodsDTO.getSaleable() <2){
            criteria.andEqualTo("saleable",goodsDTO.getSaleable());
        }
        if(!StringUtils.isEmpty(goodsDTO.getTitle())){
            criteria.andLike("title","%"+goodsDTO.getTitle()+"%");
        }




        List<GoodsEntity> goodsEntities = goodsMapper.selectByExample(example);


        List<GoodsDTO> collect = goodsEntities.stream().map(goodsEntity -> {
            //根据分类id查询分类名称
            String categroyName=categoryMapper.selectCategoryNameById(Arrays.asList(goodsEntity.getCid1(), goodsEntity.getCid2(), goodsEntity.getCid3()));

            GoodsDTO goodsDTO1 = TenXunBeanUtil.copyProperties(goodsEntity, GoodsDTO.class);
            //根据品牌id查询品牌名称
            BrandEntity brandEntity = brandMapper.selectByPrimaryKey(goodsEntity.getBrandId());
            goodsDTO1.setBrandName(brandEntity.getName());
            goodsDTO1.setCategoryName(categroyName);

            return goodsDTO1;
        }).collect(Collectors.toList());

        PageInfo<GoodsEntity> pageInfo =new PageInfo<>(goodsEntities);

        return this.setResult(HTTPStatus.OK,pageInfo.getTotal()+"",collect);
    }

    @Override
    @Transactional
    public Result<JsonObject> saveGoods(GoodsDTO goodsDTO) {

        final Date date = new Date();
        GoodsEntity spuEntity = TenXunBeanUtil.copyProperties(goodsDTO, GoodsEntity.class);
        spuEntity.setSaleable(1);
        spuEntity.setValid(1);
        spuEntity.setCreateTime(date);
        spuEntity.setLastUpdateTime(date);
        //新增spu数据
        goodsMapper.insertSelective(spuEntity);


        SpecDetailDTO specDetailDTO = goodsDTO.getSpuDetail();
        SpecDetailEntity specDetailEntity = TenXunBeanUtil.copyProperties(specDetailDTO, SpecDetailEntity.class);
        specDetailEntity.setSpuId(spuEntity.getId());
            //新增detail
        specDetailMapper.insertSelective(specDetailEntity);


        List<SpecSkuDTO> specSkuDTOS = goodsDTO.getSkus();
        specSkuDTOS.stream().forEach(sku ->{
            SpecSkuEntity specSkuEntity = TenXunBeanUtil.copyProperties(sku, SpecSkuEntity.class);
            specSkuEntity.setSpuId(spuEntity.getId());
            specSkuEntity.setCreateTime(date);
            specSkuEntity.setLastUpdateTime(date);
            //新增sku
            specSkuMapper.insertSelective(specSkuEntity);
            SpecStockEntity specStockEntity = new SpecStockEntity();
            specStockEntity.setSkuId(specSkuEntity.getId());
            specStockEntity.setStock(sku.getStock());
            //新增stock
            specStockMapper.insertSelective(specStockEntity);
        });

        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JsonObject> deleteGoods(Integer spuId) {
        //根据id删除spu数据
        goodsMapper.deleteByPrimaryKey(spuId);
        //根据spuId删除detail数据
        specDetailMapper.deleteByPrimaryKey(spuId);
        //根据spuId查询出skuId通过skuId删除sku信息和stock信息
        deleteSkuAndStockBySpuId(spuId);
        return this.setResultSuccess();
    }

    @Override
    @Transactional
    public Result<JsonObject> editGoods(GoodsDTO goodsDTO) {
        final Date date = new Date();
        //通过id删除spu数据
        GoodsEntity goodsEntity = TenXunBeanUtil.copyProperties(goodsDTO, GoodsEntity.class);
        goodsEntity.setLastUpdateTime(date);
        goodsMapper.updateByPrimaryKeySelective(goodsEntity);
        //通过spuId删除detail中的数据
        SpecDetailEntity specDetailEntity = TenXunBeanUtil.copyProperties(goodsDTO.getSpuDetail(), SpecDetailEntity.class);
        specDetailMapper.updateByPrimaryKeySelective(specDetailEntity);
        ////根据spuId查询出skuId通过skuId删除sku信息和stock信息
        deleteSkuAndStockBySpuId(goodsDTO.getId());
        //通过前台传来的数据新增sku列表以及库存
        saveSkuAndStockFunction(goodsDTO,goodsDTO.getId(),date);

        return this.setResultSuccess();
    }

    @Override
    public Result<JsonObject> xiaJiaGoods(GoodsDTO goodsDTO) {
        GoodsEntity goodsEntity = TenXunBeanUtil.copyProperties(goodsDTO, GoodsEntity.class);
        goodsMapper.updateByPrimaryKeySelective(goodsEntity);
        return this.setResultSuccess();
    }

    @Override
    public Result<SpecDetailDTO> getSpecDetailBySpuId(Integer spuId) {
        SpecDetailEntity specDetailEntity = specDetailMapper.selectByPrimaryKey(spuId);
        return this.setResultSuccess(specDetailEntity);
    }

    @Override
    public Result<List<SpecSkuDTO>> getSkuBySpu(Integer spuId) {
       List<SpecSkuDTO> list= specSkuMapper.getSkuAndStockBySpuId(spuId);
        return this.setResultSuccess(list);
    }

    private  void saveSkuAndStockFunction(GoodsDTO goodsDTO,Integer spuId, Date date){
        List<SpecSkuDTO> specSkuDTOS = goodsDTO.getSkus();
        specSkuDTOS.stream().forEach(sku ->{
            SpecSkuEntity specSkuEntity = TenXunBeanUtil.copyProperties(sku, SpecSkuEntity.class);
            specSkuEntity.setSpuId(spuId);
            specSkuEntity.setCreateTime(date);
            specSkuEntity.setLastUpdateTime(date);
            //新增sku
            specSkuMapper.insertSelective(specSkuEntity);
            SpecStockEntity specStockEntity = new SpecStockEntity();
            specStockEntity.setSkuId(specSkuEntity.getId());
            specStockEntity.setStock(sku.getStock());
            //新增stock
            specStockMapper.insertSelective(specStockEntity);
        });
    }

    private void deleteSkuAndStockBySpuId(Integer spuId){
        //根据spuId查询sku信息.
        Example example = new Example(SpecSkuEntity.class);
        example.createCriteria().andEqualTo("spuId",spuId);
        List<SpecSkuEntity> specSkuEntityList = specSkuMapper.selectByExample(example);
        //获取skuId列表
        List<Long> longList = specSkuEntityList.stream().map(sku -> sku.getId()).collect(Collectors.toList());
        //通过skuId列表删除sku数据
        specSkuMapper.deleteByIdList(longList);
        //通过skuId列表删除stock数据
        specStockMapper.deleteByIdList(longList);
    }
}

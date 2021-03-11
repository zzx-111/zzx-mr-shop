package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.config.RedisRepository;
import com.baidu.shop.constant.MrConstant;
import com.baidu.shop.dto.UserDTO;
import com.baidu.shop.entity.UserEntity;
import com.baidu.shop.mapper.UserMapper;
import com.baidu.shop.utils.BCryptUtil;
import com.baidu.shop.utils.TenXunBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 2 * @ClassName UserServiceImpl
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/10
 * 6 * @Version V1.0
 * 7
 **/
@RestController
public class UserServiceImpl extends BaseApiService implements UserService{

    @Autowired
    private RedisRepository redisRepository;

    @Resource
    private UserMapper userMapper;



    @Override
    public Result<JSONObject> register(UserDTO userDTO) {
        UserEntity userEntity = TenXunBeanUtil.copyProperties(userDTO, UserEntity.class);
        userEntity.setPassword(BCryptUtil.hashpw(userEntity.getPassword(),BCryptUtil.gensalt()));
        userEntity.setCreated(new Date());
        userMapper.insertSelective(userEntity);
        return this.setResultSuccess();
    }

    @Override
    public Result<List<UserEntity>> checkUserNameOrPhone(String value, Integer type) {
        Example example = new Example(UserEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(type != null && value != null){
            if(type == 1){
                criteria.andEqualTo("username",value);
            }else{
                criteria.andEqualTo("phone",value);
            }
        }
        List<UserEntity> userEntities = userMapper.selectByExample(example);
        return this.setResultSuccess(userEntities);
    }

    @Override
    public Result<JSONObject> sendCode(UserDTO userDTO) {
        //定义验证码
        String code=(int)((Math.random()*9+1)*100000)+"";
        //放到redis中.并设置过期时间.
            redisRepository.set("code-"+userDTO.getPhone(), code);
            redisRepository.expire(MrConstant.USER_CODE_+userDTO.getPhone(),60L);


        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> checkValidSend(String phone,String validCode ) {
        String code=redisRepository.get(MrConstant.USER_CODE_ +phone);

        if(!validCode.equals(code)){
            return this.setResultError("验证码不正确");
        }

        return this.setResultSuccess();
    }
}

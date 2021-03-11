package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.UserDTO;
import com.baidu.shop.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 2 * @ClassName UserService
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/10
 * 6 * @Version V1.0
 * 7
 **/
@Api(tags = "用户接口")
public interface UserService {

    @ApiOperation(value = "用户注册")
    @PostMapping(value = "user/register")
    Result<JSONObject> register(@RequestBody UserDTO userDTO);

    @ApiOperation(value = "校验用户名或手机号唯一")
    @GetMapping(value = "user/check/{value}/{type}")
    Result<List<UserEntity>> checkUserNameOrPhone(@PathVariable(value = "value") String value, @PathVariable(value = "type") Integer type);


    @ApiOperation(value = "发送验证码.并将数据放到redis")
    @PostMapping(value = "/user/send")
    Result<JSONObject> sendCode(@RequestBody UserDTO userDTO);


    @ApiOperation(value = "验证码校验")
    @GetMapping(value = "/user/checkValideSend")
    Result<JSONObject> checkValidSend(String phone,String validCode);



}

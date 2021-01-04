package com.baidu.shop.global;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.status.HTTPStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 2 * @ClassName GlobalException
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2020/12/24
 * 6 * @Version V1.0
 * 7
 **/
@RestControllerAdvice// 类似于aop  afterpostProcess增强方法.
@Slf4j
public class GlobalException {
    @ExceptionHandler(RuntimeException.class)
    public Result<JSONObject> test( Exception e){
        Result<JSONObject> result = new Result();
        result.setCode(HTTPStatus.ERROR);
        result.setMessage(e.getMessage());
        log.debug(e.getMessage());
        return result;
    }
    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    public Map<String,String> MethodArgumentNotValidHandler(MethodArgumentNotValidException exception) throws Exception {
        List<String> errorList = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.stream().forEach(error ->{
                    errorList.add("Field ="+error.getField()+"--->"+"msg="+error.getDefaultMessage());
            log.error("Field --> " + error.getField() + " : " + error.getDefaultMessage());
        });
        hashMap.put("code", HTTPStatus.IF_ERROR+"");

        hashMap.put("message", errorList.stream().collect(Collectors.joining(",")));
        return hashMap;
    }
}



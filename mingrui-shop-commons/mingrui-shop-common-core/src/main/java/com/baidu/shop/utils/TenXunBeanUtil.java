package com.baidu.shop.utils;

import org.springframework.beans.BeanUtils;

/**
 * 2 * @ClassName TengXunBeanUtil
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2020/12/25
 * 6 * @Version V1.0
 * 7
 **/
public class TenXunBeanUtil {

    public static   <T> T copyProperties(Object source,Class<T> tClass){
            T t=null;
        try {
        t = tClass.newInstance();
            BeanUtils.copyProperties(source,t);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}

package com.baidu.shop.utils;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 2 * @ClassName EsUtils
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/3
 * 6 * @Version V1.0
 * 7
 **/
public class EsUtils {

    public static HighlightBuilder getHighlightSetField(String ...field){


        if(field.length==0){
            return new HighlightBuilder();
        }
        HighlightBuilder highlightBuilder = new HighlightBuilder();

        Arrays.asList(field).stream().forEach(fields ->{
            highlightBuilder.field(fields);
        });
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");

        return highlightBuilder;
    }


    //将返回的内容替换成高亮
    public static <T> List<SearchHit<T>> getHighLightHit(List<SearchHit<T>> list){
        return list.stream().map(hit -> {

            Map<String, List<String>> highlightFields =
                    hit.getHighlightFields();
            highlightFields.forEach((key,value) -> {
                try {
                    T content = hit.getContent();


                    Method method = content.getClass().getMethod("set" +
                            firstCharUpperCase(key),String.class);

                    method.invoke(content,value.get(0));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
            return hit;
        }).collect(Collectors.toList());
    }


    //首字母大写,效率最高!
    private static String firstCharUpperCase(String name){
        char[] chars = name.toCharArray();
        chars[0] -= 32;
        return String.valueOf(chars);
    }






}

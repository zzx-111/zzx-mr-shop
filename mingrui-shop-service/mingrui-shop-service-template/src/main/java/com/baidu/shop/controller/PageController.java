package com.baidu.shop.controller;

import com.baidu.shop.service.PageService;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * 2 * @ClassName PageController
 * 3 * @Description: TODO
 * 4 * @Author zzx
 * 5 * @Date 2021/3/8
 * 6 * @Version V1.0
 * 7
 **/
//@Controller
//@RequestMapping("item")
public class PageController {

  //  @Autowired
    private PageService pageService;

   // @GetMapping("list")
    public String aa(){
        System.out.println("啊啊啊啊啊啊111111111111");

        return "zzx";
    }

  //  @GetMapping("{spuId}.html")
    public  String getSpuInfo(@PathVariable(value = "spuId") Integer spuId, ModelMap modelMap){
        Map<String,Object> map=pageService.getGoodsInfo(spuId);
        modelMap.putAll(map);
        return "item";
    }

}

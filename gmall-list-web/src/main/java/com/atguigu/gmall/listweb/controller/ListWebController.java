package com.atguigu.gmall.listweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.SkuESParams;
import com.atguigu.gmall.bean.SkuESResult;
import com.atguigu.gmall.service.ListService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ListWebController {
    @Reference
    private ListService listService;

    @RequestMapping("list.html")
    @ResponseBody
    public String getList(SkuESParams skuESParams){
        SkuESResult search = listService.search(skuESParams);
        return JSON.toJSONString(search);
    }
}

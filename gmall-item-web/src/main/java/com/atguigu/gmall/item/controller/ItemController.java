package com.atguigu.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.sku.SkuInfo;
import com.atguigu.gmall.bean.sku.SkuSaleAttrValue;
import com.atguigu.gmall.bean.spu.SpuSaleAttr;
import com.atguigu.gmall.service.ListService;
import com.atguigu.gmall.service.SkuInfoService;
import com.atguigu.gmall.service.SpuInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Reference
    private SkuInfoService skuInfoService;

    @Reference
    private SpuInfoService spuInfoService;

    @Reference
    private ListService listService;

    @RequestMapping("{skuId}.html")
    public String skuInfoPage(@PathVariable(value = "skuId") String skuId
            , Model model) {
        //sku信息
        SkuInfo skuInfo = skuInfoService.getSkuInfoById(skuId);
        model.addAttribute("skuInfo", skuInfo);
        //销售属性组合
        List<SpuSaleAttr> spuSaleAttrList = spuInfoService.getSpuSaleAttrListCheckBySku(skuInfo);
        model.addAttribute("saleAttrList", spuSaleAttrList);

        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfoService.getSkuSaleAttrValueListBySpu(skuInfo.getSpuId());
        //把列表变换成 valueid1|valueid2|valueid3:skuId  的 哈希表 用于在页面中定位查询
        String valueIdsKey = "";

        Map<String, String> valuesSkuMap = new HashMap<>();

        for (int i = 0; i < skuSaleAttrValueList.size(); i++) {
            SkuSaleAttrValue skuSaleAttrValue = skuSaleAttrValueList.get(i);
            if (valueIdsKey.length() != 0) {
                valueIdsKey = valueIdsKey + "|";
            }
            valueIdsKey = valueIdsKey + skuSaleAttrValue.getSaleAttrValueId();

            if ((i + 1) == skuSaleAttrValueList.size() || !skuSaleAttrValue.getSkuId().equals(skuSaleAttrValueList.get(i + 1).getSkuId())) {

                valuesSkuMap.put(valueIdsKey, skuSaleAttrValue.getSkuId());
                valueIdsKey = "";
            }
        }
        //更新点击热点
        listService.incrHotScore(skuId);

        //把map变成json串
        String valuesSkuJson = JSON.toJSONString(valuesSkuMap);

        model.addAttribute("valuesSkuJson", valuesSkuJson);
        return "item";
    }

}

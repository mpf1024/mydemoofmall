package com.atguigu.gmall.listweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.base.BaseAttrInfo;
import com.atguigu.gmall.bean.base.BaseAttrValue;
import com.atguigu.gmall.bean.sku.SkuESParams;
import com.atguigu.gmall.bean.sku.SkuESResult;
import com.atguigu.gmall.service.BaseManageService;
import com.atguigu.gmall.service.ListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class ListWebController {
    @Reference
    private ListService listService;

    @Reference
    private BaseManageService baseManageService;

    @RequestMapping("list.html")
    public String getList(SkuESParams skuESParams, Model model){

        checkoutParam(skuESParams);//检验一下表单数据

        //ES查询结果
        SkuESResult skuESResult = listService.search(skuESParams);

        //商品信息
        model.addAttribute("skuESInfoList",skuESResult.getSkuESInfoList());
        //分页信息
        model.addAttribute("totalPages",skuESResult.getTotalPages());
        model.addAttribute("pageNo",skuESParams.getPageNo());
        //查询平台属性信息
        List<BaseAttrInfo> attrList = baseManageService.getAttrList(skuESResult.getAttrValueIdList());
        //已经选择的属性
        List<BaseAttrValue> selectedAttrList = new ArrayList<>();
        //去掉已经选择了的属性值
        for (Iterator<BaseAttrInfo> attrIterator = attrList.iterator(); attrIterator.hasNext(); ) {
            BaseAttrInfo nextAttr =  attrIterator.next();
            List<BaseAttrValue> attrValueList = nextAttr.getAttrValueList();
            for (BaseAttrValue attrValue : attrValueList) {
                String valId = attrValue.getId();
                String[] selectedIds = skuESParams.getValueId();
                if (selectedIds != null) {
                    for (String selectedId : selectedIds) {
                        if (selectedId.equals(valId)) {
                            //构造面包屑
                            BaseAttrValue bv = new BaseAttrValue();
                            String av = nextAttr.getAttrName() + ":" + attrValue.getValueName();
                            bv.setValueName(av);
                            bv.setAttrId(valId);
                            //排除掉自己的ID，点击时则会排除直接的查询条件，即页面叉掉查询条件
                            bv.setUrlParam(makeUrlParam(skuESParams,valId));
                            selectedAttrList.add(bv);

                            attrIterator.remove(); //从基本平台属性条件中移除已选择的
                        }
                    }
                }

            }
        }
        //(未选择的)相关平台属性信息
        model.addAttribute("attrInfoList",attrList);
        //已经选择的属性列表(面包屑列表)
        model.addAttribute("selectedAttrList",selectedAttrList);

        //保存当前的查询条件
        String urlParam = makeUrlParam(skuESParams);//把skuESParams拼接成&param=value...的url字符串
        model.addAttribute("urlParam",urlParam);
        model.addAttribute("keyword",skuESParams.getKeyword());
        return "list";
    }

    private void checkoutParam(SkuESParams skuESParams) {
        String[] idsArr = skuESParams.getValueId();
        if(idsArr!=null&&idsArr.length >0){
            List<String> list = Arrays.asList(idsArr);
            //去掉重复的valueId
            HashSet<String> valueIdSet = new HashSet<>(list);
            String[] ids = new String[valueIdSet.size()];
            skuESParams.setValueId(valueIdSet.toArray(ids));
        }
    }

    //拼接urlParam
    private String makeUrlParam(SkuESParams skuESParams,String... excludeValueIds) {
        StringBuilder urlParam = new StringBuilder();
        if(skuESParams.getKeyword() !=null){
            urlParam.append("keyword=").append(skuESParams.getKeyword());
        }

        if(skuESParams.getCatalog3Id() !=null){
            urlParam.append("&catalog3Id=").append(skuESParams.getCatalog3Id());
        }

        String[] valueIds = skuESParams.getValueId();
        if(valueIds !=null && valueIds.length > 0){
            for (String valueId : valueIds) {
                if(excludeValueIds !=null && excludeValueIds.length >0){
                    //排除掉指定的属性值ID
                    if(valueId.equals(excludeValueIds[0]))
                    {
                        continue;
                    }
                }
                urlParam.append("&valueId=").append(valueId);
            }
        }

        if(urlParam.indexOf("&") == 0){
            urlParam.deleteCharAt(0);
        }
        return urlParam.toString();
    }
}

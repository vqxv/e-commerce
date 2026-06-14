package com.ch.ebusiness.controller.before;

import com.ch.ebusiness.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;

    @RequestMapping("/")
    public String index(Model model, Integer tid) {
        return indexService.index(model, tid);
    }

    @RequestMapping("/search")
    public String search(Model model, String mykey) {
        return indexService.search(model, mykey);
    }

    @RequestMapping("/goodsDetail")
    public String goodsDetail(Model model, Integer id) {
        return indexService.goodsDetail(model, id);
    }
}

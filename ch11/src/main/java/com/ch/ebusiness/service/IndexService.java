package com.ch.ebusiness.service;

import org.springframework.ui.Model;

public interface IndexService {
    String index(Model model, Integer tid);

    String search(Model model, String mykey);

    String goodsDetail(Model model, Integer id);
}

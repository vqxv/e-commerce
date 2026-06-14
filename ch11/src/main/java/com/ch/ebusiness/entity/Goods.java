package com.ch.ebusiness.entity;

import org.springframework.web.multipart.MultipartFile;

public class Goods {
    private Integer id;
    private String gname;
    private double goprice;
    private double grprice;
    private int gstore;
    private String gpicture;
    private int isRecommend;
    private int isAdvertisement;
    private Integer goodstype_id;
    private String typename;
    private Integer buyNumber;
    private MultipartFile fileName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public double getGoprice() {
        return goprice;
    }

    public void setGoprice(double goprice) {
        this.goprice = goprice;
    }

    public double getGrprice() {
        return grprice;
    }

    public void setGrprice(double grprice) {
        this.grprice = grprice;
    }

    public int getGstore() {
        return gstore;
    }

    public void setGstore(int gstore) {
        this.gstore = gstore;
    }

    public String getGpicture() {
        return gpicture;
    }

    public void setGpicture(String gpicture) {
        this.gpicture = gpicture;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public int getIsAdvertisement() {
        return isAdvertisement;
    }

    public void setIsAdvertisement(int isAdvertisement) {
        this.isAdvertisement = isAdvertisement;
    }

    public Integer getGoodstype_id() {
        return goodstype_id;
    }

    public void setGoodstype_id(Integer goodstype_id) {
        this.goodstype_id = goodstype_id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public Integer getBuyNumber() {
        return buyNumber;
    }

    public void setBuyNumber(Integer buyNumber) {
        this.buyNumber = buyNumber;
    }

    public MultipartFile getFileName() {
        return fileName;
    }

    public void setFileName(MultipartFile fileName) {
        this.fileName = fileName;
    }
}

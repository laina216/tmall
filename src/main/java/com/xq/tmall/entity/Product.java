package com.xq.tmall.entity;

import lombok.Data;

import java.util.List;

/**
 * 产品
 */
@Data
public class Product {
    private Integer product_id;
    /**
     * 产品名称
     */
    private String product_name;
    /**
     * 产品标题
     */
    private String product_title;
    /**
     * 原价
     */
    private Double product_price;
    /**
     * 促销价
     */
    private Double product_sale_price;
    /**
     * 创建日期
     */
    private String product_create_date;
    /**
     * 类别id
     */
    private Category product_category;
    /**
     * 是否可用
     */
    private Byte product_isEnabled;
    private List<PropertyValue> propertyValueList;
    private List<ProductImage> singleProductImageList;
    private List<ProductImage> detailProductImageList;
    private List<Review> reviewList;
    private List<ProductOrderItem> productOrderItemList;
    //销量数
    private Integer product_sale_count;
    //评价数
    private Integer product_review_count;
    public Object setProduct_sale_co;

}

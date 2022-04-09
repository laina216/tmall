package com.xq.tmall.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 订单组
 */
@Data
public class OrderGroup {
    @JsonFormat(pattern = "MM/dd")
    private Date productOrder_pay_date;
    private Integer productOrder_count;
    private Byte productOrder_status;
}
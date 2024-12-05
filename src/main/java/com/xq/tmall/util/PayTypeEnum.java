
package com.xq.tmall.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 */
@Getter
@AllArgsConstructor
public enum PayTypeEnum {

    WEIXIN_SCAN(1, "微信扫码支付", "wxScanPay"), ALIPAY_SCAN(2, "支付宝扫码支付", "aliScanPay");

    private Integer code;

    private String desc;

    /**
     * 实现
     */
    private String impl;

    /**
     * 根据编码获取枚举信息
     *
     * @param code 编码
     * @return 枚举信息
     */
    public static PayTypeEnum byCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PayTypeEnum val : PayTypeEnum.values()) {
            if (val.getCode().equals(code)) {
                return val;
            }
        }
        return null;
    }

}

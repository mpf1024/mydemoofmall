package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.order.PaymentInfo;

public interface PaymentService {
    void  savePaymentInfo(PaymentInfo paymentInfo);

    PaymentInfo getPaymentInfo(PaymentInfo paymentInfo);

    void updatePaymentInfo(String out_trade_no, PaymentInfo paymentInfoUpd);

    boolean refund(String orderId);
}

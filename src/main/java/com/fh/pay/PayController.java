package com.fh.pay;

import com.fh.common.ServerResponse;
import com.fh.sdk.MyConfig;
import com.fh.sdk.WXPay;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("payController")
public class PayController {

    @RequestMapping("createNative")
    public ServerResponse createNative(String orderNo, BigDecimal totalPrice){

        try {
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "飞狐购物中心");
        data.put("out_trade_no", orderNo);
        data.put("device_info", "WEB");
        data.put("fee_type", "CNY");
        data.put("total_fee", "1");
        data.put("spbill_create_ip", "123.12.12.123");
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        data.put("product_id", "12");

        //设置过期时间
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sim.format(DateUtils.addMinutes(new Date(), 2));
        data.put("time_expire", format);
        Map<String, String> resp = wxpay.unifiedOrder(data);
        System.out.println(resp);
        //判断是否通信成功
        if(!resp.get("return_code").equalsIgnoreCase("SUCCESS")){
            return ServerResponse.error("微信支付平台错误:"+resp.get("return_msg"));
        }
        //判断是否通信成功
        if(!resp.get("return_code").equalsIgnoreCase("SUCCESS")){
            return ServerResponse.error("微信支付平台错误:"+resp.get("err_code_des"));
        }
        //微信支付的URL
            String code_url = resp.get("code_url");
            return ServerResponse.success(code_url);


        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error(e.getMessage());
        }
    }

    @RequestMapping("queryOrderStatus")
    public ServerResponse queryOrderStatus(String orderNo){
        try {
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", orderNo);

        int count = 0;

        for (;;){
            Map<String, String> resp = wxpay.orderQuery(data);
            System.out.println(resp);
            //判断是否通信成功result_code
            if(!resp.get("return_code").equalsIgnoreCase("SUCCESS")){
                return ServerResponse.error("微信支付平台错误:"+resp.get("return_msg"));
            }
            //判断是否通信成功
            if(!resp.get("result_code").equalsIgnoreCase("SUCCESS")){
                return ServerResponse.error("微信支付平台错误:"+resp.get("err_code_des"));
            }
            //交易状态
            if(resp.get("trade_state").equalsIgnoreCase("SUCCESS")){
                System.out.println("==============进入成功======支付成功=================");
                return ServerResponse.success("支付成功");
            }

            count++;
            //每次循环睡眠3秒
            Thread.sleep(3000);
            if (count > 5){
                return ServerResponse.error("支付超时");
            }
        }

        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error(e.getMessage());
        }


    }



}

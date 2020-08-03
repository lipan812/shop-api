package com.fh.sms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.fh.annotation.Ignore;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.util.AliyunSmsUtils;
import com.fh.util.RedisUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("msg")
public class SendMsg {

    @RequestMapping("sendMsg")
    @Ignore
    public ServerResponse sendMsg(String phone){
        String code = AliyunSmsUtils.getCode();
            try {
            SendSmsResponse sendSmsResponse = AliyunSmsUtils.sendSms(phone,code);
            if (sendSmsResponse !=null && "OK".equals(sendSmsResponse.getCode())){
                RedisUtil.setEx(phone,code, SystemConstant.REDIS_KEY_EXPIRE);
                return ServerResponse.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ServerResponse.success();
    }

}

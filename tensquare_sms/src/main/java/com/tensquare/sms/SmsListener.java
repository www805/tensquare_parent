package com.tensquare.sms;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * 发送短信验证码监听类
 * 以后工作中重点 接收消息进行业务处理
 */
@Component
@RabbitListener(queues = "sms")
public class SmsListener {
    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String templateCode;

    @Value("${aliyun.sms.sign_name}")
    private String signName;


    //接收短信验证码 发送短信验证码给用户
    @RabbitHandler
    public void sendSms(Map<String, String> map) {
        System.out.println(map.get("mobile")+"==="+map.get("code"));
        //param {"code":}
        try {
            smsUtil.sendSms(map.get("mobile"),templateCode,signName,"{\"code\":"+map.get("code")+"}");
        } catch (ClientException e) {
            ///修改为日志对象 输出日志
            System.out.println(e.getMessage());
        }

    }
}

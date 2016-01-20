package cn.globalph.passport.service;

import cn.globalph.passport.domain.WechatCustomer;

import java.util.List;

public interface WechatCustomerService {

    void active(WechatCustomer wechatCustomer);

    WechatCustomer save(WechatCustomer wechatCustomer);

    WechatCustomer readWechatCustomerFromWechat(String openId);

    WechatCustomer readWechatCustomerByCustomerId(Long customerId);

    List<WechatCustomer> readWechatCustomersByCustomerId(Long customerId);

    WechatCustomer readWechatCustomerByOpenId(String openId);

    List<WechatCustomer> readWechatCustomersByOpenId(String openId);

    boolean isOpenIdBinded(String openId);

    WechatCustomer create();

    void deleteOpenId(String openId);

}

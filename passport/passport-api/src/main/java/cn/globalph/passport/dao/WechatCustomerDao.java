package cn.globalph.passport.dao;

import cn.globalph.common.dao.BasicEntityDao;
import cn.globalph.passport.domain.WechatCustomer;

import java.util.List;

public interface WechatCustomerDao extends BasicEntityDao<WechatCustomer> {

    WechatCustomer readWechatCustomerByOpenId(String openId);

    WechatCustomer get(String id);

    List<WechatCustomer> readWechatCustomersByOpenId(String openId);

    WechatCustomer create();

    void deleteOpenId(String openId);

    WechatCustomer readWechatCustomerByCustomerId(Long customerId);

    List<WechatCustomer> readWechatCustomersByCustomerId(Long customerId);

    WechatCustomer readWechatCustomerFromWechat(String openId);

    void active(String openId, Long customerId);
}

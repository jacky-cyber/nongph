package cn.globalph.passport.service;

import cn.globalph.passport.dao.WechatCustomerDao;
import cn.globalph.passport.domain.WechatCustomer;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("phWechatCustomerService")
public class WechatCustomerServiceImpl implements WechatCustomerService {

    @Resource(name = "phWechatCustomerDao")
    protected WechatCustomerDao wechatCustomerDao;

    @Override
    public void active(WechatCustomer wechatCustomer) {
        wechatCustomerDao.active(wechatCustomer.getOpenId(), wechatCustomer.getCustomerId());
    }

    public WechatCustomer save(WechatCustomer wechatCustomer) {
        return wechatCustomerDao.persist(wechatCustomer);
    }

    @Override
    public WechatCustomer readWechatCustomerFromWechat(String openId) {
        return wechatCustomerDao.readWechatCustomerFromWechat(openId);
    }

    @Override
    public WechatCustomer readWechatCustomerByCustomerId(Long customerId) {
        return wechatCustomerDao.readWechatCustomerByCustomerId(customerId);
    }

    @Override
    public List<WechatCustomer> readWechatCustomersByCustomerId(Long customerId) {
        return wechatCustomerDao.readWechatCustomersByCustomerId(customerId);
    }

    public WechatCustomer readWechatCustomerByOpenId(String openId) {
        return wechatCustomerDao.readWechatCustomerByOpenId(openId);
    }

    public List<WechatCustomer> readWechatCustomersByOpenId(String openId) {
        return wechatCustomerDao.readWechatCustomersByOpenId(openId);
    }

    @Override
    public boolean isOpenIdBinded(String openId) {
        List<WechatCustomer> wechatCustomerList = readWechatCustomersByOpenId(openId);
        return wechatCustomerList != null && wechatCustomerList.size() > 1;
    }

    public WechatCustomer create() {
        return wechatCustomerDao.create();
    }

    @Override
    public void deleteOpenId(String openId) {
        wechatCustomerDao.deleteOpenId(openId);
    }
}

package cn.globalph.logistics.service;

import cn.globalph.logistics.ph.Express;

public interface ExpressService {

    Express save(Express Express);

    Express readByExpressNo(String expressNo);

    Express create();

    void delete(Express Express);

    void deleteByExpressNo(String expressNo);

    Express readByOrderNo(String orderNo);

    String generateExpressNo();
}

package cn.globalph.logistics.dao;

import cn.globalph.common.dao.BasicEntityDao;
import cn.globalph.logistics.ph.Express;

public interface ExpressDao extends BasicEntityDao<Express> {

    Express save(Express Express);

    Express create();

    Express readByExpressNo(String expressNo);

    Express readByOrderNo(String orderNo);
}

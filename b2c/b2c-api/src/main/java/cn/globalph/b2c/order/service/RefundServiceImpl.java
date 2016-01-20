package cn.globalph.b2c.order.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.globalph.b2c.order.dao.RefundDao;
import cn.globalph.b2c.order.domain.Refund;

@Service("phRefundService")
public class RefundServiceImpl implements RefundService {
	@Resource(name = "phRefundDao")
	private RefundDao refundDao;

	@Override
	public Refund createRefund() {
		return refundDao.create();
	}
	
	public Refund saveRefund(Refund refund){
		return refundDao.persist(refund);
	}
}

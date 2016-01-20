package cn.globalph.b2c.order.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.globalph.b2c.order.dao.RefundMediaDao;
import cn.globalph.b2c.order.domain.RefundMedia;

@Service("phRefundMediaService")
public class RefundMediaServiceImpl implements RefundMediaService {
	@Resource(name = "phRefundMediaDao")
	private RefundMediaDao refundMediaDao;
	
	@Override
	public RefundMedia createRefundMedia(){
		return refundMediaDao.create();
	}

	@Override
	public RefundMedia saveRefundMedia(RefundMedia refundMedia) {
		return refundMediaDao.persist(refundMedia);
	}
}

package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.domain.Refund;

public interface RefundService {
	public Refund createRefund();
	
	public Refund saveRefund(Refund refund);
}
